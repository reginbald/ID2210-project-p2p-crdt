package se.kth.app.mngr

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.AppComp
import se.kth.app.broadcast.{EagerReliableBroadcast, GossipingBestEffortBroadcastComponent, NoWaitCausalBroadcast}
import se.kth.app.links.PerfectPointToPointLink
import se.kth.app.logoot.Logoot
import se.kth.app.ports._
import se.kth.app.sim.logoot.LogootTestClient
import se.kth.croupier.util.NoView
import se.sics.kompics.{Channel, Negative, Positive, Start}
import se.sics.kompics.network.Network
import se.sics.kompics.sl._
import se.sics.kompics.timer.Timer
import se.sics.ktoolbox.croupier.CroupierPort
import se.sics.ktoolbox.overlaymngr.OverlayMngrPort
import se.sics.ktoolbox.overlaymngr.events.OMngrCroupier
import se.sics.ktoolbox.util.identifiable.overlay.OverlayId
import se.sics.ktoolbox.util.network.KAddress
import se.sics.ktoolbox.util.overlays.view.{OverlayViewUpdate, OverlayViewUpdatePort}

/**
  * Created by reginbald on 26/04/2017.
  */
class AppMngrComp(init: Init[AppMngrComp]) extends ComponentDefinition with StrictLogging {
  //*****************************CONNECTIONS**********************************
  private val omngrPort = requires[OverlayMngrPort]
  //***************************EXTERNAL_STATE*********************************
  private val (extPorts, self, croupierId, simulation) = init match {
    case Init(extPorts: ExtPort, self: KAddress, croupierId: OverlayId, simulation: Int) => (extPorts, self, croupierId, simulation)
  }
  //***************************INTERNAL_STATE*********************************
  private val appComp = create(classOf[AppComp], Init[AppComp](self))
  private val perfectLinkComp = create(classOf[PerfectPointToPointLink], Init[PerfectPointToPointLink](self))
  private val gossipBEBComp = create(classOf[GossipingBestEffortBroadcastComponent], Init[GossipingBestEffortBroadcastComponent](self))
  private val eagerRBComp = create(classOf[EagerReliableBroadcast], Init[EagerReliableBroadcast](self))
  private val causalBroadcastComp = create(classOf[NoWaitCausalBroadcast], Init[NoWaitCausalBroadcast](self))
  private val logootComp = create(classOf[Logoot], Init[Logoot](self))

  //******************************AUX_STATE***********************************
  private var pendingCroupierConnReq = None: Option[OMngrCroupier.ConnectRequest]
  //******************************FOR_TEST************************************
  private val client = create(classOf[LogootTestClient], Init[LogootTestClient](self, simulation))

  //**************************************************************************

  ctrl uponEvent {
    case _: Start => handle {
      logger.info("Starting...")
      pendingCroupierConnReq = Some(new OMngrCroupier.ConnectRequest(croupierId,false))
      trigger(pendingCroupierConnReq.get, omngrPort)
    }
  }

  omngrPort uponEvent {
    case _: OMngrCroupier.ConnectResponse => handle {
      logger.info("Overlays connected")
      connectPerfectLinkComp()
      connectGossipBEBComp()
      connectEagerRBComp()
      connectCausalBroadcastComp()
      connectLogootComp()
      connectAppComp()
      connectTestClient()

      trigger(new OverlayViewUpdate.Indication[NoView](croupierId, false, new NoView), extPorts.viewUpdate)
    }
  }

  private def connectPerfectLinkComp(){
    logger.info("Connecting Perfect Link Component")
    connect(perfectLinkComp.getNegative(classOf[Network]), extPorts.network, Channel.TWO_WAY)
  }

  private def connectGossipBEBComp(){
    logger.info("Connecting Gossip BEB Component")
    connect(gossipBEBComp.getNegative(classOf[CroupierPort]), extPorts.croupier, Channel.TWO_WAY)
    connect(perfectLinkComp.getPositive(classOf[PerfectLink]), gossipBEBComp.getNegative(classOf[PerfectLink]), Channel.TWO_WAY)
  }

  private def connectEagerRBComp(){
    logger.info("Connecting Gossip BEB Component")
    connect(gossipBEBComp.getPositive(classOf[GossipingBestEffortBroadcast]), eagerRBComp.getNegative(classOf[GossipingBestEffortBroadcast]), Channel.TWO_WAY)
  }

  private def connectCausalBroadcastComp(){
    logger.info("Connecting No-Waiting Causal Broadcast Component")
    connect(eagerRBComp.getPositive(classOf[ReliableBroadcast]), causalBroadcastComp.getNegative(classOf[ReliableBroadcast]), Channel.TWO_WAY)
  }

  private def connectLogootComp() {
    logger.info("connecting logoot component")
    connect(causalBroadcastComp.getPositive(classOf[CausalOrderReliableBroadcast]), logootComp.getNegative(classOf[CausalOrderReliableBroadcast]), Channel.TWO_WAY)
  }

  private def connectAppComp() {
    logger.info("Connecting App Component")
    connect(appComp.getNegative(classOf[Timer]), extPorts.timer, Channel.TWO_WAY)
    connect(perfectLinkComp.getPositive(classOf[PerfectLink]), appComp.getNegative(classOf[PerfectLink]), Channel.TWO_WAY)
    connect(causalBroadcastComp.getPositive(classOf[CausalOrderReliableBroadcast]), appComp.getNegative(classOf[CausalOrderReliableBroadcast]), Channel.TWO_WAY)
    connect(logootComp.getPositive(classOf[LogootPort]), appComp.getNegative(classOf[LogootPort]), Channel.TWO_WAY)
  }

  private def connectTestClient() ={
    logger.info("Connecting Test Client Component")
    connect(client.getNegative(classOf[Timer]), extPorts.timer, Channel.TWO_WAY)
    connect(client.getNegative(classOf[CroupierPort]), extPorts.croupier, Channel.TWO_WAY)
    connect(appComp.getPositive(classOf[AppPort]), client.getNegative(classOf[AppPort]), Channel.TWO_WAY)

  }
}

case class ExtPort(
          timer: Positive[Timer],
          network: Positive[Network],
          croupier: Positive[CroupierPort],
          viewUpdate: Negative[OverlayViewUpdatePort]
        )

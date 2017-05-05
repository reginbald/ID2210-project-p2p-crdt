package se.kth.system

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.mngr.{AppMngrComp, ExtPort}
import se.sics.kompics.{Channel, Component, Positive, Start}
import se.sics.kompics.sl._
import se.sics.kompics.network.Network
import se.sics.kompics.timer.Timer
import se.sics.ktoolbox.cc.heartbeat.CCHeartbeatPort
import se.sics.ktoolbox.croupier.CroupierPort
import se.sics.ktoolbox.omngr.bootstrap.BootstrapClientComp
import se.sics.ktoolbox.overlaymngr.OverlayMngrComp
import se.sics.ktoolbox.overlaymngr.OverlayMngrPort
import se.sics.ktoolbox.util.identifiable.overlay.OverlayId
import se.sics.ktoolbox.util.network.KAddress
import se.sics.ktoolbox.util.network.nat.NatAwareAddress
import se.sics.ktoolbox.util.overlays.view.OverlayViewUpdatePort

/**
  * Created by reginbald on 26/04/2017.
  */
class HostMngrComp(init: Init[HostMngrComp]) extends ComponentDefinition with StrictLogging {
  //*****************************CONNECTIONS**********************************
  private val timerPort: Positive[Timer] = requires[Timer]
  private val networkPort: Positive[Network] = requires[Network]
  //***************************EXTERNAL_STATE*********************************
  private val (selfAdr, bootstrapServer, croupierId) = init match {
    case Init(selfAdr: KAddress, bootstrapServer: KAddress, croupierId: OverlayId) => (selfAdr, bootstrapServer, croupierId)
  }
  //***************************INTERNAL_STATE*********************************
  private var bootstrapClientComp = create(classOf[BootstrapClientComp], new BootstrapClientComp.Init(selfAdr, bootstrapServer))
  private var overlayMngrComp = createOverlayMngr()
  private var appMngrComp = createApp()

  connectBootstrapClient()
  connectApp()

  ctrl uponEvent {
    case _: Start => handle {
      logger.info("HostMngrComp starting ...")
    }
  }

  private def connectBootstrapClient() {
    connect(bootstrapClientComp.getNegative(classOf[Timer]), timerPort, Channel.TWO_WAY)
    connect(bootstrapClientComp.getNegative(classOf[Network]), networkPort, Channel.TWO_WAY)
  }

  private def createOverlayMngr() :Component = {
    val extPorts: OverlayMngrComp.ExtPort = new OverlayMngrComp.ExtPort(timerPort, networkPort, bootstrapClientComp.getPositive(classOf[CCHeartbeatPort]))
    return create(classOf[OverlayMngrComp], new OverlayMngrComp.Init(selfAdr.asInstanceOf[NatAwareAddress], extPorts))
  }

  private def createApp() : Component = {
    val extPorts: ExtPort = new ExtPort(timerPort, networkPort, overlayMngrComp.getPositive(classOf[CroupierPort]), overlayMngrComp.getNegative(classOf[OverlayViewUpdatePort]))
    return create(classOf[AppMngrComp], Init[AppMngrComp](extPorts, selfAdr, croupierId))
  }

  private def connectApp() {
    connect(appMngrComp.getNegative(classOf[OverlayMngrPort]), overlayMngrComp.getPositive(classOf[OverlayMngrPort]), Channel.TWO_WAY)
  }
}


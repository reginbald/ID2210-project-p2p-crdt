package se.kth.system

import com.typesafe.scalalogging.StrictLogging
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
  private val LOG: Logger = LoggerFactory.getLogger(classOf[HostMngrComp])
  private var logPrefix: String = " "
  //*****************************CONNECTIONS**********************************
  private val timerPort: Positive[Timer] = requires[Timer]
  private val networkPort: Positive[Network] = requires[Network]
  //***************************EXTERNAL_STATE*********************************
  private val (selfAdr, bootstrapServer, croupierId) = init match {
    case Init(selfAdr: KAddress, bootstrapServer: KAddress, croupierId: OverlayId) => (selfAdr, bootstrapServer, croupierId)
  }
  //***************************INTERNAL_STATE*********************************
  private var bootstrapClientComp = None: Option[Component]
  private var overlayMngrComp = None: Option[Component]
  private var appMngrComp = None: Option[Component]

  connectBootstrapClient()
  connectOverlayMngr()
  connectApp()

  ctrl uponEvent {
    case _: Start => handle {
      logger.info("HostMngrComp starting ...")
    }
  }

  private def connectBootstrapClient() {
    bootstrapClientComp = Some(create(classOf[BootstrapClientComp], new BootstrapClientComp.Init(selfAdr, bootstrapServer)))
    connect(bootstrapClientComp.get.getNegative(classOf[Timer]), timerPort, Channel.TWO_WAY)
    connect(bootstrapClientComp.get.getNegative(classOf[Network]), networkPort, Channel.TWO_WAY)
  }

  private def connectOverlayMngr() {
    val extPorts: OverlayMngrComp.ExtPort = new OverlayMngrComp.ExtPort(timerPort, networkPort, bootstrapClientComp.get.getPositive(classOf[CCHeartbeatPort]))
    overlayMngrComp = Some(create(classOf[OverlayMngrComp], new OverlayMngrComp.Init(selfAdr.asInstanceOf[NatAwareAddress], extPorts)))
  }

  private def connectApp() {
    val extPorts: ExtPort = new ExtPort(timerPort, networkPort, overlayMngrComp.get.getPositive(classOf[CroupierPort]), overlayMngrComp.get.getNegative(classOf[OverlayViewUpdatePort]))
    appMngrComp = Some(create(classOf[AppMngrComp], Init[AppMngrComp](extPorts, selfAdr, croupierId)))
    connect(appMngrComp.get.getNegative(classOf[OverlayMngrPort]), overlayMngrComp.get.getPositive(classOf[OverlayMngrPort]), Channel.TWO_WAY)
  }
}


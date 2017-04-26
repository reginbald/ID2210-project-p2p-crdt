package se.kth.system

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import se.kth.app.mngr.AppMngrComp
import se.sics.kompics.{ComponentDefinition => _, Handler => _, _}
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
class HostMngrComp(int: Int[HostMngrComp]) extends ComponentDefinition{
  private val LOG: Logger = LoggerFactory.getLogger(classOf[HostMngrComp])
  private var logPrefix: String = " "
  //*****************************CONNECTIONS**********************************
  private[system] val timerPort: Positive[Timer] = requires(classOf[Timer])
  private[system] val networkPort: Positive[Network] = requires(classOf[Network])
  //***************************EXTERNAL_STATE*********************************
  private var selfAdr: KAddress = null
  private var bootstrapServer: KAddress = null
  private var croupierId: OverlayId = null
  //***************************INTERNAL_STATE*********************************
  private var bootstrapClientComp: Component = null
  private var overlayMngrComp: Component = null
  private var appMngrComp: Component = null

  def this(init: HostMngrComp.Init) {
    this()
    selfAdr = init.selfAdr
    logPrefix = "<nid:" + selfAdr.getId + ">"
    LOG.info("{}initiating...", logPrefix)
    bootstrapServer = init.bootstrapServer
    croupierId = init.croupierId
    subscribe(handleStart, control)
  }

  private[system] val handleStart: Handler[_ <: KompicsEvent] = new Handler[Start]() {
    def handle(event: Start) {
      LOG.info("{}starting...", logPrefix)
      connectBootstrapClient()
      connectOverlayMngr()
      connectApp()
      trigger(Start.event, bootstrapClientComp.control)
      trigger(Start.event, overlayMngrComp.control)
      trigger(Start.event, appMngrComp.control)
    }
  }

  private def connectBootstrapClient() {
    bootstrapClientComp = create(classOf[BootstrapClientComp], new BootstrapClientComp.Init(selfAdr, bootstrapServer))
    connect(bootstrapClientComp.getNegative(classOf[Timer]), timerPort, Channel.TWO_WAY)
    connect(bootstrapClientComp.getNegative(classOf[Network]), networkPort, Channel.TWO_WAY)
  }

  private def connectOverlayMngr() {
    val extPorts: OverlayMngrComp.ExtPort = new OverlayMngrComp.ExtPort(timerPort, networkPort, bootstrapClientComp.getPositive(classOf[CCHeartbeatPort]))
    overlayMngrComp = create(classOf[OverlayMngrComp], new OverlayMngrComp.Init(selfAdr.asInstanceOf[NatAwareAddress], extPorts))
  }

  private def connectApp() {
    val extPorts: AppMngrComp.ExtPort = new AppMngrComp.ExtPort(timerPort, networkPort, overlayMngrComp.getPositive(classOf[CroupierPort]), overlayMngrComp.getNegative(classOf[OverlayViewUpdatePort]))
    appMngrComp = create(classOf[AppMngrComp], new AppMngrComp.Init(extPorts, selfAdr, croupierId))
    connect(appMngrComp.getNegative(classOf[OverlayMngrPort]), overlayMngrComp.getPositive(classOf[OverlayMngrPort]), Channel.TWO_WAY)
  }

  class Init(val selfAdr: KAddress, val bootstrapServer: KAddress, val croupierId: OverlayId) extends se.sics.kompics.Init[HostMngrComp] {
  }
}

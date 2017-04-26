package se.kth.app.mngr

import com.typesafe.scalalogging.StrictLogging
import org.slf4j.LoggerFactory
import se.kth.app.AppComp
import se.kth.croupier.util.NoView
import se.sics.kompics.network.Network
import se.sics.kompics.{Channel, Negative, Positive, Start}
import se.sics.kompics.sl._
import se.sics.kompics.timer.Timer
import se.sics.ktoolbox.croupier.CroupierPort
import se.sics.ktoolbox.omngr.bootstrap.BootstrapClientComp
import se.sics.ktoolbox.overlaymngr.OverlayMngrPort
import se.sics.ktoolbox.overlaymngr.events.OMngrCroupier
import se.sics.ktoolbox.util.identifiable.overlay.OverlayId
import se.sics.ktoolbox.util.network.KAddress
import se.sics.ktoolbox.util.overlays.view.{OverlayViewUpdate, OverlayViewUpdatePort}

/**
  * Created by reginbald on 26/04/2017.
  */
class AppMngrComp extends ComponentDefinition with StrictLogging {
  //private val LOG = LoggerFactory.getLogger(classOf[BootstrapClientComp])
  //private var logPrefix = ""
  //*****************************CONNECTIONS**********************************
  private[mngr] val omngrPort = requires[OverlayMngrPort]//requires(classOf[OverlayMngrPort])
  //***************************EXTERNAL_STATE*********************************
  private var extPorts = null
  private var selfAdr = null
  private var croupierId = null
  //***************************INTERNAL_STATE*********************************
  private var appComp = null
  //******************************AUX_STATE***********************************
  private var pendingCroupierConnReq = null
  //**************************************************************************
  def this(init: AppMngrComp.Init) {
    this()
    selfAdr = init.selfAdr
    logPrefix = "<nid:" + selfAdr.getId + ">"
    LOG.info("{}initiating...", logPrefix)
    extPorts = init.extPorts
    croupierId = init.croupierOId
    subscribe(handleStart, control)
    subscribe(handleCroupierConnected, omngrPort)
  }

  private[mngr] val handleStart = new Handler[Start]() {
    def handle(event: Start) {
      LOG.info("{}starting...", logPrefix)
      pendingCroupierConnReq = new OMngrCroupier.ConnectRequest(croupierId, false)
      trigger(pendingCroupierConnReq, omngrPort)
    }
  }
  private[mngr] val handleCroupierConnected = new Handler[OMngrCroupier.ConnectResponse]() {
    def handle(event: OMngrCroupier.ConnectResponse) {
      LOG.info("{}overlays connected", logPrefix)
      connectAppComp()
      trigger(Start.event, appComp.control)
      trigger(new OverlayViewUpdate.Indication[NoView](croupierId, false, new NoView), extPorts.viewUpdatePort)
    }
  }

  private def connectAppComp() {
    appComp = create(classOf[AppComp], new AppComp.Init(selfAdr, croupierId))
    connect(appComp.getNegative(classOf[Timer]), extPorts.timerPort, Channel.TWO_WAY)
    connect(appComp.getNegative(classOf[Network]), extPorts.networkPort, Channel.TWO_WAY)
    connect(appComp.getNegative(classOf[CroupierPort]), extPorts.croupierPort, Channel.TWO_WAY)
  }

  class Init(val extPorts: AppMngrComp.ExtPort, val selfAdr: KAddress, val croupierOId: OverlayId) extends se.sics.kompics.Init[AppMngrComp] {
  }

  class ExtPort(val timerPort: Positive[Timer], val networkPort: Positive[Network], val croupierPort: Positive[CroupierPort], val viewUpdatePort: Negative[OverlayViewUpdatePort]) {
  }
}

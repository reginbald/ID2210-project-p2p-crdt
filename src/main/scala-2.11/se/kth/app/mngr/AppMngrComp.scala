package se.kth.app.mngr

import com.typesafe.scalalogging.StrictLogging
import org.slf4j.LoggerFactory
import se.kth.app.AppComp
import se.kth.app.mngr.AppMngrComp.ExtPort
import se.kth.croupier.util.NoView
import se.sics.kompics.network.Network
import se.sics.kompics.{ComponentDefinition => _, Init => _, _}
import se.sics.kompics.sl._
import se.sics.kompics.timer.Timer
import se.sics.ktoolbox.croupier.CroupierPort
import se.sics.ktoolbox.omngr.bootstrap.BootstrapClientComp
import se.sics.ktoolbox.overlaymngr.OverlayMngrPort
import se.sics.ktoolbox.overlaymngr.events.OMngrCroupier
import se.sics.ktoolbox.overlaymngr.events.OMngrCroupier.ConnectRequest
import se.sics.ktoolbox.util.identifiable.overlay.OverlayId
import se.sics.ktoolbox.util.network.KAddress
import se.sics.ktoolbox.util.overlays.view.{OverlayViewUpdate, OverlayViewUpdatePort}

/**
  * Created by reginbald on 26/04/2017.
  */
class AppMngrComp(init: Init[AppMngrComp]) extends ComponentDefinition with StrictLogging {
  //private val LOG = LoggerFactory.getLogger(classOf[BootstrapClientComp])
  //private var logPrefix = ""
  //*****************************CONNECTIONS**********************************
  //private[mngr] val omngrPort = requires[OverlayMngrPort]//requires(classOf[OverlayMngrPort])
  private val omngrPort = requires[OverlayMngrPort]
  //***************************EXTERNAL_STATE*********************************
  private val extPorts = init match {
    case Init(e: ExtPort) => e
  }

  private val self = init match {
    case Init(s: KAddress) => s
  }

  private val croupierId = init match {
    case Init(c: OverlayId) => c
  }
  //***************************INTERNAL_STATE*********************************
  private var appComp = None: Option[Component]
  //******************************AUX_STATE***********************************
  private var pendingCroupierConnReq = None: Option[OMngrCroupier.ConnectRequest]
  //**************************************************************************

  ctrl uponEvent {
    case _: Start => handle {
      //logger.info("{}starting...", logPrefix)
      logger.info("Starting...")
      pendingCroupierConnReq = Some(new OMngrCroupier.ConnectRequest(croupierId,false))
      //new OMngrCroupier.ConnectRequest(croupierId, false)
      trigger(pendingCroupierConnReq.get, omngrPort)
    }
  }

  omngrPort uponEvent {
    case event: OMngrCroupier.ConnectResponse => handle {
      logger.info("Overlays connected")
      connectAppComp()
      appComp match {
        case Some(mainApp) =>
          trigger(Start.event -> mainApp.control())
          trigger(new OverlayViewUpdate.Indication[NoView](croupierId, false, new NoView), extPorts.viewUpdate)
        case None =>
          logger.error("Application component does not exist. Exiting")
          throw new RuntimeException("Application component is None")
      }
    }
  }

  private def connectAppComp() {
    appComp = Some(create(classOf[AppComp], Init[AppComp](self)))

    appComp match {
      case Some(a) =>
        connect(a.getNegative(classOf[Timer]), extPorts.timer, Channel.TWO_WAY)
        connect(a.getNegative(classOf[Network]), extPorts.network, Channel.TWO_WAY)
        connect(a.getNegative(classOf[CroupierPort]), extPorts.croupier, Channel.TWO_WAY)
      case None =>
        logger.error("AppMngrComp error from connectAppComp ")
        throw new RuntimeException("Application component is None")
    }
  }

   /*case class ExtPort(
    timer: Positive[Timer],
    network: Positive[Network],
    croupier: Positive[CroupierPort],
    viewUpdate: Negative[OverlayViewUpdatePort]
  )*/

}
object AppMngrComp {

  case class ExtPort(
    timer: Positive[Timer],
    network: Positive[Network],
    croupier: Positive[CroupierPort],
    viewUpdate: Negative[OverlayViewUpdatePort]
  )

}
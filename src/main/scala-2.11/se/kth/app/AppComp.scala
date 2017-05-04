package se.kth.app

import java.util.UUID

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events._
import se.kth.app.ports.{GossipingBestEffortBroadcast, PerfectLink, ReliableBroadcast}
import se.kth.app.test.{Ping, Pong}
import se.sics.kompics.Start
import se.sics.kompics.sl._
import se.sics.kompics.timer._
import se.sics.ktoolbox.croupier.CroupierPort
import se.sics.ktoolbox.croupier.event.CroupierSample
import se.sics.ktoolbox.util.network.{KAddress, KHeader}
import se.sics.ktoolbox.util.network.basic.{BasicContentMsg, BasicHeader}

import scala.collection.JavaConversions._

/**
  * Created by reginbald on 25/04/2017.
  */
class AppComp(init: Init[AppComp]) extends ComponentDefinition with StrictLogging {
  //*******************************CONNECTIONS********************************
  val timerPort: PositivePort[Timer] = requires[Timer]
  val pLinkPort: PositivePort[PerfectLink] = requires[PerfectLink]
  val croupierPort: PositivePort[CroupierPort] = requires[CroupierPort]
  //val gBEBPort: PositivePort[GossipingBestEffortBroadcast] = requires[GossipingBestEffortBroadcast]
  val reliableBroadcastPort: PositivePort[ReliableBroadcast] = requires[ReliableBroadcast]

  //**************************************************************************
  private var timerId: Option[UUID] = None;


  private val self = init match {
    case Init(s: KAddress) => s
  }

  ctrl uponEvent {
    case _: Start => handle {
      logger.info("Starting...")
      val spt = new SchedulePeriodicTimeout(0, 2000);
      val timeout = PingTimeout(spt);
      spt.setTimeoutEvent(timeout);
      trigger(spt -> timerPort);
      timerId = Some(timeout.getTimeoutId());
    }
  }

  timerPort uponEvent {
    case PingTimeout(_) => handle {
      logger.info("Got Timeout event")
      //trigger(TMessage(THeader(self, ponger, Transport.TCP), Ping) -> net);
    }
  }

  croupierPort uponEvent {
    case sample:CroupierSample[_] => handle {
      logger.info("Got Timeout event")
      trigger(new RB_Broadcast(new Ping) -> reliableBroadcastPort)
    }
  }

  reliableBroadcastPort uponEvent {
    case RB_Deliver(src: KAddress, ping:Ping)  => handle {
      logger.info("Received ping from: " + src)
      trigger(new PL_Send(src, new Pong), pLinkPort)
    }
  }

  pLinkPort uponEvent {
    case PL_Deliver(src: KAddress, pong:Pong) => handle {
      logger.info("Received pong from: " + src)
    }
  }

  override def tearDown(): Unit = {
    timerId match {
      case Some(id) =>
        logger.debug("CancelPeriodicTimeout")
        trigger(new CancelPeriodicTimeout(id) -> timerPort);
      case None => // nothing
    }
  }
}

case class PingTimeout(spt: SchedulePeriodicTimeout) extends Timeout(spt)
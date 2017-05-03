package se.kth.app

import java.util.UUID

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events.{GBEB_Broadcast, GBEB_Deliver, PL_Deliver, PL_Send}
import se.kth.app.ports.{GossipingBestEffortBroadcast, PerfectLink}
import se.kth.app.test.{Ping, Pong}
import se.sics.kompics.Start
import se.sics.kompics.network.{Network, Transport}
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
  //val networkPort: PositivePort[Network] = requires[Network]
  val croupierPort: PositivePort[CroupierPort] = requires[CroupierPort]
  val gBEBPort: PositivePort[GossipingBestEffortBroadcast] = requires[GossipingBestEffortBroadcast]
  val pLinkPort: PositivePort[PerfectLink] = requires[PerfectLink]
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
      trigger(new GBEB_Broadcast(new Ping) -> gBEBPort)
    }
  }

  //croupierPort uponEvent {
  //  case sample:CroupierSample[_] => handle {
  //    if(!sample.publicSample.isEmpty){
  //      logger.info("Handling croupier sample")
  //      val samples = sample.publicSample.values().map{ x => x.getSource}
  //      samples.foreach{ peer: KAddress =>
  //        val header = new BasicHeader[KAddress](self, peer, Transport.UDP)
  //        val msg = new BasicContentMsg[KAddress, KHeader[KAddress], Ping](header, new Ping)
  //        trigger(msg -> networkPort)
  //      }
  //    } else {
  //      logger.debug("Croupier sample is empty")
  //    }
  //  }
  //}
//
  //networkPort uponEvent {
  //  case msg:BasicContentMsg[_, _, Ping] => handle {
  //    logger.info("Received ping from: " + msg.getHeader.getSource)
  //    trigger(msg.answer(new Pong), networkPort)
  //  }
  //  case msg:BasicContentMsg[_, _, Pong] => handle {
  //    logger.info("Received pong from: " + msg.getHeader.getSource)
  //  }
  //}

  gBEBPort uponEvent {
    case GBEB_Deliver(src: KAddress, ping:Ping)  => handle {
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
package se.kth.app

import java.util.UUID

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events._
import se.kth.app.ports.{AppPort, CausalOrderReliableBroadcast, PerfectLink}
import se.kth.app.test.{Ping, Pong}
import se.sics.kompics.{KompicsEvent, Start}
import se.sics.kompics.sl._
import se.sics.kompics.timer._
import se.sics.ktoolbox.util.network.KAddress

import scala.collection.JavaConversions._

/**
  * Created by reginbald on 25/04/2017.
  */
class AppComp(init: Init[AppComp]) extends ComponentDefinition with StrictLogging {
  //*******************************CONNECTIONS********************************
  val appPort: NegativePort[AppPort] = provides[AppPort]

  val timerPort: PositivePort[Timer] = requires[Timer]
  val pLinkPort: PositivePort[PerfectLink] = requires[PerfectLink]
  val broadcastPort: PositivePort[CausalOrderReliableBroadcast] = requires[CausalOrderReliableBroadcast]
  //**************************************************************************

  private val self = init match {
    case Init(s: KAddress) => s
  }

  ctrl uponEvent {
    case _: Start => handle {
      logger.info("Starting...")
    }
  }

  appPort uponEvent {
    case AppIn(payload: KompicsEvent) => handle {
      trigger(new CORB_Broadcast(new Ping(payload)) -> broadcastPort)
    }
  }

  broadcastPort uponEvent {
    case CORB_Deliver(src: KAddress, ping@Ping(payload: KompicsEvent)) => handle {
      logger.info("Received ping from: " + src)
      trigger(new PL_Send(src, new Pong), pLinkPort)
      trigger(new AppOut(src, payload), appPort)
    }
  }

  pLinkPort uponEvent {
    case PL_Deliver(src: KAddress, pong: Pong) => handle {
      logger.info("Received pong from: " + src)
      trigger(new AppOut(src, pong), appPort)
    }
  }
}
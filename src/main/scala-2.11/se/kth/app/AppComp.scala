package se.kth.app

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events._
import se.kth.app.ports.{AppPort, CausalOrderReliableBroadcast, PerfectLink, ReliableBroadcast}
import se.kth.app.test.{Ping, Pong}
import se.sics.kompics.Start
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
  val broadcastPort: PositivePort[ReliableBroadcast] = requires[ReliableBroadcast]
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
    case AppIn(payload: Ping) => handle {
      trigger(new RB_Broadcast(payload) -> broadcastPort)
    }
  }

  broadcastPort uponEvent {
    case RB_Deliver(src: KAddress, ping:Ping) => handle {
      if (!src.equals(self)){
        trigger(new PL_Send(src, new Pong(self, ping.id)), pLinkPort)
        trigger(new AppOut(src, ping), appPort)
      }
    }
  }

  pLinkPort uponEvent {
    case PL_Deliver(src: KAddress, pong: Pong) => handle {
      trigger(new AppOut(src, pong), appPort)
    }
  }
}
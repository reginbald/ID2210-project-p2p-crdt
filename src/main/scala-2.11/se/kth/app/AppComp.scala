package se.kth.app

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events._
import se.kth.app.ports.{AppPort, CausalOrderReliableBroadcast, LogootPort, PerfectLink}
import se.sics.kompics.Start
import se.sics.kompics.sl._
import se.sics.kompics.timer._
import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 25/04/2017.
  */
class AppComp(init: Init[AppComp]) extends ComponentDefinition with StrictLogging {
  //*******************************CONNECTIONS********************************
  val appPort: NegativePort[AppPort] = provides[AppPort]

  val timerPort: PositivePort[Timer] = requires[Timer]
  val pLinkPort: PositivePort[PerfectLink] = requires[PerfectLink]
  val broadcastPort: PositivePort[CausalOrderReliableBroadcast] = requires[CausalOrderReliableBroadcast]
  val logootPort: PositivePort[LogootPort] = requires[LogootPort]
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
      trigger(CORB_Broadcast(payload) -> broadcastPort)
    }
    case AppIn(logoot: Logoot_Do) => handle {
      trigger(logoot -> logootPort)
    }
    case AppIn(logoot: Logoot_Undo) => handle {
      trigger(logoot -> logootPort)
    }
    case AppIn(logoot: Logoot_Redo) => handle {
      trigger(logoot -> logootPort)
    }
    case AppIn(logoot: Logoot_Doc) => handle {
      trigger(logoot -> logootPort)
    }
  }

  logootPort uponEvent {
    case msg:Logoot_Doc => handle {
      trigger(AppOut(self, msg) -> appPort)
    }
    case msg:Logoot_Done => handle {
      trigger(AppOut(self, msg) -> appPort)
    }
    case msg:Logoot_Patch => handle {
      trigger(AppOut(self, msg) -> appPort)
    }
  }

  broadcastPort uponEvent {
    case CORB_Deliver(src: KAddress, ping:Ping) => handle {
      trigger(PL_Send(src, Pong(self, ping.id)), pLinkPort)
      trigger(AppOut(src, ping), appPort)
    }
  }

  pLinkPort uponEvent {
    case PL_Deliver(src: KAddress, pong: Pong) => handle {
      trigger(AppOut(src, pong), appPort)
    }
  }
}
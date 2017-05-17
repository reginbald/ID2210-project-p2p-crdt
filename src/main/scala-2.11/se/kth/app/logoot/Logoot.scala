package se.kth.app.logoot

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.broadcast.NoWaitCausalBroadcast
import se.kth.app.events.Logoot_Insert
import se.kth.app.ports.CausalOrderReliableBroadcast
import se.sics.kompics.Start
import se.sics.kompics.sl.{ComponentDefinition, Init, PositivePort, handle}


class Logoot(init: Init[Logoot]) extends ComponentDefinition with StrictLogging {
  val nwcb: PositivePort[CausalOrderReliableBroadcast] = requires[CausalOrderReliableBroadcast]

  var identifierTable: List[(Int, Int, Int)] = List.empty

  /* Logoot events */
  ctrl uponEvent {
    case _:Start => handle {
      logger.info("--- Logoot is starting ---")
    }
  }
  nwcb uponEvent {
    case Logoot_Insert(payload) => handle {
      logger.info("logoot received insert")
    }
  }


}

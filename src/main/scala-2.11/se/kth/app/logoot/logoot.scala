package se.kth.app.logoot

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.broadcast.NoWaitCausalBroadcast
import se.kth.app.events.{ERBData, GBEB_Broadcast, RB_Broadcast}
import se.sics.kompics.{KompicsEvent, Start}
import se.sics.kompics.sl.{ComponentDefinition, PositivePort, handle}

import scala.collection.mutable.Set

class logoot extends ComponentDefinition with StrictLogging {
  val nwcb: PositivePort[NoWaitCausalBroadcast] = requires[NoWaitCausalBroadcast]

  var identifierTable: List[(Int, Int, Int)] = List.empty

  /* Logoot events */
  ctrl uponEvent {
    case _:Start => handle {
      logger.info("--- Logoot is starting ---")
    }
  }
  /*rb uponEvent {
    case RB_Broadcast(payload) => handle {
      trigger(GBEB_Broadcast(ERBData(self, payload)) -> gbeb)
    }
  }*/


}

package se.kth.app.logoot

import com.typesafe.scalalogging.StrictLogging
import se.sics.kompics.{KompicsEvent, Start}
import se.sics.kompics.sl.{ComponentDefinition, handle}

import scala.collection.mutable.Set

class logoot extends ComponentDefinition with StrictLogging {

  var identifierTable : List[(Int, Int, Int)] = List.empty

  /* Logoot events */
  ctrl uponEvent {
    case _:Start => handle {
      logger.info("--- Logoot is starting ---")
    }
  }

}

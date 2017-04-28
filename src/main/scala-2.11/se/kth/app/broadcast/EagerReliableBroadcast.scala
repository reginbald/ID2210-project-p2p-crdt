package se.kth.app.broadcast

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.ports.BestEffortBroadcast
import se.sics.kompics.sl.{ComponentDefinition, Init}


class EagerReliableBroadcast(init: Init[EagerReliableBroadcast]) extends ComponentDefinition with StrictLogging {
  //EagerReliableBroadcast Subscriptions
  val beb = requires[BestEffortBroadcast];
  //val rb = provides[ReliableBroadcast];
}

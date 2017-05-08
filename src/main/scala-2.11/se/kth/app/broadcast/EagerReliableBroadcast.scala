package se.kth.app.broadcast

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events._
import se.kth.app.ports.{GossipingBestEffortBroadcast, ReliableBroadcast}
import se.sics.kompics.{KompicsEvent, Start}
import se.sics.kompics.sl._
import se.sics.ktoolbox.util.network.KAddress

import scala.collection.mutable.Set

class EagerReliableBroadcast(init: Init[EagerReliableBroadcast]) extends ComponentDefinition with StrictLogging {
  /* EagerReliableBroadcast Subscriptions */
  val gbeb: PositivePort[GossipingBestEffortBroadcast] = requires[GossipingBestEffortBroadcast]
  val rb: NegativePort[ReliableBroadcast] = provides[ReliableBroadcast]

  /* state */
  var delivered: Set[KompicsEvent] = Set.empty[KompicsEvent]
  val self: KAddress = init match {
    case Init(s: KAddress) => s
  }

  ctrl uponEvent {
    case _:Start => handle {
      logger.info("Starting...")
      delivered = Set.empty[KompicsEvent]
    }
  }

  /* EagerReliableBroadcast Event Handlers */
  rb uponEvent {
    case RB_Broadcast(payload) => handle {
      trigger(GBEB_Broadcast(ERBData(self, payload)) -> gbeb)
    }
  }

  gbeb uponEvent{
    case GBEB_Deliver(_:KAddress, ERBData(src:KAddress, m:KompicsEvent)) => handle {
      if(!delivered.contains(m)) {
        delivered +=  m
        trigger(RB_Deliver(src, m) -> rb)
        trigger(GBEB_Broadcast(ERBData(src, m)) -> gbeb)
      }
    }
  }
}

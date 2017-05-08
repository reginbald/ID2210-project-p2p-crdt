package se.kth.app.broadcast

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events._
import se.kth.app.ports.{GossipingBestEffortBroadcast, ReliableBroadcast}
import se.sics.kompics.{KompicsEvent, Start}
import se.sics.kompics.sl._
import se.sics.ktoolbox.util.network.KAddress

import scala.collection.mutable

class EagerReliableBroadcast(init: Init[EagerReliableBroadcast]) extends ComponentDefinition with StrictLogging {
  /* EagerReliableBroadcast Subscriptions */
  val gbeb: PositivePort[GossipingBestEffortBroadcast] = requires[GossipingBestEffortBroadcast]
  val rb: NegativePort[ReliableBroadcast] = provides[ReliableBroadcast]

  /* state */
  var delivered: mutable.Set[KompicsEvent] = collection.mutable.Set[KompicsEvent]()
  val self: KAddress = init match {
    case Init(s: KAddress) => s
  }

  ctrl uponEvent {
    case _:Start => handle {
      logger.info("Starting...")
      delivered = collection.mutable.Set[KompicsEvent]()
    }
  }

  /* EagerReliableBroadcast Event Handlers */
  rb uponEvent {
    case RB_Broadcast(payload) => handle {
      trigger(GBEB_Broadcast(ERBData(payload)) -> gbeb)
    }
  }

  gbeb uponEvent{
    case GBEB_Deliver(src:KAddress, ERBData(m:KompicsEvent)) => handle {
      if(!delivered.contains(m)) {
        delivered += m
        trigger(RB_Deliver(src, m) -> rb)
        trigger(GBEB_Broadcast(ERBData(m)) -> gbeb)
      }
    }
  }
}

package se.kth.app.broadcast

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events._
import se.kth.app.ports.{GossipingBestEffortBroadcast, ReliableBroadcast}
import se.sics.kompics.KompicsEvent
import se.sics.kompics.network.Address
import se.sics.kompics.sl._
import se.sics.ktoolbox.util.network.KAddress

class EagerReliableBroadcast(init: Init[EagerReliableBroadcast]) extends ComponentDefinition with StrictLogging {
  /* EagerReliableBroadcast Subscriptions */
  val gbeb = requires[GossipingBestEffortBroadcast]
  val rb = provides[ReliableBroadcast]

  /* state */
  val delivered = collection.mutable.Set[KompicsEvent]()
  val self = init match {
    case Init(s: KAddress) => s
  };

  /* EagerReliableBroadcast Event Handlers */
  rb uponEvent {
    case RB_Broadcast(payload) => handle {
      /**/
      trigger(GBEB_Broadcast(new ERBData(payload)) -> gbeb)
    }
  }

  gbeb uponEvent{
    case GBEB_Deliver(src:KAddress, ERBData(m:KompicsEvent)) => handle {
      if(!delivered.contains(m)) {
        delivered += m
        trigger(new RB_Deliver(src, m) -> rb)
        trigger(GBEB_Broadcast(new ERBData(m)) -> gbeb)
      }
    }
  }
}

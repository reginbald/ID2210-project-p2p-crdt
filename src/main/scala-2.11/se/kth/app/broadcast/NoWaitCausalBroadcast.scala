package se.kth.app.broadcast

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events._
import se.kth.app.ports.{CausalOrderReliableBroadcast, ReliableBroadcast}
import se.sics.kompics.{KompicsEvent, Start}
import se.sics.kompics.sl.{ComponentDefinition, Init, NegativePort, PositivePort, handle}
import se.sics.ktoolbox.util.network.KAddress

import scala.collection.mutable

class NoWaitCausalBroadcast(init: Init[NoWaitCausalBroadcast]) extends ComponentDefinition with StrictLogging {
  // Ports
  var crb: NegativePort[CausalOrderReliableBroadcast] = provides[CausalOrderReliableBroadcast]
  val rb: PositivePort[ReliableBroadcast] = requires[ReliableBroadcast]

  var delivered: mutable.Set[KompicsEvent] = collection.mutable.Set[KompicsEvent]()
  var past: mutable.Set[(KAddress, KompicsEvent)] = collection.mutable.Set[(KAddress,KompicsEvent)]()

  val (self) = init match {
    case Init(s: KAddress) => s
  }

  ctrl uponEvent {
    case _:Start => handle {
      logger.info("Starting...")
      delivered = collection.mutable.Set[KompicsEvent]()
      past = collection.mutable.Set[(KAddress,KompicsEvent)]()
    }
  }

  crb uponEvent {
    case CORB_Broadcast(payload) => handle {
      trigger(RB_Broadcast(CORBData(past, payload)) -> rb)
      past += ((self, payload))
    }
  }

  rb uponEvent{
    case RB_Deliver(src:KAddress, CORBData(mPast:collection.mutable.Set[(KAddress,KompicsEvent)], payload:KompicsEvent)) => handle {
      if (!delivered.contains(payload)) {
        for (((s,n)) <- mPast) {
          if (!delivered.contains(n)) {
            trigger(CORB_Deliver(s,n) -> crb)
            delivered += n
            if (!past.contains(s,n)) {
              past += ((s,n))
            }
          }
        }
        trigger(CORB_Deliver(src,payload) -> crb)
        delivered += payload
        if (!past.contains((src,payload))){
          past.add((src,payload))
        }
      }
    }
  }
}
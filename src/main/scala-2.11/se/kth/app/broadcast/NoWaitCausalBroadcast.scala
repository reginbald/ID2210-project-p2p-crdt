package se.kth.app.broadcast

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events._
import se.kth.app.ports.{CausalOrderReliableBroadcast, ReliableBroadcast}
import se.sics.kompics.KompicsEvent
import se.sics.kompics.sl.{ComponentDefinition, Init, handle}
import se.sics.ktoolbox.util.network.KAddress

class NoWaitCausalBroadcast(init: Init[NoWaitCausalBroadcast]) extends ComponentDefinition with StrictLogging {
  var crb = provides[CausalOrderReliableBroadcast]
  val rb  = requires[ReliableBroadcast]

  val delivered = collection.mutable.Set[KompicsEvent]()
  val past      = collection.mutable.Set[(KAddress,KompicsEvent)]()

  val (self) = init match {
    case Init(s: KAddress) => s
  }

  crb uponEvent {
    case CORB_Broadcast(payload) => handle {
      trigger(RB_Broadcast(new CORBData(past, payload)) -> rb)
      past += ((self, payload))
    }
  }

  rb uponEvent{
    case RB_Deliver(src:KAddress, CORBData(mpast:collection.mutable.Set[(KAddress,KompicsEvent)], payload:KompicsEvent)) => handle {
      if (!delivered.contains(payload)) {
        for (((s,n)) <- mpast) {
          if (!delivered.contains(n)) {
            trigger(CORB_Deliver(s,n) -> crb)
            delivered += n
            if (!past.contains(s,n)) {
              past.add((s,n))
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
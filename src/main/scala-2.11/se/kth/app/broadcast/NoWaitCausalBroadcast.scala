package se.kth.app.broadcast

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events._
import se.kth.app.ports.{CausalOrderReliableBroadcast, ReliableBroadcast}
import se.sics.kompics.{KompicsEvent, Start}
import se.sics.kompics.sl.{ComponentDefinition, Init, NegativePort, PositivePort, handle}
import se.sics.ktoolbox.util.network.KAddress

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class NoWaitCausalBroadcast(init: Init[NoWaitCausalBroadcast]) extends ComponentDefinition with StrictLogging {
  // Ports
  var crb: NegativePort[CausalOrderReliableBroadcast] = provides[CausalOrderReliableBroadcast]
  val rb: PositivePort[ReliableBroadcast] = requires[ReliableBroadcast]

  var delivered: mutable.Set[KompicsEvent] = mutable.Set.empty[KompicsEvent]
  var past: mutable.ListBuffer[(KAddress, KompicsEvent)] = mutable.ListBuffer.empty[(KAddress,KompicsEvent)]

  val self: KAddress = init match { case Init(s: KAddress) => s }

  ctrl uponEvent {
    case _:Start => handle {
      logger.info("Starting...")
      delivered = mutable.Set.empty[KompicsEvent]
      past = ListBuffer.empty[(KAddress,KompicsEvent)]
    }
  }

  crb uponEvent {
    case CORB_Broadcast(payload) => handle {
      trigger(RB_Broadcast(CORBData(past.toList, payload)) -> rb)
      past += ((self, payload))
    }
  }

  rb uponEvent{
    case RB_Deliver(src:KAddress, CORBData(mPast:List[(KAddress,KompicsEvent)], payload:KompicsEvent)) => handle {
      val contains = delivered.contains(payload)
      val tmp = mPast
      if (!contains) {
        for ((s:KAddress, n:KompicsEvent) <- mPast) {
          if (!delivered.contains(n)) {
            trigger(CORB_Deliver(s, n) -> crb)
            delivered += n
            if (!past.contains(s, n)) {
              past += ((s, n))
            }
          }
        }
        trigger(CORB_Deliver(src, payload) -> crb)
        delivered += payload
        if (!past.contains((src,payload))){
          past += ((src,payload))
        }
      }
    }
  }
}
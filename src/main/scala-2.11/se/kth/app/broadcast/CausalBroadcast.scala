package se.kth.app.broadcast

import com.typesafe.scalalogging.StrictLogging
import org.javatuples.Tuple
import se.kth.app.events.{GBEB_Broadcast, RB_Broadcast}
import se.kth.app.ports.{CausalOrderReliableBroadcast, ReliableBroadcast}
import se.sics.kompics.KompicsEvent
import se.sics.kompics.sl.{ComponentDefinition, Init, handle}
import se.sics.ktoolbox.util.network.KAddress

class CausalBroadcast(init: Init[CausalBroadcast]) extends ComponentDefinition with StrictLogging {
  var crb = provides[CausalOrderReliableBroadcast]
  val rb  = requires[ReliableBroadcast]

  val delivered = collection.mutable.Set[KompicsEvent]()
  val past      = collection.mutable.Set[(KAddress,KompicsEvent)]()

  val (self) = init match {
    case Init(s: KAddress) => s
  }

  crb uponEvent {
    case RB_Broadcast(payload) => handle {
      trigger(RB_Broadcast(new Data(self,payload)) -> rb)
      past += ((self, payload))
    }
  }
}
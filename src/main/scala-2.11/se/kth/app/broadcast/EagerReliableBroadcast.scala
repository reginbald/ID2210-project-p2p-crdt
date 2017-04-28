package se.kth.app.broadcast

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events.{GBEB_Broadcast, RB_Broadcast}
import se.kth.app.ports.{GossipingBestEffortBroadcast, ReliableBroadcast}
import se.sics.kompics.KompicsEvent
import se.sics.kompics.network.Address
import se.sics.kompics.sl._
import se.sics.ktoolbox.util.network.KAddress

case class Data(src: KAddress, payload:KompicsEvent) extends KompicsEvent

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
      trigger(GBEB_Broadcast(new Data(self,payload)) -> gbeb)
    }
  }
}

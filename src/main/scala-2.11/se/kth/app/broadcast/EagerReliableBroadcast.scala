package se.kth.app.broadcast

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events.{GBEB_Broadcast, RB_Broadcast}
import se.kth.app.ports.ReliableBroadcast
import se.sics.kompics.Init.None
import se.sics.kompics.KompicsEvent
import se.sics.kompics.network.Address
import se.sics.kompics.sl.{ComponentDefinition, Init}


class EagerReliableBroadcast(init: Init[EagerReliableBroadcast]) extends ComponentDefinition with StrictLogging {
  /* EagerReliableBroadcast Subscriptions */
  val gbeb = requires[GossipingBestEffortBroadcastComponent]
  val rb = provides[ReliableBroadcast]

  /* state */
  val delivered = collection.mutable.Set[KompicsEvent]()
  val self = init match {
    case Init(s: Address) => s
  };

  /* EagerReliableBroadcast Event Handlers */

  /*rb uponEvent {
    case RB_Broadcast(payload) => handle {
      /**/

    }
  }*/
}

package se.kth.app.ports

import se.kth.app.events.{GBEB_Broadcast, GBEB_Deliver}
import se.sics.kompics.sl._

/**
  * Created by reginbald on 28/04/2017.
  */
class GossipingBestEffortBroadcast extends Port {
  indication[GBEB_Deliver];
  request[GBEB_Broadcast];
}

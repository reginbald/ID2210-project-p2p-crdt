package se.kth.app.broadcast

import se.kth.app.ports.{GossipingBestEffortBroadcast, PerfectLink}
import se.sics.kompics.sl._

/**
  * Created by reginbald on 28/04/2017.
  */
class GossipingBestEffortBroadcastComponent extends ComponentDefinition{
  val beb = provides[GossipingBestEffortBroadcast]
  val pLink = requires[PerfectLink]
  //val basicSample = requires[BasicSample] //Todo implement

  ctrl uponEvent


}

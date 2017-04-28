package se.kth.app.broadcast

import se.kth.app.events.{RB_Broadcast, RB_Deliver}
import se.sics.kompics.sl.Port

class ReliableBroadcast extends Port{
  indication[RB_Deliver];
  request[RB_Broadcast];
}

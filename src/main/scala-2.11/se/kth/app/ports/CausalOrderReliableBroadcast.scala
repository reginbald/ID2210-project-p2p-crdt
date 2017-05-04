package se.kth.app.ports

import se.kth.app.events.{CORB_Broadcast, CORB_Deliver}
import se.sics.kompics.sl.Port


class CausalOrderReliableBroadcast extends Port {
  indication[CORB_Deliver]
  request[CORB_Broadcast]
}
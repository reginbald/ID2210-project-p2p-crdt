package se.kth.app.events

import se.sics.kompics.KompicsEvent
import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 27/05/2017.
  */
case class RB_Broadcast(payload: KompicsEvent) extends KompicsEvent

case class RB_Deliver(source: KAddress, payload: KompicsEvent) extends KompicsEvent

case class ERBData(src:KAddress, payload:KompicsEvent) extends KompicsEvent
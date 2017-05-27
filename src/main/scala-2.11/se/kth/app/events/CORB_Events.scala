package se.kth.app.events

import se.sics.kompics.KompicsEvent
import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 27/05/2017.
  */
case class CORB_Broadcast(payload: KompicsEvent) extends KompicsEvent

case class CORB_Deliver(source: KAddress, payload: KompicsEvent) extends KompicsEvent

case class CORBData(past:List[(KAddress,KompicsEvent)], payload:KompicsEvent) extends KompicsEvent
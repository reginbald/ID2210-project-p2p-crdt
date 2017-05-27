package se.kth.app.events

import se.sics.kompics.KompicsEvent
import se.sics.ktoolbox.util.network.KAddress

import scala.collection.mutable.Set

/**
  * Created by reginbald on 27/05/2017.
  */
case class GBEB_Broadcast(payload: KompicsEvent) extends KompicsEvent

case class GBEB_Deliver(source: KAddress, payload: KompicsEvent) extends KompicsEvent

class HistoryRequest extends KompicsEvent

case class HistoryResponse(history:Set[(KAddress, KompicsEvent)]) extends KompicsEvent
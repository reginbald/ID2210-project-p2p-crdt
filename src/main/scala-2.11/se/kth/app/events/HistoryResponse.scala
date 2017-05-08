package se.kth.app.events

import se.sics.kompics.KompicsEvent
import se.sics.ktoolbox.util.network.KAddress

import scala.collection.mutable.Set

/**
  * Created by reginbald on 28/04/2017.
  */
case class HistoryResponse(history:Set[(KAddress, KompicsEvent)]) extends KompicsEvent

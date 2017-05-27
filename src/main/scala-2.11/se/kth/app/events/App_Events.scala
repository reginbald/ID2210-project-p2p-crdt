package se.kth.app.events

import se.sics.kompics.KompicsEvent
import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 27/05/2017.
  */
case class AppIn(payload: KompicsEvent) extends KompicsEvent

case class AppOut(source: KAddress, payload: KompicsEvent) extends KompicsEvent
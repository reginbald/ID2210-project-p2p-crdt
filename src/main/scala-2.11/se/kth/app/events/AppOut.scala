package se.kth.app.events

import se.sics.kompics.KompicsEvent
import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 04/05/2017.
  */
case class AppOut(source: KAddress, payload: KompicsEvent) extends KompicsEvent {

}

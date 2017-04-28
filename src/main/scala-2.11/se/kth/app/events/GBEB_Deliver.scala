package se.kth.app.events

import se.sics.kompics.KompicsEvent
import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 28/04/2017.
  */
case class GBEB_Deliver(source: KAddress, payload: KompicsEvent) extends KompicsEvent;
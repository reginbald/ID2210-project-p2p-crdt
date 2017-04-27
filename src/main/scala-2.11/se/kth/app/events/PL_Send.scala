package se.kth.app.events

import se.sics.kompics.KompicsEvent
import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 27/04/2017.
  */
case class PL_Send(dest: KAddress, payload: KompicsEvent) extends KompicsEvent
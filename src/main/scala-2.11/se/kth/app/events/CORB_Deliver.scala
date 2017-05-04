package se.kth.app.events

import se.sics.kompics.KompicsEvent
import se.sics.ktoolbox.util.network.KAddress

case class CORB_Deliver(source: KAddress, payload: KompicsEvent) extends KompicsEvent;

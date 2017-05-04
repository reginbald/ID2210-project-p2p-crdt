package se.kth.app.events

import se.sics.kompics.KompicsEvent
import se.sics.kompics.network.Address
import se.sics.ktoolbox.util.network.KAddress

case class RB_Deliver(source: KAddress, payload: KompicsEvent) extends KompicsEvent;

package se.kth.app.events

import se.sics.kompics.KompicsEvent
import se.sics.kompics.network.Address

case class RB_Deliver(source: Address, payload: KompicsEvent) extends KompicsEvent;

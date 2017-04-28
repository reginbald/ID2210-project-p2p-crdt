package se.kth.app.events

import se.sics.kompics.KompicsEvent

case class RB_Broadcast(payload: KompicsEvent) extends KompicsEvent;

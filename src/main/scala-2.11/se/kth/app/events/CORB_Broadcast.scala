package se.kth.app.events

import se.sics.kompics.KompicsEvent

case class CORB_Broadcast(payload: KompicsEvent) extends KompicsEvent;

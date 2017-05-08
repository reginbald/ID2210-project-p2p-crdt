package se.kth.app.events

import se.sics.kompics.KompicsEvent

case class ERBData(payload:KompicsEvent) extends KompicsEvent

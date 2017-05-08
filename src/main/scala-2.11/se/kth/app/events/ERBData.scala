package se.kth.app.events

import se.sics.kompics.KompicsEvent
import se.sics.ktoolbox.util.network.KAddress

case class ERBData(src:KAddress, payload:KompicsEvent) extends KompicsEvent

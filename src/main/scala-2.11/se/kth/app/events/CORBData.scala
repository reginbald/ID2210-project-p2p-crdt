package se.kth.app.events

import se.sics.kompics.KompicsEvent
import se.sics.ktoolbox.util.network.KAddress

case class CORBData(past:collection.mutable.ListBuffer[(KAddress,KompicsEvent)], payload:KompicsEvent) extends KompicsEvent

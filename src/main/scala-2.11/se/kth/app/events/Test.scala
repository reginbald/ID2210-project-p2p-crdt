package se.kth.app.events

import se.sics.kompics.KompicsEvent
import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 26/04/2017.
  */
case class Ping(src:KAddress, id:Int) extends KompicsEvent
case class Pong(src:KAddress, id:Int) extends KompicsEvent
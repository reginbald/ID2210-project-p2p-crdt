package se.kth.app.test

import se.sics.kompics.KompicsEvent

/**
  * Created by reginbald on 26/04/2017.
  */
case class Ping(data:KompicsEvent) extends KompicsEvent
case class Pong() extends KompicsEvent
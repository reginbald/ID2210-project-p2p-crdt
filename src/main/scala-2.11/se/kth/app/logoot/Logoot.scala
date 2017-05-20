package se.kth.app.logoot

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.broadcast.NoWaitCausalBroadcast
import se.kth.app.events.Logoot_Insert
import se.kth.app.ports.{CausalOrderReliableBroadcast, LogootPort}
import se.sics.kompics.Start
import se.sics.kompics.sl.{ComponentDefinition, Init, NegativePort, PositivePort, handle}
import se.sics.ktoolbox.util.network.KAddress

import scala.collection.mutable


class Logoot(init: Init[Logoot]) extends ComponentDefinition with StrictLogging {
  val nwcb: PositivePort[CausalOrderReliableBroadcast] = requires[CausalOrderReliableBroadcast]
  val rb: NegativePort[LogootPort] = provides[LogootPort]

  var identifierTable: mutable.ListBuffer[(Int, Int, Int)] = mutable.ListBuffer.empty
  var clock: mutable.Map[KAddress, Int] = mutable.Map.empty[KAddress, Int]

  /* Logoot events */
  ctrl uponEvent {
    case _:Start => handle {
      logger.info("--- Logoot is starting ---")
    }
  }
  nwcb uponEvent {
    case Logoot_Insert(payload) => handle {
      logger.info("logoot received insert")
    }
  }

  // Todo remove site will use self
  def generateLineId(p: LineId, q: LineId, N: Int, boundary: Int, site: KAddress): LineId = {
    var list:mutable.ListBuffer[Position] = new mutable.ListBuffer[Position]
    var index = 0
    var interval = 0
    while (interval < N){ // Finds a place for N identifiers
      index += 1
      interval = prefix_for_interval(q, index) - prefix_for_interval(p, index) - 1;
    }
    var step = math.min(interval/N, boundary)
    var r = prefix(p, index)
    for (j <-1 to N ){ // Constructs N identifiers
      list ++= constructId(r += Random(1, step), p, q, site).positions
      r += step;
    }
    new LineId(list)
  }

  def prefix_for_interval(p: LineId, index: Int): Int = { // Todo check if correct
    var out = 0
    for (i <- 0 to index - 1) {
      out += p.positions(i).digit * 10 ^ i
    }
    out
  }

  def prefix(p: LineId, index: Int): mutable.ListBuffer[Int] = { // Todo check if correct
    var out: mutable.ListBuffer[Int] = new mutable.ListBuffer[Int]
    for (i <- 0 to index - 1) {
      out += p.positions(i).digit
    }
    out
  }

  def Random(start: Int, end: Int): Int ={
    val rnd = new scala.util.Random
    start + rnd.nextInt( (end - start) + 1 )
  }


  // Todo remove site will use self
  def constructId(r: mutable.ListBuffer[Int], p: LineId, q: LineId, site: KAddress): LineId = {
    val id = new LineId(mutable.ListBuffer.empty)
    for( i <- 1 to r.size){
      val d = r(i)
      var s: KAddress = null
      var c = mutable.Map.empty[KAddress, Int]
      if(d == p.positions(i).digit){
        s = p.positions(i).siteId
        c = q.positions(i).clock
      } else if (d == q.positions(i).digit) {
        s = q.positions(i).siteId
        c = q.positions(i).clock
      } else {
        s = site // Todo change to self
        clock.get(site) match { // Todo check if works
          case Some(v) => clock(site) = v + 1
          case None => println("Got nothing")
        }
        c = clock
      }
      id.positions += new Position(d, s, c)
    }
    id
  }
}

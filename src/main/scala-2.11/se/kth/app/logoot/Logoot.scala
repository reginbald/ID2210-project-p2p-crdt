package se.kth.app.logoot

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.broadcast.NoWaitCausalBroadcast
import se.kth.app.events.Logoot_Insert
import se.kth.app.ports.CausalOrderReliableBroadcast
import se.sics.kompics.Start
import se.sics.kompics.sl.{ComponentDefinition, Init, PositivePort, handle}
import se.sics.ktoolbox.util.network.KAddress

import scala.collection.mutable


class Logoot(init: Init[Logoot]) extends ComponentDefinition with StrictLogging {
  val nwcb: PositivePort[CausalOrderReliableBroadcast] = requires[CausalOrderReliableBroadcast]

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


  def constructId(r: mutable.ListBuffer[Int], p: LineId, q: LineId, site: KAddress): LineId = {
    val id = new LineId(mutable.ListBuffer.empty)
    for( i <- 1 to r.size){
      val d = r(i)
      var s = KAddress
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

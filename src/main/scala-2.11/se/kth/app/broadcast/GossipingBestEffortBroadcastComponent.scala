package se.kth.app.broadcast

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events.{HistoryRequest, _}
import se.kth.app.ports.{BasicSample, GossipingBestEffortBroadcast, PerfectLink}
import se.sics.kompics.{KompicsEvent, Start}
import se.sics.kompics.sl._
import se.sics.ktoolbox.util.network.KAddress

import scala.collection.mutable.ListBuffer

/**
  * Created by reginbald on 28/04/2017.
  */
class GossipingBestEffortBroadcastComponent(init: Init[GossipingBestEffortBroadcastComponent]) extends ComponentDefinition with StrictLogging{
  val gbeb = provides[GossipingBestEffortBroadcast]
  val pLink = requires[PerfectLink]
  val bs = requires[BasicSample]

  var (self, past) = init match {case Init(self:KAddress, list: ListBuffer[(KAddress, KompicsEvent)]) => (self, list)}

  ctrl uponEvent {
    case _: Start => handle {
      logger.info("Starting...")
    }
  }

  gbeb uponEvent {
    case GBEB_Broadcast(payload: KompicsEvent) => handle {
      past += ((self, payload))
    }
  }

  bs uponEvent {
    case  s:Sample => handle {
      for (p <- s.sample) trigger(new PL_Send(p, new HistoryRequest) -> pLink)
    }
  }

  pLink uponEvent {
    case  PL_Deliver(src: KAddress, _:HistoryRequest) => handle {
      trigger(new PL_Send(src, new HistoryResponse(past)) -> pLink)
    }
    case PL_Deliver(src:KAddress, history:HistoryResponse) => handle {
      val unseen = history.history.diff(past)

      for ((pp, m) <- unseen) trigger(new GBEB_Deliver(pp, m) -> gbeb)
      past ++= unseen
    }
  }

}

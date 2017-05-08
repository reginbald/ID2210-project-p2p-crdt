package se.kth.app.broadcast

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events.{HistoryRequest, _}
import se.kth.app.ports.{GossipingBestEffortBroadcast, PerfectLink}
import se.sics.kompics.{KompicsEvent, Start}
import se.sics.kompics.sl._
import se.sics.ktoolbox.croupier.CroupierPort
import se.sics.ktoolbox.croupier.event.CroupierSample
import se.sics.ktoolbox.util.network.KAddress

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

/**
  * Created by reginbald on 28/04/2017.
  */
class GossipingBestEffortBroadcastComponent(init: Init[GossipingBestEffortBroadcastComponent]) extends ComponentDefinition with StrictLogging{
  val gbeb = provides[GossipingBestEffortBroadcast]
  val pLink = requires[PerfectLink]
  val croupier = requires[CroupierPort]

  var self = init match {case Init(self:KAddress) => self}
  var past = new ListBuffer[(KAddress, KompicsEvent)]

  ctrl uponEvent {
    case Start => handle {
      logger.info("Starting...")
    }
  }

  gbeb uponEvent {
    case GBEB_Broadcast(payload: KompicsEvent) => handle {
      past += ((self, payload))
    }
  }

  croupier uponEvent {
    case  sample:CroupierSample[_] => handle {
      logger.info("Croupier sample received")
      val nodes = sample.publicSample.values().map{ x => x.getSource}
      for (p <- nodes) trigger(new PL_Send(p, new HistoryRequest) -> pLink)
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

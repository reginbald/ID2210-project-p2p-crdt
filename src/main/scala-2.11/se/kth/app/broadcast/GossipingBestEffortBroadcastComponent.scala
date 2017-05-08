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
import scala.collection.mutable.Set

/**
  * Created by reginbald on 28/04/2017.
  */
class GossipingBestEffortBroadcastComponent(init: Init[GossipingBestEffortBroadcastComponent]) extends ComponentDefinition with StrictLogging{
  val gbeb: NegativePort[GossipingBestEffortBroadcast] = provides[GossipingBestEffortBroadcast]
  val pLink: PositivePort[PerfectLink] = requires[PerfectLink]
  val croupier: PositivePort[CroupierPort] = requires[CroupierPort]

  var self: KAddress = init match {case Init(self:KAddress) => self}
  var past: Set[(KAddress, KompicsEvent)] = Set.empty[(KAddress, KompicsEvent)]

  ctrl uponEvent {
    case _:Start => handle {
      logger.info("Starting...")
      past = Set.empty[(KAddress, KompicsEvent)]
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
      if(!sample.publicSample.isEmpty){
        val nodes = sample.publicSample.values().map{ x => x.getSource}
        for (p <- nodes) trigger(PL_Send(p, new HistoryRequest) -> pLink)

      }
    }
  }

  pLink uponEvent {
    case  PL_Deliver(src: KAddress, _:HistoryRequest) => handle {
      trigger(PL_Send(src, HistoryResponse(past)) -> pLink)
    }
    case PL_Deliver(_:KAddress, HistoryResponse(history)) => handle {
      val unseen = history.diff(past)

      for ((pp, m) <- unseen) trigger(GBEB_Deliver(pp, m) -> gbeb)
      past ++= unseen
    }
  }

}

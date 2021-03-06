package se.kth.app.links

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events.{PL_Deliver, PL_Send}
import se.kth.app.ports.PerfectLink
import se.sics.kompics.KompicsEvent
import se.sics.kompics.network.{Network, Transport}
import se.sics.kompics.sl._
import se.sics.ktoolbox.util.network.{KAddress, KHeader}
import se.sics.ktoolbox.util.network.basic.{BasicContentMsg, BasicHeader}

/**
  * Created by reginbald on 27/04/2017.
  */
class PerfectPointToPointLink(init: Init[PerfectPointToPointLink]) extends ComponentDefinition with StrictLogging {
  val pLink: NegativePort[PerfectLink] = provides[PerfectLink]
  val net: PositivePort[Network] = requires[Network]

  val self: KAddress = init match { case Init(self:KAddress) => self }

  pLink uponEvent {
    case PL_Send(dest:KAddress, payload:KompicsEvent) => handle {
      val header = new BasicHeader[KAddress](self, dest, Transport.TCP)
      val message = new BasicContentMsg[KAddress, KHeader[KAddress], KompicsEvent](header, PL_Deliver(self, payload))
      trigger(message -> net)
    }
  }

  net uponEvent {
    case msg:BasicContentMsg[_,_,_] => handle{
      val content = msg.getContent
      content match {
        case plDeliver: PL_Deliver =>
          trigger(plDeliver -> pLink)
        case _ =>
      }
    }
  }
}

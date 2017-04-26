package se.kth.app

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.test.{Ping, Pong}
import se.sics.kompics.Start
import se.sics.kompics.network.{Network, Transport}
import se.sics.kompics.sl._
import se.sics.kompics.timer.Timer
import se.sics.ktoolbox.croupier.CroupierPort
import se.sics.ktoolbox.croupier.event.CroupierSample
import se.sics.ktoolbox.util.network.{KAddress, KHeader}
import se.sics.ktoolbox.util.network.basic.{BasicContentMsg, BasicHeader}

import scala.collection.JavaConversions._

/**
  * Created by reginbald on 25/04/2017.
  */
class AppComp(init: Init[AppComp]) extends ComponentDefinition with StrictLogging {
  //private var logPrefix = " "
  //*******************************CONNECTIONS********************************
  val timerPort: PositivePort[Timer] = requires[Timer]
  val networkPort: PositivePort[Network] = requires[Network]
  val croupierPort: PositivePort[CroupierPort] = requires[CroupierPort]
  //**************************************************************************

  private val self = init match {
    case Init(s: KAddress) => s
  }

  ctrl uponEvent {
    case _: Start => handle {
      //logger.info("{}starting...", logPrefix) Todo: do we need the prefix?
      logger.info("Starting...")
    }
  }

  croupierPort uponEvent {
    case sample:CroupierSample[_] => handle {
      if(!sample.publicSample.isEmpty){
        logger.info("Handling croupier sample")
        val samples = sample.publicSample.values().map{ x => x.getSource}
        samples.foreach{ peer: KAddress =>
          val header = new BasicHeader[KAddress](self, peer, Transport.UDP)
          val msg = new BasicContentMsg[KAddress, KHeader[KAddress], Ping](header, new Ping)
          trigger(msg -> networkPort)
        }
      } else {
        logger.debug("Croupier sample is empty")
      }
    }
  }

  networkPort uponEvent {
    case msg:BasicContentMsg[_, _, Ping] => handle {
      logger.info("Received ping from: " + msg.getHeader.getSource)
      trigger(msg.answer(new Pong), networkPort)
    }
    case msg:BasicContentMsg[_, _, Pong] => handle {
      logger.info("Received pong from: " + msg.getHeader.getSource)
    }
  }
}

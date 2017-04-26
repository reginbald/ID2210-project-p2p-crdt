package se.kth.app

import com.typesafe.scalalogging.StrictLogging
import org.slf4j.LoggerFactory
import se.kth.app.test.Ping
import se.kth.croupier.util.CroupierHelper
import se.sics.kompics.Start
import se.sics.kompics.network.{Network, Transport}
import se.sics.kompics.sl._
import se.sics.kompics.timer.Timer
import se.sics.ktoolbox.croupier.CroupierPort
import se.sics.ktoolbox.croupier.event.CroupierSample
import se.sics.ktoolbox.util.identifiable.Identifier
import se.sics.ktoolbox.util.network.{KAddress, KHeader}
import se.sics.ktoolbox.util.network.basic.{BasicContentMsg, BasicHeader}

import scala.collection.JavaConversions._

/**
  * Created by reginbald on 25/04/2017.
  */
class AppComp(init: Init[AppComp]) extends ComponentDefinition with StrictLogging {
  //private var logPrefix = " "
  //*******************************CONNECTIONS********************************
  val timerPort = requires[Timer]
  val networkPort = requires[Network]
  val croupierPort = requires[CroupierPort]
  //**************************************************************************

  private val self = init match {
    case Init(s: KAddress) => s
  }

 //def this(init: AppComp.Init) {
 //  this()
 //  logPrefix = "<nid:" + self.getId + ">"
 //  logger.info("{}initiating...", logPrefix)
 //  //subscribe(handleStart, control)
 //  //subscribe(handleCroupierSample, croupierPort)
 //  //subscribe(handlePing, networkPort)
 //  //subscribe(handlePong, networkPort)
 //}

  ctrl uponEvent {
    case _: Start => handle {
      //logger.info("{}starting...", logPrefix) Todo: do we need the prefix?
      logger.info("Starting...")
    }
  }

  croupierPort uponEvent {
    case sample:CroupierSample[_] => handle {
      if(!sample.publicSample.isEmpty()){
        val samples = sample.publicSample.values().map{ x => x.getSource()}
        samples.foreach{ peer: KAddress =>
          val header = new BasicHeader[KAddress](self, peer, Transport.UDP)
          val msg = new BasicContentMsg[KAddress, KHeader[KAddress], Ping](header, new Ping)
        }
      }
    }
  }


  //Handler handleCroupierSample = new Handler<CroupierSample>() {
  //  @Override
  //  public void handle(CroupierSample croupierSample) {
  //    if (croupierSample.publicSample.isEmpty()) {
  //      return;
  //    }
  //    List<KAddress> sample = CroupierHelper.getSample(croupierSample);
  //    for (KAddress peer : sample) {
  //      KHeader header = new BasicHeader(selfAdr, peer, Transport.UDP);
  //      KContentMsg msg = new BasicContentMsg(header, new Ping());
  //      trigger(msg, networkPort);
  //    }
  //  }
  //};

  //croupier uponEvent {
  //  case sample: CroupierSample[_] => handle {
  //    if (!sample.publicSample.isEmpty) {
  //      logger.info("Handling croupier sample")
  //      import scala.collection.JavaConversions._
  //      val samples = sample.publicSample.values().map { it => it.getSource }
  //      samples.foreach { peer: KAddress =>
  //        val header = new BasicHeader[KAddress](self, peer, Transport.UDP)
  //        val msg = new BasicContentMsg[KAddress, KHeader[KAddress], Ping](header, new Ping)
  //        trigger(msg -> network)
  //      }
  //    } else {
  //      logger.debug("Empty croupier sample")
  //    }
  //  }
  //}

  private[app] val handleCroupierSample = new Nothing() {
    def handle(croupierSample: Nothing) {
      if (croupierSample.publicSample.isEmpty) return
      val sample = CroupierHelper.getSample(croupierSample)
      import scala.collection.JavaConversions._
      for (peer <- sample) {
        val header = new Nothing(selfAdr, peer, Transport.UDP)
        val msg = new Nothing(header, new Nothing)
        trigger(msg, networkPort)
      }
    }
  }
  private[app] val handlePing = new Nothing() {
    def handle(content: Nothing, container: Nothing) {
      LOG.info("{}received ping from:{}", logPrefix, container.getHeader.getSource)
      trigger(container.answer(new Nothing), networkPort)
    }
  }
  private[app] val handlePong = new Nothing() {
    def handle(content: Nothing, container: Nothing) {
      LOG.info("{}received pong from:{}", logPrefix, container.getHeader.getSource)
    }
  }

  class Init(val selfAdr: KAddress, val gradientOId: Identifier) extends se.sics.kompics.Init[AppComp] {

  }
}

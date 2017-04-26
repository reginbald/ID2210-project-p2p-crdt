package se.kth.app

import java.util.List
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import se.kth.croupier.util.CroupierHelper
import se.kth.app.test.Ping
import se.kth.app.test.Pong
import se.sics.kompics.ClassMatchedHandler
import se.sics.kompics.ComponentDefinition
import se.sics.kompics.Handler
import se.sics.kompics.Positive
import se.sics.kompics.Start
import se.sics.kompics.network.Network
import se.sics.kompics.network.Transport
import se.sics.kompics.timer.Timer
import se.sics.ktoolbox.croupier.CroupierPort
import se.sics.ktoolbox.croupier.event.CroupierSample
import se.sics.ktoolbox.util.identifiable.Identifier
import se.sics.ktoolbox.util.network.KAddress
import se.sics.ktoolbox.util.network.KContentMsg
import se.sics.ktoolbox.util.network.KHeader
import se.sics.ktoolbox.util.network.basic.BasicContentMsg
import se.sics.ktoolbox.util.network.basic.BasicHeader

/**
  * Created by reginbald on 25/04/2017.
  */
class AppComp extends ComponentDefinition{
  private val LOG = LoggerFactory.getLogger(classOf[AppComp])
  private var logPrefix = " "
  //*******************************CONNECTIONS********************************
  private[app] val timerPort = requires(classOf[Nothing])
  private[app] val networkPort = requires(classOf[Nothing])
  private[app] val croupierPort = requires(classOf[Nothing])
  //**************************************************************************
  private var selfAdr = null

  def this(init: AppComp.Init) {
    this()
    selfAdr = init.selfAdr
    logPrefix = "<nid:" + selfAdr.getId + ">"
    LOG.info("{}initiating...", logPrefix)
    subscribe(handleStart, control)
    subscribe(handleCroupierSample, croupierPort)
    subscribe(handlePing, networkPort)
    subscribe(handlePong, networkPort)
  }

  private[app] val handleStart = new Nothing() {
    def handle(event: Nothing) {
      LOG.info("{}starting...", logPrefix)
    }
  }
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

  class Init(val selfAdr: Nothing, val gradientOId: Nothing) extends se.sics.kompics.Init[AppComp] {
  }
}

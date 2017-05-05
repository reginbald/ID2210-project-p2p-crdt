package se.kth.app.sim.ping

import java.util.UUID

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events.{AppIn, AppOut}
import se.kth.app.ports.AppPort
import se.kth.app.sim.{SimulationResultMap, SimulationResultSingleton}
import se.kth.app.test.{Ping, Pong}
import se.sics.kompics.Start
import se.sics.kompics.sl.{ComponentDefinition, Init, PositivePort, handle}
import se.sics.kompics.timer._
import se.sics.ktoolbox.croupier.CroupierPort
import se.sics.ktoolbox.croupier.event.CroupierSample
import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 04/05/2017.
  */

//case class PingTimeout(spt: ScheduleTimeout) extends Timeout(spt)

class PingTestClient(init: Init[PingTestClient]) extends ComponentDefinition with StrictLogging  {
  val timer:PositivePort[Timer] = requires[Timer]
  val appPort: PositivePort[AppPort] = requires[AppPort]
  val croupier:PositivePort[CroupierPort]  = requires[CroupierPort]

  private val res:SimulationResultMap = SimulationResultSingleton.getInstance
  //private var timerId: Option[UUID] = None;

  private val self = init match {
    case Init(s: KAddress) => s
  }

  var counter:Int = 0
  var ping:Int = 0
  var pong:Int = 0


  ctrl uponEvent {
    case _: Start => handle {
      logger.info("Starting test client")

      res.put(self.getId + "sent", counter)
      res.put(self.getId + "ping", ping)
      res.put(self.getId + "pong", pong)

      //val spt = new ScheduleTimeout(1000);
      //val timeout = new PingTimeout(spt)
      //spt.setTimeoutEvent(timeout)
      //trigger(spt -> timer);
      //timerId = Some(timeout.getTimeoutId());
    }
  }

  appPort uponEvent {
    case AppOut(src:KAddress, _:Ping) => handle {
      logger.info(self + " - Got ping from: " + src + " with id: " + id)
      ping += 1
      res.put(self.getId + "ping", ping)
    }
    case AppOut(src:KAddress, _:Pong) => handle {
      logger.info(self + " - Got pong from: " + src + " with id: " + id)
      pong += 1
      res.put(self.getId + "pong", pong)
    }
  }

  //timer uponEvent {
  //  case PingTimeout(_) => handle {
  //    logger.info("Sending Command")
  //    trigger(AppIn(new Ping()) -> appPort)
  //  }
  //}

  croupier uponEvent {
    case _:CroupierSample[_] => handle {
      if(counter < 5){
        logger.info("Sending Command")
        counter += 1
        res.put(self.getId + "sent", counter);
        trigger(AppIn(Ping()) -> appPort)
      }

    }
  }

  //override def tearDown(): Unit = {
  //  timerId match {
  //    case Some(id) =>
  //      trigger(new CancelTimeout(id) -> timer)
  //    case None => // nothing
  //  }
  //}

}

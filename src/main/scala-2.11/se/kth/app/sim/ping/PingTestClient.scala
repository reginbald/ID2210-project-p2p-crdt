package se.kth.app.sim.ping

import java.util.UUID

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events.{AppIn, AppOut}
import se.kth.app.ports.AppPort
import se.kth.app.sim.{SimulationResultMap, SimulationResultSingleton}
import se.sics.kompics.{KompicsEvent, Start}
import se.sics.kompics.sl.{ComponentDefinition, Init, PositivePort, handle}
import se.sics.kompics.timer._
import se.sics.ktoolbox.croupier.CroupierPort
import se.sics.ktoolbox.croupier.event.CroupierSample
import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 04/05/2017.
  */

case class Command(id:Int) extends KompicsEvent
case class PingTimeout(spt: ScheduleTimeout) extends Timeout(spt)

class PingTestClient(init: Init[PingTestClient]) extends ComponentDefinition with StrictLogging  {
  val timer = requires[Timer];
  val appPort: PositivePort[AppPort] = requires[AppPort]
  val croupier = requires[CroupierPort]

  private val res:SimulationResultMap = SimulationResultSingleton.getInstance
  private var timerId: Option[UUID] = None;

  private val self = init match {
    case Init(s: KAddress) => s
  }

  var counter:Int = 0


  ctrl uponEvent {
    case _: Start => handle {
      logger.info("Starting test client")
      val spt = new ScheduleTimeout(1000);
      val timeout = new PingTimeout(spt)
      spt.setTimeoutEvent(timeout)
      trigger(spt -> timer);
      timerId = Some(timeout.getTimeoutId());
    }
  }

  appPort uponEvent {
    case AppOut(src:KAddress, Command(id:Int)) => handle {
      logger.info("Got Command from: " + src + " with id: " + id)
    }
  }

  timer uponEvent {
    case PingTimeout(_) => handle {
      logger.info("Sending Command")
      trigger(AppIn(new Command(1)) -> appPort)
    }
  }

  croupier uponEvent {
    case  sample:CroupierSample[_] => handle {
      if(counter < 10){
        logger.info("Sending Command")
        counter += 1
        trigger(AppIn(new Command(counter)) -> appPort)
      }

    }
  }

  override def tearDown(): Unit = {
    timerId match {
      case Some(id) =>
        trigger(new CancelTimeout(id) -> timer)
      case None => // nothing
    }
  }

}

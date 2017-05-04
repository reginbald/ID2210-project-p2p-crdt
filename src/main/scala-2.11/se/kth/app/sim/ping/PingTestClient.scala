package se.kth.app.sim.ping

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events.{AppIn, AppOut, RB_Broadcast}
import se.kth.app.ports.AppPort
import se.kth.app.sim.{SimulationResultMap, SimulationResultSingleton}
import se.sics.kompics.{KompicsEvent, Start}
import se.sics.kompics.sl.{ComponentDefinition, Init, PositivePort, handle}
import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 04/05/2017.
  */

case class Command(id:Int) extends KompicsEvent

class PingTestClient(init: Init[PingTestClient]) extends ComponentDefinition with StrictLogging  {
  private val res:SimulationResultMap = SimulationResultSingleton.getInstance
  private val self = init match {
    case Init(s: KAddress) => s
  }
  val appPort: PositivePort[AppPort] = requires[AppPort]

  ctrl uponEvent {
    case _: Start => handle {
      logger.info("Sending Command")
      trigger(new AppIn(new Command(1)) -> appPort)
    }
  }

  appPort uponEvent {
    case AppOut(src:KAddress, Command(id:Int)) => handle {
      logger.info("Got Command from: " + src + " with id: " + id)
    }
  }

}

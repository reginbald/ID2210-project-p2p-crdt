package se.kth.app.sim.logoot

import java.util

import se.kth.app.sim.ScenarioSetup
import se.kth.sim.compatibility.SimNodeIdExtractor
import se.kth.system.HostMngrComp
import se.sics.kompics.network.Address
import se.sics.kompics.simulator.SimulationScenario
import se.sics.kompics.simulator.adaptor.{Operation, Operation1}
import se.sics.kompics.simulator.adaptor.distributions.extra.BasicIntSequentialDistribution
import se.sics.kompics.simulator.events.system.{KillNodeEvent, SetupEvent, StartNodeEvent}
import se.sics.kompics.simulator.network.identifier.IdentifierExtractor
import se.sics.kompics.sl.Init
import se.sics.ktoolbox.omngr.bootstrap.BootstrapServerComp
import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 21/05/2017.
  */
object LogootTestScenarioGen {
  val systemSetupOp = new Operation[SetupEvent]() {
    override def generate = new SetupEvent() {
      override def getIdentifierExtractor: IdentifierExtractor = new SimNodeIdExtractor
    }
  }

  val startBootstrapServerOp = new Operation[StartNodeEvent]() {
    override def generate = new StartNodeEvent() {
      val selfAdr: KAddress = ScenarioSetup.bootstrapServer

      override def getNodeAddress: Address = selfAdr

      override def getComponentDefinition: Class[BootstrapServerComp] = classOf[BootstrapServerComp]

      override def getComponentInit: BootstrapServerComp.Init = new BootstrapServerComp.Init(selfAdr)
    }
  }

  val startNodeOp = new Operation1[StartNodeEvent, Integer]() {
    override def generate(nodeId: Integer) : StartNodeEvent = new StartNodeEvent() {
      val selfAdr: KAddress = ScenarioSetup.getNodeAdr("193.0.0." + nodeId, nodeId)

      override def getNodeAddress: Address = selfAdr

      override def getComponentDefinition: Class[HostMngrComp] = classOf[HostMngrComp]

      override def getComponentInit: Init[HostMngrComp] = new Init(
        selfAdr,
        ScenarioSetup.bootstrapServer,
        ScenarioSetup.croupierOId
      )

      override def initConfigUpdate: util.HashMap[String, Object] = {
        val nodeConfig = new java.util.HashMap[String, Object]
        nodeConfig.put("system.id", nodeId)
        nodeConfig.put("system.seed", long2Long(ScenarioSetup.getNodeSeed(nodeId)))
        nodeConfig.put("system.port", int2Integer(ScenarioSetup.appPort))
        nodeConfig
      }
    }
  }

  val killOp:Operation1[KillNodeEvent, Integer] = new Operation1[KillNodeEvent, Integer]() {

    override def generate(nodeId: Integer): KillNodeEvent = new KillNodeEvent() {
      val selfAdr: KAddress = ScenarioSetup.getNodeAdr("193.0.0." + nodeId, nodeId)

      override def getNodeAddress: Address = selfAdr

      override def toString: String = "Kill<" + selfAdr.toString + ">"

    }
  }

  def simpleBoot: SimulationScenario = {
    val scenario: SimulationScenario = new SimulationScenario() {
      val systemSetup = new StochasticProcess() {
        {
          eventInterArrivalTime(constant(1000))
          raise(1, systemSetupOp)
        }
      }

      val startBootstrapServer = new StochasticProcess() {
        {
          eventInterArrivalTime(constant(1000))
          raise(1, startBootstrapServerOp)
        }
      }

      val startPeers = new StochasticProcess() {
        {
          eventInterArrivalTime(uniform(1000, 1100))
          raise(5, startNodeOp, new BasicIntSequentialDistribution(1))
        }
      }

      systemSetup.start()
      startBootstrapServer.startAfterTerminationOf(1000, systemSetup)
      startPeers.startAfterTerminationOf(1000, startBootstrapServer)
      terminateAfterTerminationOf(100*1000, startPeers)
    }
    scenario
  }

  def killOne: SimulationScenario = {
    val scenario: SimulationScenario = new SimulationScenario() {
      val systemSetup = new StochasticProcess() {
        {
          eventInterArrivalTime(constant(1000))
          raise(1, systemSetupOp)
        }
      }

      val startBootstrapServer = new StochasticProcess() {
        {
          eventInterArrivalTime(constant(1000))
          raise(1, startBootstrapServerOp)
        }
      }

      val startPeers = new StochasticProcess() {
        {
          eventInterArrivalTime(uniform(1000, 1100))
          raise(5, startNodeOp, new BasicIntSequentialDistribution(1))
        }
      }

      val killer = new StochasticProcess(){
        eventInterArrivalTime(constant(0))
        raise(1, killOp, new BasicIntSequentialDistribution(1))
      }

      systemSetup.start()
      startBootstrapServer.startAfterTerminationOf(1000, systemSetup)
      startPeers.startAfterTerminationOf(1000, startBootstrapServer)
      killer.startAfterTerminationOf(1000, startPeers)
      terminateAfterTerminationOf(100*1000, startPeers)
    }
    scenario
  }
}
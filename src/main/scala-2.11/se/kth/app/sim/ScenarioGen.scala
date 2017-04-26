package se.kth.app.sim

import java.util.HashMap
import java.util.Map
import se.kth.sim.compatibility.SimNodeIdExtractor
import se.kth.system.HostMngrComp
import se.sics.kompics.network.Address
import se.sics.kompics.simulator.SimulationScenario
import se.sics.kompics.simulator.adaptor.Operation
import se.sics.kompics.simulator.adaptor.Operation1
import se.sics.kompics.simulator.adaptor.distributions.extra.BasicIntSequentialDistribution
import se.sics.kompics.simulator.events.system.SetupEvent
import se.sics.kompics.simulator.events.system.StartNodeEvent
import se.sics.kompics.simulator.network.identifier.IdentifierExtractor
import se.sics.ktoolbox.omngr.bootstrap.BootstrapServerComp
import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 26/04/2017.
  */
class ScenarioGen {
  private[sim] val systemSetupOp = new Operation[SetupEvent]() {
    def generate = new SetupEvent() {
      override def getIdentifierExtractor: IdentifierExtractor = return new SimNodeIdExtractor
    }
  }
  private[sim] val startBootstrapServerOp = new Operation[StartNodeEvent]() {
    def generate = new StartNodeEvent() {
      private[sim] var selfAdr = null
      def getNodeAddress: Address
      =
        return selfAdr
      def getComponentDefinition: Class[_]
      =
        return classOf[BootstrapServerComp]
      def getComponentInit: BootstrapServerComp.Init
      =
        return new BootstrapServerComp.Init(selfAdr)
    }
  }
  private[sim] val startNodeOp = new Operation1[StartNodeEvent, Integer]() {
    def generate(nodeId: Integer) = new StartNodeEvent() {
      private[sim] var selfAdr = null
      def getNodeAddress: Address
      =
        return selfAdr
      def getComponentDefinition: Class[_]
      =
        return classOf[HostMngrComp]
      def getComponentInit: HostMngrComp.Init
      =
        return new HostMngrComp.Init(selfAdr, ScenarioSetup.bootstrapServer, ScenarioSetup.croupierOId)
      override

      def initConfigUpdate: util.Map[String, AnyRef] = {
        val nodeConfig = new util.HashMap[String, AnyRef]
        nodeConfig.put("system.id", nodeId)
        nodeConfig.put("system.seed", ScenarioSetup.getNodeSeed(nodeId))
        nodeConfig.put("system.port", ScenarioSetup.appPort)
        nodeConfig
      }
    }
  }

  def simpleBoot: SimulationScenario = {
    val scen: SimulationScenario = new SimulationScenario() {}
    return scen
  }
}

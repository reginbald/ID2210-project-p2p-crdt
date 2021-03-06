package se.kth.app.sim

import java.net.InetAddress
import java.net.UnknownHostException

import se.kth.system.SystemSetup
import se.sics.ktoolbox.util.identifiable.BasicBuilders
import se.sics.ktoolbox.util.identifiable.BasicIdentifiers
import se.sics.ktoolbox.util.identifiable.overlay.OverlayId
import se.sics.ktoolbox.util.network.KAddress
import se.sics.ktoolbox.util.network.basic.BasicAddress
import se.sics.ktoolbox.util.network.nat.NatAwareAddressImpl

/**
  * Created by reginbald on 26/04/2017.
  */
object ScenarioSetup {

  val scenarioSeed: Long = 1234
  val appPort: Int = 12345
  var bootstrapServer: KAddress = null
  var croupierOId: OverlayId = null

  {
    croupierOId = SystemSetup.setup()
    val bootstrapId = BasicIdentifiers.nodeId(new BasicBuilders.IntBuilder(0))
    try {
      bootstrapServer = NatAwareAddressImpl.open(new BasicAddress(InetAddress.getByName("193.0.0.1"), appPort, bootstrapId));
    }
    catch {
      case ex: UnknownHostException => throw new RuntimeException(ex)
    }
  }

  def getNodeAdr(nodeIp: String, baseNodeId: Int): KAddress = {
    try {
      val nodeId = BasicIdentifiers.nodeId(new BasicBuilders.IntBuilder(baseNodeId))
      NatAwareAddressImpl.open(new BasicAddress(InetAddress.getByName(nodeIp), appPort, nodeId))
    } catch {
      case ex: UnknownHostException => {
        throw new RuntimeException(ex)
      }
    }
  }

  def getNodeSeed(nodeId: Int): Long = scenarioSeed + nodeId
}

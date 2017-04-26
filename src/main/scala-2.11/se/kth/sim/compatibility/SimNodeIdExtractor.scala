package se.kth.sim.compatibility

import se.sics.kompics.network.Address
import se.sics.kompics.simulator.network.identifier.Identifier
import se.sics.kompics.simulator.network.identifier.IdentifierExtractor
import se.sics.ktoolbox.util.identifiable.basic.IntId
import se.sics.ktoolbox.util.network.KAddress

class SimNodeIdExtractor extends IdentifierExtractor{

  override def extract(adr: Address): Identifier = {
    val usedAdr: KAddress = adr.asInstanceOf[KAddress]
    val nodeId: Int = usedAdr.getId.asInstanceOf[IntId].id
    new SimNodeIdentifier(nodeId)
  }
}
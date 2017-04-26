package se.kth.sim.compatibility

import se.sics.kompics.simulator.network.identifier.Identifier

/**
  * Created by reginbald on 26/04/2017.
  */
class SimNodeIdentifier extends Identifier{
  var nodeId = 0

  def this(nodeId: Int) {
    this()
    this.nodeId = nodeId
  }

  def partition(nrPartitions: Int): Int = nodeId % nrPartitions

  override def hashCode: Int = {
    var hash = 7
    hash = 97 * hash + this.nodeId
    hash
  }

  override def equals(obj: Any): Boolean = {
    if (obj == null) return false
    if (getClass ne obj.getClass) return false
    val other = obj.asInstanceOf[SimNodeIdentifier]
    if (this.nodeId != other.nodeId) return false
    true
  }
}

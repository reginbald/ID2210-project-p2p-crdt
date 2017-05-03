package se.kth.app.crdt.gset

class GSet {
  val gSet = scala.collection.mutable.Set[String]()

  def add(message: String): Unit = {
    gSet += message
  }
  def lookup (element: String) : Boolean = {
    gSet.contains(element)
  }
}

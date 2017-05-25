package se.kth.app.logoot

import scala.collection.mutable.ListBuffer

/**
  * Created by reginbald on 20/05/2017.
  */
class IdentifierTable {

  class ObjectArrayTools[T <: AnyRef](a: Array[T]) {
    def binarySearch(key: T) = {
      java.util.Arrays.binarySearch(a.asInstanceOf[Array[AnyRef]],key)
    }
  }
  implicit def anyrefarray_tools[T <: AnyRef](a: Array[T]): ObjectArrayTools[T] = new ObjectArrayTools(a)


  private var table:ListBuffer[LineId] = new ListBuffer[LineId]

  table.insert(0, new LineId(ListBuffer[Position](new Position(0, null, null))))
  table.insert(1, new LineId(ListBuffer[Position](new Position(99, null, null))))

  def binarySearch(key: LineId): Int = {
    var out = table.toArray.binarySearch(key)
    out
  }

  def getBounds(index:Int): (LineId, LineId) = {
    if (index >= table.size - 1)
      return (getId(table.size - 2), getId(table.size - 1))
    else if (index <= 0)
      return (getId(0), getId(1))
    else
      return (getId(index - 1), getId(index + 1))
  }

  def contains(id: LineId): Boolean ={
    table.contains(id)
  }

  def insert(index: Int, id: LineId) = {
    table.insert(index, id)
  }

  def remove(index: Int, id: LineId) = {
    table.remove(index)
  }

  def getId(index: Int): LineId = {
    table(index)
  }
}

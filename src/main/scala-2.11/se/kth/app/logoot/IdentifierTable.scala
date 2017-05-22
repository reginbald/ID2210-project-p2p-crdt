package se.kth.app.logoot

import scala.collection.mutable.ListBuffer

/**
  * Created by reginbald on 20/05/2017.
  */
class IdentifierTable {
  private var table:ListBuffer[LineId] = new ListBuffer[LineId]

  table.insert(0, new LineId(ListBuffer[Position](new Position(0, null, null))))
  table.insert(1, new LineId(ListBuffer[Position](new Position(99, null, null))))

  def binarySearch(key: LineId): Int = {
    var out: Int = -1
    var lo: Int = 0
    var hi: Int = table.size - 1
    var mid:Int = 0
    while (lo <= hi) {
      mid = lo + (hi - lo) / 2
      //if (key.lessThan(table(mid))) {
      if(key.compare(table(mid)) < 0) {
        hi = mid - 1
      }
      //else if (table(mid).lessThan(key)) {
      else if(key.compare(table(mid)) > 0) {
        lo = mid + 1
      }
      else {
        return mid
      }
    }
    if (out == -1 ) out = -1 - mid
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

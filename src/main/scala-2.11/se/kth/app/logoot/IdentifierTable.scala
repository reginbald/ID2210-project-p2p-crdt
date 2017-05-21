package se.kth.app.logoot

import scala.collection.mutable.ListBuffer

/**
  * Created by reginbald on 20/05/2017.
  */
class IdentifierTable {
  private var table:ListBuffer[LineId] = new ListBuffer[LineId]

  table.insert(0, new LineId(ListBuffer[Position](new Position(0, null, 0))))
  table.insert(1, new LineId(ListBuffer[Position](new Position(100, null, 0))))

  def binarySearch(key: LineId): Int = {
    var out: Int = -1
    var lo: Int = 0
    var hi: Int = table.size - 1
    var mid:Int = 0
    while (lo <= hi) {
      // Key is in a[lo..hi] or not present.
      mid = lo + (hi - lo) / 2
      if (key.lessThan(table(mid))) {
        hi = mid - 1
      }
      else if (table(mid).lessThan(key)) {
        lo = mid + 1
      }
      else {
        out = mid
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

  //def getUpperLineId(index: Int): LineId ={
  //  var out: LineId = null
  //  if (table.size - 1 < index || table.size - 1 < index + 1) out = getId(table.size - 1)
  //  else out = getId(index + 1)
  //  out
  //}
//
  //def getLowerLineId(index: Int): LineId ={
  //  var out: LineId = null
  //  if (0 > index - 1) out = getId(0)
  //  else if (index - 1 == table.size) getId(index - 2)
  //  else out = getId(index - 1)
  //  out
  //}

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

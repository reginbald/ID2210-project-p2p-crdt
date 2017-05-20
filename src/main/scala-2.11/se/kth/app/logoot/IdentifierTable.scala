package se.kth.app.logoot

import scala.collection.mutable.ListBuffer

/**
  * Created by reginbald on 20/05/2017.
  */
class IdentifierTable {
  private var table:ListBuffer[LineId] = new ListBuffer[LineId]

  def binarySearch(key: LineId): Int = {
    var out: Int = -1
    var lo: Int = 0
    var hi: Int = table.size - 1
    while (lo <= hi) {
      // Key is in a[lo..hi] or not present.
      val mid = lo + (hi - lo) / 2
      if (key.lessThan(table(mid))) hi = mid - 1
      else if (table(mid).lessThan(key)) lo = mid + 1
      else out = mid
    }
    out
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

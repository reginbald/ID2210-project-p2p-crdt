package se.kth.app.logoot

import scala.collection.mutable

/**
  * Created by reginbald on 17/05/2017.
  */
class LineId(val positions: mutable.ListBuffer[Position]) {
  def lessThan(that: LineId): Boolean = {
    var out:Boolean = false
    var same:Int = 0

    for (i <- positions.indices){
      if (i >= that.positions.size) return out
      else if (positions(i).digit < that.positions(i).digit) {
        out = true
        return true
      }
      else if (positions(i).digit == that.positions(i).digit && positions(i).siteId == null && that.positions(i).siteId != null){
        out = true
        return true
      }
      else if (positions(i).digit == that.positions(i).digit && positions(i).siteId == null && that.positions(i).siteId == null &&
        positions(i).clock < that.positions(i).clock){
        out = true
        return true
      }
      else if (positions(i).digit == that.positions(i).digit &&
        positions(i).siteId == null && that.positions(i).siteId == null &&
        positions(i).clock == that.positions(i).clock) {
        same += 1
      }
      else if (positions(i).digit == that.positions(i).digit && positions(i).siteId == null && that.positions(i).siteId == null){
        out = false
        //continue
      }
      else if (positions(i).digit == that.positions(i).digit && positions(i).siteId != null && that.positions(i).siteId == null){
        out = false
        //continue
      }
      else if (positions(i).digit == that.positions(i).digit &&
        positions(i).siteId.getId.toString.toInt < that.positions(i).siteId.getId.toString.toInt ){
        out = true
        return true
      }
      else if (positions(i).digit == that.positions(i).digit &&
        positions(i).siteId.getId.toString.toInt  == that.positions(i).siteId.getId.toString.toInt  &&
        positions(i).clock < that.positions(i).clock) {
        out = true
        return true
      } else if (positions(i).digit == that.positions(i).digit &&
        positions(i).siteId.getId.toString.toInt == that.positions(i).siteId.getId.toString.toInt &&
        positions(i).clock == that.positions(i).clock) {
        same += 1
      }
    }
    if (same == positions.size && that.positions.size > positions.size) out = true

    out
  }

}

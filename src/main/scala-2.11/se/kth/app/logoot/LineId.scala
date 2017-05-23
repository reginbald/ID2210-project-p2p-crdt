package se.kth.app.logoot

import scala.collection.mutable

/**
  * Created by reginbald on 17/05/2017.
  */
class LineId(val positions: mutable.ListBuffer[Position]) extends Ordered[LineId]{

  override def compare(that: LineId): Int = {
    for(i <- this.positions.indices){
      if (i >= that.positions.size) return 1
      if (this.positions(i).digit == 99 && this.positions(i).siteId == null && positions(i).clock == null) return 1
      if (that.positions(i).digit == 99 && that.positions(i).siteId == null && that.positions(i).clock == null) return -1
      else {
        val tmp = this.positions(i).compare(that.positions(i))
        if (tmp != 0) return tmp
      }
    }
    0
  }

  /*def lessThan(that: LineId): Boolean = {
    var out:Boolean = false
    var same:Int = 0

    for (i <- positions.indices){
      if (that.positions(i).digit == 99 && that.positions(i).siteId == null && that.positions(i).clock == null) return true
      if (positions(i).digit == 99 && positions(i).siteId == null && positions(i).clock == null) return


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
        positions(i).clock == null && that.positions(i).clock != null){
        out = true
        return true
      }
      else if (positions(i).digit == that.positions(i).digit && positions(i).siteId == null && that.positions(i).siteId == null &&
        positions(i).clock == null && that.positions(i).clock == null){
        same += 1
      }
      else if (positions(i).digit == that.positions(i).digit && positions(i).siteId == null && that.positions(i).siteId == null &&
        positions(i).clock != null && that.positions(i).clock == null){
        out = false
        //continue
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
  }*/
}

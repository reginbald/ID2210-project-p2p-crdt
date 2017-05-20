package se.kth.app.logoot

import scala.collection.mutable
import scala.util.control.Breaks._

/**
  * Created by reginbald on 17/05/2017.
  */
class LineId(val positions: mutable.ListBuffer[Position]) {
  def lessThan(that: LineId): Boolean = {
    var out:Boolean = false;

    for (i <- 0 to positions.size -1){
      if (positions(i).digit < that.positions(i).digit) {
        out = true
        break
      }
      else if (positions(i).digit == that.positions(i).digit &&
        positions(i).siteId.getId.asInstanceOf[Int] < that.positions(i).siteId.getId.asInstanceOf[Int]){
        out = true
        break
      }
      else if (positions(i).digit == that.positions(i).digit &&
        positions(i).siteId.getId.asInstanceOf[Int] == that.positions(i).siteId.getId.asInstanceOf[Int] &&
        positions(i).clock < that.positions(i).clock) {
        out = true
        break
      }
    }
    out
  }

}

package se.kth.app.logoot

import scala.collection.mutable

/**
  * Created by reginbald on 17/05/2017.
  */
class LineId(val positions: mutable.ListBuffer[Position]) extends Ordered[LineId]{

  override def compare(that: LineId): Int = {
    if(this < that) return 1
    if(that < this) return -1
    0
  }

  override def <(that: LineId): Boolean = {
    for(i <- this.positions.indices){
      if (i >= that.positions.size) return true
      if(this.positions(i).compareTo(that.positions(i)) == -1) return true
      if(this.positions(i).compareTo(that.positions(i)) == 1) return false
    }
    false
  }
}

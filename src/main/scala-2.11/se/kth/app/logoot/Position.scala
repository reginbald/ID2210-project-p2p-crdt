package se.kth.app.logoot

import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 17/05/2017.
  */
class Position(val digit: Int, val siteId: KAddress, val clock: Integer) extends Ordered[Position] {
  override def compare(that: Position): Int = {
    if (this.digit != that.digit) this.digit - that.digit
    else if (this.siteId != that.siteId) {
      if(this.siteId == null) -1
      else if (that.siteId == null) 1
      else this.siteId.getId.toString.toInt - that.siteId.getId.toString.toInt
    } else if (this.clock != that.clock) {
      if(this.clock == null) -1
      else if (that.clock == null) 1
      else this.clock - that.clock
    }
    else 0
  }
}

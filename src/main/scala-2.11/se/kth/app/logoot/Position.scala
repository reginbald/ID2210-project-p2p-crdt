package se.kth.app.logoot

import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 17/05/2017.
  */
class Position(val digit: Int, val siteId: KAddress, val clock: Integer) extends Ordered[Position] {
  override def compare(that: Position): Int = {
    if (this < that) return 1
    if (that < this) return -1
    return 0
  }

  override def <(that: Position): Boolean = {
    if(this.digit < that.digit) return true
    if(this.digit == that.digit && that.siteId != null && this.siteId.getId.compareTo(that.siteId.getId) == -1) return true
    if(this.digit == that.digit && that.siteId != null && this.siteId.getId.compareTo(that.siteId.getId) == 0
      && that.clock != null && this.clock < that.clock) return true
    false
  }

}

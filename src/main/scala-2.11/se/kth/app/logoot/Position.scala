package se.kth.app.logoot

import se.sics.ktoolbox.util.network.KAddress

import scala.collection.mutable

/**
  * Created by reginbald on 17/05/2017.
  */
class Position(val digit: Int, val siteId: KAddress, val clock: mutable.Map[KAddress, Int]) {
  def MAX: (Int, KAddress, Map[KAddress, Int] )= {
    (Int.MaxValue, null, null)
  }

  def MIN: (Int, KAddress, Map[KAddress, Int] )= {
    (0, null, null)
  }
}

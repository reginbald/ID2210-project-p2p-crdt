package se.kth.app.logoot

import se.sics.ktoolbox.util.network.KAddress

import scala.collection.mutable

/**
  * Created by reginbald on 17/05/2017.
  */
class Position(val digit: Int, val siteId: KAddress, val clock: Int/*mutable.Map[KAddress, Int]*/) {
  def MAX: (Int, KAddress, Int/*Map[KAddress, Int] */)= {
    (Int.MaxValue, null, 0)
  }

  def MIN: (Int, KAddress, Int/*Map[KAddress, Int]*/ )= {
    (0, null, 0)
  }
}

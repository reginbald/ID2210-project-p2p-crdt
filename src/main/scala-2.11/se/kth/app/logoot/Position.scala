package se.kth.app.logoot

import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 17/05/2017.
  */
class Position(i: Int, s: KAddress, c: Map[KAddress, Int]) {
  def MAX: (Int, KAddress, Map[KAddress, Int] )= {
    (Int.MaxValue, null, null)
  }

  def MIN: (Int, KAddress, Map[KAddress, Int] )= {
    (0, null, null)
  }
}

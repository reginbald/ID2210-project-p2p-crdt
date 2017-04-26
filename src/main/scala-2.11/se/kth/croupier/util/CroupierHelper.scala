package se.kth.croupier.util

import se.sics.ktoolbox.croupier.event.CroupierSample
import se.sics.ktoolbox.util.network.KAddress

/**
  * Created by reginbald on 26/04/2017.
  */
class CroupierHelper {
  def getSample(sample: CroupierSample[Boolean]): util.List[KAddress] = {
    val s = new util.LinkedList[KAddress]
    import scala.collection.JavaConversions._
    for (e <- sample.publicSample.values) {
      s.add(e.getSource)
    }
    s
  }
}

package se.kth.croupier.util

import se.sics.ktoolbox.croupier.event.CroupierSample
import se.sics.ktoolbox.util.network.KAddress
import scala.collection.JavaConversions._

/**
  * Created by reginbald on 26/04/2017.
  */
class CroupierHelper {
  def getSample(sample: CroupierSample[_]): Iterable[KAddress] = {

    sample.publicSample.values().map{ x => x.getSource()}
  }
}

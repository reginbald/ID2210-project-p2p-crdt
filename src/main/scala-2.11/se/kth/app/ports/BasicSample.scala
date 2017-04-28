package se.kth.app.ports

import se.kth.app.events.Sample
import se.sics.kompics.sl._

/**
  * Created by reginbald on 28/04/2017.
  */
class BasicSample extends Port {
  request[Sample]
}

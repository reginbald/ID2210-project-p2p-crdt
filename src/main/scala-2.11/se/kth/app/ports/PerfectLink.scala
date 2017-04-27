package se.kth.app.ports

import se.kth.app.events.{PL_Deliver, PL_Send}
import se.sics.kompics.sl.Port

/**
  * Created by reginbald on 27/04/2017.
  */
class PerfectLink extends Port {
  request[PL_Send]
  indication[PL_Deliver]
}

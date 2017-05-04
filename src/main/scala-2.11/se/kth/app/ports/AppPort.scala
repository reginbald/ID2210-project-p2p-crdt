package se.kth.app.ports

import se.kth.app.events.{AppIn, AppOut}
import se.sics.kompics.sl.Port

/**
  * Created by reginbald on 04/05/2017.
  */
class AppPort extends Port{
  indication[AppOut]
  request[AppIn]
}

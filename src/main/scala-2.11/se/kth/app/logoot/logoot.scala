package se.kth.app.logoot

import com.typesafe.scalalogging.StrictLogging
import se.sics.kompics.sl.ComponentDefinition

class logoot extends ComponentDefinition with StrictLogging {

  var identifierTable : List[(Int, Int, Int)] = List.empty
  

}

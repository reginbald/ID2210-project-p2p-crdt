package se.kth.app.logoot

import java.util.UUID

/**
  * Created by reginbald on 20/05/2017.
  */
trait Operation {
  var id: LineId
}
case class Insert(var id: LineId, content:String) extends Operation
case class Remove(var id: LineId, content:String) extends Operation
case class Redo(var id: LineId, patchId:UUID) extends Operation
case class Undo(var id: LineId, patchId:UUID) extends Operation

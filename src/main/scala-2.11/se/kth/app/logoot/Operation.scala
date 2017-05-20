package se.kth.app.logoot

import java.util.UUID

/**
  * Created by reginbald on 20/05/2017.
  */
class Operation {}
case class Insert(id: LineId, content:String) extends Operation
case class Remove(id: LineId, content:String) extends Operation
case class Redo(patchId:UUID) extends Operation
case class Undo(patchId:UUID) extends Operation

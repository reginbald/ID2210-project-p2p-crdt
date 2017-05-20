package se.kth.app.logoot

/**
  * Created by reginbald on 20/05/2017.
  */
class Operation {}
case class Insert(id: LineId, content:String) extends Operation
case class Remove(id: LineId, content:String) extends Operation

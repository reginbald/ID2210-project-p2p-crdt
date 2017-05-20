package se.kth.app.logoot

/**
  * Created by reginbald on 20/05/2017.
  */
trait Operation {
  var id: LineId
}
case class Insert(var id: LineId, content:String) extends Operation
case class Remove(var id: LineId, content:String) extends Operation

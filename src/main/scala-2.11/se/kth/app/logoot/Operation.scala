package se.kth.app.logoot

/**
  * Created by reginbald on 20/05/2017.
  */
trait Operation {
  var id: LineId
  var content: String
}
case class Insert(var id: LineId, var content:String) extends Operation
case class Remove(var id: LineId, var content:String) extends Operation

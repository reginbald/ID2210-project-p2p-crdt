package se.kth.app.logoot

/**
  * Created by reginbald on 20/05/2017.
  */
class Operation {
}
case class Insert(id: Int, content:String) extends Operation
case class Remove(id:Int, content:String) extends Operation

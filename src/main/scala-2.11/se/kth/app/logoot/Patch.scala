package se.kth.app.logoot

import java.util.UUID

import scala.collection.mutable.ListBuffer

/**
  * Created by reginbald on 20/05/2017.
  */
class Patch(val id: UUID, var degree: Int, val operations: ListBuffer[Operation], val N: Int) {
  //override def hashCode(): Int = {
  //  id.hashCode() //+ degree + N + operations.size
  //}
}

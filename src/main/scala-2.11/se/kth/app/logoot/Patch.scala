package se.kth.app.logoot

import java.util.UUID

import scala.collection.mutable.ListBuffer

/**
  * Created by reginbald on 20/05/2017.
  */
case class Patch(id: UUID, var degree: Int, var operations: ListBuffer[Operation]) {}

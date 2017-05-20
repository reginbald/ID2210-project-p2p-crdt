package se.kth.app.logoot

import java.util.UUID

import scala.collection.mutable.Map

/**
  * Created by reginbald on 20/05/2017.
  */
class HistoryBuffer {
  var table: Map[UUID, Patch] = Map[UUID, Patch]()

  def add(patch:Patch): Unit ={
    table(patch.id) = patch
  }

  def get(id: UUID): Option[Patch] ={
    table.get(id)
  }
}

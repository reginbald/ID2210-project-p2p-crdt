package se.kth.app.logoot

import java.util.UUID

/**
  * Created by reginbald on 20/05/2017.
  */
class HistoryBuffer {
  private val table: scala.collection.mutable.HashMap[UUID, Patch] = new scala.collection.mutable.HashMap[UUID, Patch]

  def add(patch:Patch): Unit ={
    table(patch.id) = new Patch(patch.id, patch.degree, patch.operations, patch.N)
  }

  def get(id: UUID): Option[Patch] ={
    table.get(id)
  }
}

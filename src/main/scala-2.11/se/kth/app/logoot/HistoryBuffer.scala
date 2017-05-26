package se.kth.app.logoot

import java.util.UUID

import scala.collection.mutable

/**
  * Created by reginbald on 20/05/2017.
  */
class HistoryBuffer {
  private val table: mutable.HashMap[UUID, Patch] = new mutable.HashMap[UUID, Patch]().empty

  def add(patch:Patch): Unit ={
    table(patch.id) = patch
  }

  def get(id: UUID): Option[Patch] ={
    table.get(id)
  }
}

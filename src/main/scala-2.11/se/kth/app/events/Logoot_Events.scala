package se.kth.app.events

import java.util.UUID

import se.kth.app.logoot.Patch
import se.sics.kompics.KompicsEvent

/**
  * Created by reginbald on 20/05/2017.
  */

@SerialVersionUID(1L)
case class Logoot_Do(line:Int, op: Patch) extends KompicsEvent with Serializable

@SerialVersionUID(2L)
case class Logoot_Done(patch: Patch) extends KompicsEvent with Serializable

@SerialVersionUID(3L)
case class Logoot_Doc(doc: String) extends KompicsEvent with Serializable

@SerialVersionUID(4L)
case class Logoot_Undo(patchId: UUID) extends KompicsEvent with Serializable {
  override def hashCode(): Int = {
    patchId.hashCode() + 1
  }
}

@SerialVersionUID(5L)
case class Logoot_Redo(patchId: UUID) extends KompicsEvent with Serializable {
  override def hashCode(): Int = {
    patchId.hashCode() + 2
  }
}

case class Logoot_Patch(patch: Patch) extends KompicsEvent
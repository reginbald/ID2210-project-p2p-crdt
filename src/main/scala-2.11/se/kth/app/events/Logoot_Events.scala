package se.kth.app.events

import java.util.UUID

import se.kth.app.logoot.{Patch}
import se.sics.kompics.KompicsEvent

/**
  * Created by reginbald on 20/05/2017.
  */
case class Logoot_Do(line:Int, op: Patch) extends KompicsEvent

case class Logoot_Doc(doc: String) extends KompicsEvent

case class Logoot_Undo(patchId: UUID) extends KompicsEvent

case class Logoot_Redo(patchId: UUID) extends KompicsEvent

case class Logoot_Patch(patch: Patch) extends KompicsEvent
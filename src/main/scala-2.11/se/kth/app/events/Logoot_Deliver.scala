package se.kth.app.events

import java.util.UUID

import se.sics.kompics.KompicsEvent

/**
  * Created by reginbald on 20/05/2017.
  */
case class Logoot_Deliver(message:KompicsEvent) extends KompicsEvent {
}

case class Logoot_Patch(patch: Logoot_Patch) extends KompicsEvent {
}

case class Logoot_Undo(patchId: UUID) extends KompicsEvent {
}

case class Logoot_Redo(patchId: UUID) extends KompicsEvent {
}

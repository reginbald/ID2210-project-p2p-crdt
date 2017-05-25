package se.kth.app.ports

import se.kth.app.events._
import se.sics.kompics.sl.Port

class LogootPort extends Port {
  request[Logoot_Do] // in
  request[Logoot_Undo] // in
  request[Logoot_Redo] // in
  indication[Logoot_Doc] // out
  indication[Logoot_Done] // out
  indication[Logoot_Patch] // out
}
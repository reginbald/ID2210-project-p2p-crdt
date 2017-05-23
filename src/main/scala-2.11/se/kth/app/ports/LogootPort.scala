package se.kth.app.ports

import se.kth.app.events.{Logoot_Do, Logoot_Doc, Logoot_Done}
import se.sics.kompics.sl.Port

class LogootPort extends Port {
  request[Logoot_Do] // in
  indication[Logoot_Doc] // out
  indication[Logoot_Done] // out
}
package se.kth.app.ports

import se.kth.app.events.{Logoot_Do, Logoot_Doc}
import se.sics.kompics.sl.Port

class LogootPort extends Port {
  request[Logoot_Do] // in
  indication[Logoot_Doc] // out
}
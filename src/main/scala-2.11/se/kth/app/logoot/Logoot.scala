package se.kth.app.logoot

import java.util.UUID

import com.typesafe.scalalogging.StrictLogging
import se.kth.app.events._
import se.kth.app.ports.{CausalOrderReliableBroadcast, LogootPort}
import se.sics.kompics.Start
import se.sics.kompics.sl.{ComponentDefinition, Init, NegativePort, PositivePort, handle}
import se.sics.ktoolbox.util.network.KAddress

import scala.collection.mutable.ListBuffer
import scala.collection.mutable


class Logoot(init: Init[Logoot]) extends ComponentDefinition with StrictLogging {
  val nwcb: PositivePort[CausalOrderReliableBroadcast] = requires[CausalOrderReliableBroadcast]
  val logootPort: NegativePort[LogootPort] = provides[LogootPort]

  val self: KAddress = init match { case Init(s: KAddress) => s }

  var clock: Int = 0
  var identifierTable = new IdentifierTable
  var cemetery = new Cemetery
  var document = new Document
  var histBuff = new HistoryBuffer

  var currentPatchID: UUID = _

  /* Logoot events */
  ctrl uponEvent {
    case _:Start => handle {
      logger.info("--- Logoot is starting ---")
    }
  }

  logootPort uponEvent {
    case Logoot_Do(line: Int, patch:Patch) => handle {
      logger.info("logoot received patch from client")
      val (p:LineId, q:LineId)= identifierTable.getBounds(line)
      if (patch.N > 0){
        val ids:ListBuffer[LineId] = generateLineId(p, q, patch.N, 10, self)

        var counter: Int = 0
        for (i <- patch.operations.indices){
          patch.operations(i) match {
            case insert: Insert => insert.id = ids(counter); counter += 1
            case _ => // Do nothing
          }
        }
      }
      currentPatchID = patch.id
      trigger(Logoot_Patch(patch), logootPort)
      trigger(CORB_Broadcast(Logoot_Patch(patch)), nwcb)
    }
    case undo:Logoot_Undo => handle {
      logger.info("logoot received undo from client")
      trigger(CORB_Broadcast(undo), nwcb)
    }
    case redo:Logoot_Redo => handle {
      logger.info("logoot received redo from client")
      trigger(CORB_Broadcast(redo), nwcb)
    }
    case doc:Logoot_Doc => handle {
      logger.info("logoot received document request from client")
      trigger(Logoot_Doc(document.flatten()), logootPort)
    }
  }

  nwcb uponEvent {
    case CORB_Deliver(_:KAddress, Logoot_Patch(patch:Patch)) => handle {
      logger.info("logoot received patch")
      execute(patch)
      patch.degree = 1
      histBuff.add(patch)
      if(currentPatchID == patch.id) trigger(Logoot_Done(patch), logootPort)
    }
    case CORB_Deliver(_:KAddress, Logoot_Undo(patchId: UUID)) => handle {
      logger.info("logoot received undo")
      var patch: Patch = null
      histBuff.get(patchId) match {
        case Some(p) =>
          patch = p
          patch.degree -= 1
          if (patch.degree == 0){
            execute(inverse(patch))
          }
        case _ => logger.info("patch not found")
      }
      if(patch != null && currentPatchID == patchId) trigger(Logoot_Done(patch), logootPort)
    }
    case CORB_Deliver(_:KAddress, Logoot_Redo(patchId: UUID)) => handle {
      logger.info("logoot received redo")
      var patch:Patch = null
      histBuff.get(patchId) match {
        case Some(p) =>
          patch = p
          patch.degree += 1
          if (patch.degree == 1){
            execute(patch)
          }
        case _ => logger.info("patch not found")
      }
      if(patch != null && currentPatchID == patchId) trigger(Logoot_Done(patch), logootPort)
    }
  }

  def inverse(patch:Patch): Patch ={
    val out:Patch = new Patch(patch.id, patch.degree, new ListBuffer[Operation], patch.N)
    for (i <- patch.operations.indices){
      patch.operations(i) match {
        case insert:Insert => out.operations += Remove(insert.id, insert.content)
        case remove:Remove => out.operations += Insert(remove.id, remove.content)
      }
      out.operations
    }
    out
  }

  // Todo remove site will use self
  def generateLineId(p: LineId, q: LineId, N: Int, boundary: Int, site: KAddress): ListBuffer[LineId] = {
    logger.info("logoot generating line ids")
    var list:mutable.ListBuffer[LineId] = new mutable.ListBuffer[LineId]
    var index: Int = 0
    var interval: Int = 0
    while (interval < N){ // Finds a place for N identifiers
      index += 1
      interval = prefix1(q, index) - prefix1(p, index) - 1
    }
    var step = math.min(interval/N, boundary)
    var r = prefix2(p, index)
    for (_ <- 1 to N ){ // Constructs N identifiers
      var rand:ListBuffer[Int] = ListBuffer.empty[Int]
      rand += Random(1, step)
      list += constructId(r ++ rand, p, q, site)
      r += step
    }
    list
  }

  def prefix1(p: LineId, index: Int):Int = {
    var digits = ""
    for (i <- 0 until index){
      if (i < p.positions.size){
        digits += p.positions(i).digit
      } else {
        digits += "0"
      }
    }
    digits.toInt
  }

  def prefix2(p: LineId, index: Int):ListBuffer[Int] = {
    var digits: ListBuffer[Int] = new ListBuffer[Int]
    for (i <- 0 until index){
      if (i < p.positions.size){
        digits += p.positions(i).digit
      } else {
        digits += 0
      }
    }
    digits
  }

  def toBase10(digits: mutable.ListBuffer[Int]): Int ={
    //1.2.3 base 100 is 3*100^0 + 2*100^1 + 1*100^2 in base 10
    var out: Int = 0
    for(i <- (digits.size - 1) to 0 by -1){
      val veldi = (digits.size - 1) - i
      out += digits(i) * math.pow(100, veldi).toInt
    }
    out
  }

  def prefix(p: LineId, index: Int): mutable.ListBuffer[Int] = {
    var out: mutable.ListBuffer[Int] = new mutable.ListBuffer[Int]
    for (i <- 0 until index) {
      if (i >= p.positions.size){
        out += 0
      } else {
        out += p.positions(i).digit
      }
    }
    out
  }


  def Random(start: Int, end: Int): Int ={
    val rnd = new scala.util.Random
    start + rnd.nextInt( (end - start) + 1 )
  }


  // Todo remove site will use self
  def constructId(r: mutable.ListBuffer[Int], p: LineId, q: LineId, site: KAddress): LineId = {
    val id = new LineId(mutable.ListBuffer.empty)
    for( i <- r.indices){
      val d: Int = r(i)
      var s: KAddress = null
      var c:Integer = 0
      if(i < p.positions.size && d == p.positions(i).digit){
        s = p.positions(i).siteId
        c = p.positions(i).clock
      } else if (i < q.positions.size && d == q.positions(i).digit) {
        s = q.positions(i).siteId
        c = q.positions(i).clock
      } else {
        s = site
        c = clock
        clock += 1
      }
      id.positions += new Position(d, s, c)
    }
    id
  }

  def execute(patch: Patch): Unit = {
    logger.info("logoot executing patch")
    for(op <- patch.operations){
      op match {
        case in: Insert =>
          val degree: Int = cemetery.get(in.id) + 1
          if (degree == 1) {
            var position = identifierTable.binarySearch(in.id)
            if( position < 0) position = math.abs(position) - 1
            document.insert(position, in.content)
            identifierTable.insert(position,in.id)
          }
          else {
            cemetery.set(in.id, degree)
          }
        case del:Remove =>
          var degree = 0
          if(identifierTable.contains(del.id)){
            val position = identifierTable.binarySearch(del.id)
            document.remove(position,del.content)
            identifierTable.remove(position, del.id)
            degree = 0
          }
          else {
            degree = cemetery.get(del.id) - 1
          }
          cemetery.set(del.id, degree)
      }
    }
  }
}

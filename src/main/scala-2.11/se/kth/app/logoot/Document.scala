package se.kth.app.logoot

import scala.collection.mutable.ListBuffer

/**
  * Created by reginbald on 20/05/2017.
  */
class Document {
  private var doc:ListBuffer[String] = new ListBuffer[String]

  doc.insert(0, "")
  doc.insert(1, "")

  def insert(index: Int, content: String) = {
    doc.insert(index, content)
  }

  def remove(index: Int, content: String) = {
    doc.remove(index)
  }

  def flatten(): String ={
    var out:String = ""
    doc.foreach(x => out += x)
    out
  }
}

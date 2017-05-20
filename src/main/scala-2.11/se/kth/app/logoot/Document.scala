package se.kth.app.logoot

import scala.collection.mutable.ListBuffer

/**
  * Created by reginbald on 20/05/2017.
  */
class Document {
  private var doc:ListBuffer[String] = new ListBuffer[String]

  def insert(index: Int, content: String) = {
    doc.insert(index, content)
  }

  def remove(index: Int, content: String) = {
    doc.remove(index)
  }
}

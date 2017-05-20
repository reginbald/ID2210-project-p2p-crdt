package se.kth.app.logoot

/**
  * Created by reginbald on 20/05/2017.
  */
class Cemetery {
  var table: scala.collection.mutable.Map[LineId, Int] = scala.collection.mutable.Map[LineId, Int]()

  // cemetery.get(id): returns 0 if the id is not in the cemetery
  // otherwise it returns the visibility degree associated to this id
  def get(id: LineId): Int ={
    var out:Int = 0
    table.get(id) match {
      case Some(degree) => out = degree
      case None => out = 0
    }
    out
  }

  //cemetery.set(id, degree): inserts the pair (id, degree) in the cemetery.
  // If the id is already present, the old value is overwritten.
  // If degree = 0, the pair is removed from the cemetery.
  def set(id: LineId, degree: Int): Unit ={
    if (degree == 0) table.remove(id)
    table(id) = degree
  }
}

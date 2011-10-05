package su.msk.dunno.blame.prototypes

import su.msk.dunno.blame.field.FieldObject
import su.msk.dunno.scage.single.support.{Vec, ScageColor}
import su.msk.dunno.scage.screens.support.tracer.{Trace, State}

class Item(val name:String,
           val description:String,
           private val symbol:Int,
           private val color:ScageColor)
extends FieldObject with HaveStats {
  setStat("item")
  setStat("name", name)
  setStat("description", description)

  def getSymbol = symbol
  def getColor = color
  def isTransparent = true
  def isPassable = true

  def getState = stats
  def changeState(changer:Trace, s:State) = {
    if(s.contains("point")) point is s.getVec("point")
  }
}
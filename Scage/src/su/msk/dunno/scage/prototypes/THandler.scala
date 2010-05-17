package su.msk.dunno.scage.prototypes

import su.msk.dunno.scage.objects.Movable

trait THandler {
  def evolve(objects:List[Movable]):List[Movable] = {
    doAction()
    objects
  }
  def doAction():Unit = {}
  def exitSequence():Unit = {}

  def ::(o:THandler) = o :: List[THandler](this)
}
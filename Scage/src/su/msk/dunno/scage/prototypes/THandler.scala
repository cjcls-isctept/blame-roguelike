package su.msk.dunno.scage.prototypes

trait THandler {
  def doAction():Unit = {}
  def exitSequence():Unit = {}

  def ::(o:THandler) = o :: List[THandler](this)
}
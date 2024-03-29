package su.msk.dunno.blame.prototypes

import su.msk.dunno.blame.screens.Blamer._
import su.msk.dunno.scage.single.support.{ScageColor, Vec}
import su.msk.dunno.scage.screens.ScageScreen._
import su.msk.dunno.blame.support.TimeUpdater
import su.msk.dunno.blame.decisions.DoNothing

abstract class Npc(name:String, description:String, init_point:Vec, symbol:Int, color:ScageColor)
extends Living(name, description, init_point, symbol, color) {
  protected var last_decision:Decision = new DoNothing(this)
  action {
    if(isAlive) {
      if(TimeUpdater.time - lastActionTime >= last_decision.actionPeriod) {
        last_decision = livingAI
        TimeUpdater.addDecision(last_decision)
      }
    } else delActions(currentOperation)
  }

  def randomDir:Vec = Vec((math.random*3).toInt - 1, (math.random*3).toInt - 1)
  def livingAI:Decision
}

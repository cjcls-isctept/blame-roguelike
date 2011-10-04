package su.msk.dunno.blame.decisions

import su.msk.dunno.scage.single.support.Vec
import su.msk.dunno.scage.screens.support.tracer.State
import su.msk.dunno.blame.support.{BottomMessages, TimeUpdater}
import su.msk.dunno.scage.single.support.ScageColors._
import su.msk.dunno.blame.animations.BulletFlight
import su.msk.dunno.blame.field.FieldTracer
import su.msk.dunno.scage.single.support.messages.ScageMessage
import org.lwjgl.input.Keyboard
import su.msk.dunno.blame.prototypes.{Player, Living, Decision}

class Move(living:Living, val step:Vec) extends Decision(living) {
  def doAction = {
    val new_point = living.point + step
    FieldTracer.move2PointIfPassable(living, new_point)
    true
  }
}

class OpenDoor(living:Living) extends Decision(living) {
  def doAction = {
    FieldTracer.findVisibleObject(living.point, 1, obj => {
      obj.getState.contains("door") && obj.getState.contains("close")
    }) match {
      case Some(door) => {
        door.changeState(living, new State("door_open", living.stat("name")))
        true
      }
      case None => false
    }
  }
}

class CloseDoor(living:Living) extends Decision(living) {
  def doAction = {
    FieldTracer.findVisibleObject(living.point, 1, obj => {
      obj.getState.contains("door") && obj.getState.contains("open") &&
      !FieldTracer.objectsAtPoint(obj.point).exists(_.getState.contains("living"))
    }) match {
      case Some(door) => {
        door.changeState(living, new State("door_close", living.stat("name")))
        true
      }
      case None => false
    }
  }
}

class Shoot(living:Living, target_point:Vec = Vec(-1,-1)) extends Decision(living) {
  def doAction = {
    val target = if(target_point != Vec(-1, -1)) target_point else living.selectTarget(Keyboard.KEY_F)
    if(target != living.point) {
      BottomMessages.addPropMessage("decision.shoot", living.stat("name"))
      if(FieldTracer.isNearPlayer(living.point)) new BulletFlight(living.point, target, YELLOW)
      val kickback = (living.point - target).n
      val kickback_delta = Vec(if(math.abs(kickback.x) > 0.3) math.signum(kickback.x) else 0,
                               if(math.abs(kickback.y) > 0.3) math.signum(kickback.y) else 0)
      TimeUpdater.addDecision(new Move(living, kickback_delta))

      val damage = living.floatStat("damage")

      FieldTracer.objectsAtPoint(target).foreach(_.changeState(living, new State("damage", damage)))
      true
    } else false
  }
}

class DropItem(living:Living) extends Decision(living) {
  def doAction = {
    living.inventory.selectItem(ScageMessage.xml("decision.drop.selection")) match {
      case Some(item_to_drop) => {
        living.inventory.removeItem(item_to_drop)
        item_to_drop.changeState(living, new State("point", living.point))
        FieldTracer.addTraceSecondToLast(living.point, item_to_drop)
        BottomMessages.addPropMessage("decision.drop", living.stat("name"), item_to_drop.getState.getString("name"))
        true
      }
      case None => false
    }
  }
}

class PickUpItem(living:Living) extends Decision(living) {
  def doAction = {
    FieldTracer.findObjectAtPoint(living.point, "item") match {
      case Some(item) => {
        living.inventory.addItem(item)
        FieldTracer.removeTraces(item)
        BottomMessages.addPropMessage("decision.pickup", living.stat("name"), item.getState.getString("name"))
      }
      case None => BottomMessages.addPropMessage("decision.pickup.failed", living.stat("name"))
    }
    true
  }
}

class OpenWeapon(living:Living) extends Decision(living) {
  def doAction = {
    living.weapon.showWeapon()
    true
  }
}

class OpenInventory(living:Living) extends Decision(living) {
  def doAction = {
    living.inventory.showInventory()
    true
  }
}

class IssueCommand(player:Player) extends Decision(player) {
  private def findPlayer = FieldTracer.findVisibleObject(player.point, player.getState.getInt("dov"), obj => {
    obj.getState.contains("player") && obj.getState.getInt("health") > 0
  })

  def doAction = {
    player.selectCommand match {
      case 1 => findPlayer match {
        case Some(other_player) => {
          other_player.changeState(player, new State("follow"))
          BottomMessages.addPropMessage("decision.followme", player.getState.getString("name"), other_player.getState.getString("name"))
        }
        case None =>
      }
      case 2 => findPlayer match {
        case Some(other_player) => {
          other_player.changeState(player, new State("stay"))
          BottomMessages.addPropMessage("decision.stay", player.getState.getString("name"), other_player.getState.getString("name"))
        }
        case None =>
      }
      case 3 => findPlayer match {
        case Some(other_player) => {
          other_player.changeState(player, new State("attack"))
          BottomMessages.addPropMessage("decision.attack", player.getState.getString("name"), other_player.getState.getString("name"))
        }
        case None =>
      }
      case 4 => findPlayer match {
        case Some(other_player) => {
          other_player.changeState(player, new State("noattack"))
          BottomMessages.addPropMessage("decision.noattack", player.getState.getString("name"), other_player.getState.getString("name"))
        }
        case None =>
      }
      case _ =>
    }
    true
  }
}

class DoNothing(living:Living) extends Decision(living) {
  def doAction = true
}

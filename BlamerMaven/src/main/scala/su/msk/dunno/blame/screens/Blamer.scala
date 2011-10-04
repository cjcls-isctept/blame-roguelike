package su.msk.dunno.blame.screens

import su.msk.dunno.scage.screens.ScageScreen
import su.msk.dunno.scage.screens.ScageScreen._
import su.msk.dunno.scage.single.support.messages.ScageMessage._
import org.lwjgl.input.Keyboard._
import su.msk.dunno.scage.single.support.Vec
import su.msk.dunno.scage.single.support.ScageColors._
import su.msk.dunno.scage.single.support.ScageProperties._
import su.msk.dunno.blame.field.FieldTracer
import su.msk.dunno.blame.field.tiles.{Door, Wall, Floor}

import su.msk.dunno.scage.screens.handlers.Renderer._
import su.msk.dunno.blame.support.{BottomMessages, TimeUpdater, GenLib}
import su.msk.dunno.blame.livings.{SiliconCreature, Cibo, Killy}
import su.msk.dunno.blame.decisions._

object Blamer extends ScageScreen(
  screen_name = "Blamer",
  is_main_screen = true,
  properties = "blame.properties") {
  val right_messages_width = property("rightmessages.width", 200)
  
  // map
  private val maze = GenLib.CreateStandardDunegon(FieldTracer.N_x, FieldTracer.N_y)
  for {
    i <- 0 until FieldTracer.N_x
    j <- 0 until FieldTracer.N_y
  } {
    maze(i)(j) match {
      case '#' => new Wall(i, j)
      case '.' => new Floor(i, j)
      case ',' => new Floor(i, j)
      case '+' => new Door(i, j)
      case _ =>
    }
  }
  
  // players
  private var is_play_cibo = false
  val killy = FieldTracer.randomPassablePoint() match {
    case Some(point) => new Killy(point)
    case None => {
      log.error("failed to place killy to the field, the programm will exit")
      System.exit(1)
      null
    }
  }
  val cibo = FieldTracer.randomPassablePoint(killy.point - Vec(2,2), killy.point + Vec(2,2)) match {
    case Some(point) => new Cibo(point)
    case None => {
      log.error("failed to place cibo to the field, the programm will exit")
      System.exit(1)
      null
    }
  }
  def currentPlayer = if(is_play_cibo) cibo else killy
  
  // enemies
  for(i <- 1 to 50) FieldTracer.randomPassablePoint() match {
    case Some(point) => new SiliconCreature(point)
    case None =>
  }

  // controls on main screen
  private var is_key_pressed = false
  private var pressed_start_time:Long = 0
  private def repeatTime = {
    if(is_key_pressed) {
      if(System.currentTimeMillis - pressed_start_time > 600) 100
      else 300
    }
    else 300
  }
  private def move(step:Vec) {
    if(!is_key_pressed) {
      is_key_pressed = true
      pressed_start_time = System.currentTimeMillis
    }
    TimeUpdater.addDecision(new Move(currentPlayer, step))
  }
  
  key(KEY_NUMPAD9, repeatTime, onKeyDown = move(Vec(1,1)),   onKeyUp = is_key_pressed = false)
  key(KEY_UP,      repeatTime, onKeyDown = move(Vec(0,1)),   onKeyUp = is_key_pressed = false)
  key(KEY_NUMPAD8, repeatTime, onKeyDown = move(Vec(0,1)),   onKeyUp = is_key_pressed = false)
  key(KEY_NUMPAD7, repeatTime, onKeyDown = move(Vec(-1,1)),  onKeyUp = is_key_pressed = false)
  key(KEY_RIGHT,   repeatTime, onKeyDown = move(Vec(1,0)),   onKeyUp = is_key_pressed = false)
  key(KEY_NUMPAD6, repeatTime, onKeyDown = move(Vec(1,0)),   onKeyUp = is_key_pressed = false)
  key(KEY_NUMPAD5, repeatTime, onKeyDown = move(Vec(0,0)),   onKeyUp = is_key_pressed = false)
  key(KEY_LEFT,    repeatTime, onKeyDown = move(Vec(-1,0)),  onKeyUp = is_key_pressed = false)
  key(KEY_NUMPAD4, repeatTime, onKeyDown = move(Vec(-1,0)),  onKeyUp = is_key_pressed = false)
  key(KEY_NUMPAD3, repeatTime, onKeyDown = move(Vec(1,-1)),  onKeyUp = is_key_pressed = false)
  key(KEY_DOWN,    repeatTime, onKeyDown = move(Vec(0,-1)),  onKeyUp = is_key_pressed = false)
  key(KEY_NUMPAD2, repeatTime, onKeyDown = move(Vec(0,-1)),  onKeyUp = is_key_pressed = false)
  key(KEY_NUMPAD1, repeatTime, onKeyDown = move(Vec(-1,-1)), onKeyUp = is_key_pressed = false)
  
  key(KEY_O,     onKeyDown = TimeUpdater.addDecision(new OpenDoor(currentPlayer)))
  key(KEY_C,     onKeyDown = TimeUpdater.addDecision(new CloseDoor(currentPlayer)))
  key(KEY_F,     onKeyDown = TimeUpdater.addDecision(new Shoot(currentPlayer)))
  key(KEY_I,     onKeyDown = TimeUpdater.addDecision(new OpenInventory(currentPlayer)))
  key(KEY_W,     onKeyDown = TimeUpdater.addDecision(new OpenWeapon(currentPlayer)))
  key(KEY_D,     onKeyDown = TimeUpdater.addDecision(new DropItem(currentPlayer)))
  key(KEY_COMMA, onKeyDown = TimeUpdater.addDecision(new PickUpItem(currentPlayer)))
  
  key(KEY_TAB,    onKeyDown = is_play_cibo = !is_play_cibo)
  key(KEY_ESCAPE, onKeyDown = stopApp())

  private lazy val help_screen = new ScageScreen("Help Screen") {
    key(KEY_ESCAPE, onKeyDown = stop())

    interface {
        print(xml("helpscreen.tutorial.keys"),           10,  screen_height-20)
        print(xml("helpscreen.tutorial.description"),    300, screen_height-65)
        print(xml("helpscreen.helpmessage"), 10, row_height, GREEN)
    }
  }
  key(KEY_F1, onKeyDown = help_screen.run())
  key(KEY_T, onKeyDown = TimeUpdater.addDecision(new IssueCommand(currentPlayer)))

  // render on main screen
  windowCenter = Vec((width - right_messages_width)/2, 
  		     BottomMessages.bottom_messages_height + (height - BottomMessages.bottom_messages_height)/2)
  center = FieldTracer.pointCenter(currentPlayer.getPoint)
  
  backgroundColor = BLACK

  def drawInterface() {
    def intStat(key:String) = currentPlayer.intStat(key).toString

    //messages on the right side of the screen
    print(currentPlayer.stat("name"),             width - right_messages_width, height-25, WHITE)
    /*print("FPS: "+Renderer.fps,                   width - right_messages_width, height-45, WHITE)
    print("time: "+TimeUpdater.time,              width - right_messages_width, height-65, WHITE)*/
    print(xml("mainscreen.stats.health", intStat("health"), intStat("max_health")),
      width - right_messages_width, height-65, WHITE)
    /*print("Follow: "+currentPlayer.boolStat("follow"), width - right_messages_width, height-105, WHITE)
    print("Attack: "+currentPlayer.boolStat("attack"), width - right_messages_width, height-125, WHITE)
    print("Last Action: "+currentPlayer.lastActionTime, width - right_messages_width, height-145, WHITE)*/
    print(xml("mainscreen.stats.damage", intStat("damage")),
      width - right_messages_width, height-85, WHITE)
    print(xml("mainscreen.stats.energy", intStat("energy"), intStat("max_energy"), intStat("energy_increase_rate")),
      width - right_messages_width, height-105, WHITE)
    print(xml("mainscreen.stats.shield", intStat("shield"), intStat("max_shield"), intStat("shield_increase_rate")),
      width - right_messages_width, height-125, WHITE)
    print(xml("mainscreen.stats.level", intStat("level")), width - right_messages_width, height-145, WHITE)
    print(xml("mainscreen.stats.speed", intStat("speed")), width - right_messages_width, height-165, WHITE)
  } 

  render {
    FieldTracer.drawField(currentPlayer.getPoint)
  }

  interface {
      BottomMessages.showBottomMessages(0)
      drawInterface()
  }
  
  // initial message
  BottomMessages.addPropMessage("mainscreen.openhelp")
  BottomMessages.addPropMessage("greetings.helloworld", currentPlayer.stat("name"))
  
  def main(args:Array[String]) {run()}
}

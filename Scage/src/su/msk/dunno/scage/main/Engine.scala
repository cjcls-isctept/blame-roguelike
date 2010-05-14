package su.msk.dunno.scage.main

import java.util.Properties
import java.io.FileInputStream
import org.lwjgl.opengl.{GL11, DisplayMode, Display}
import org.lwjgl.util.glu.GLU
import su.msk.dunno.scage.prototypes.{THandler, TObject}
import su.msk.dunno.scage.handlers.{Idler, Renderer}
import su.msk.dunno.scage.handlers.eventmanager.EventManager

object Engine {
  private val properties:Properties = {
    if(properties == null) {
      val p:Properties = new Properties()
      p.load(new FileInputStream("options.txt"))
      p
    }
    else properties
  }
  def getProperty(key:String):String = properties.getProperty(key)
  def getIntProperty(key:String):Int = Integer.valueOf(properties.getProperty(key)).intValue

  private var isRunning = true
  def start() = {
    isRunning = true
    run
  }
  def stop() = {isRunning = false}

  private var objects = List[TObject]()
  def getObjects() = objects
  def addObject(o:TObject) = {objects = o :: objects}
  def addObjects(o:List[TObject]) = {objects = o ::: objects}

  private var handlers = List[THandler]()
  def getHandlers() = handlers
  def setDefaultHandlers() = {handlers = EventManager :: Renderer :: Idler :: Nil}
  def addHandler(h:THandler) = {handlers = h :: handlers}
  def addHandlers(h:List[THandler]) = {handlers = h ::: handlers}

  def run():Unit = {
    if(!isRunning) handlers.foreach(h => h.exitSequence)
    else {
      handlers.foreach(h => h.doAction)
      run
    }
  }
}
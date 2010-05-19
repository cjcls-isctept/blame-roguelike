package su.msk.dunno.scage.main

import java.util.Properties
import java.io.FileInputStream
import su.msk.dunno.scage.handlers.eventmanager.EventManager
import su.msk.dunno.scage.prototypes.{Physical, THandler}
import su.msk.dunno.scage.handlers.{Physics, Idler, Renderer}

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
  def getFloatProperty(key:String):Float = java.lang.Float.valueOf(properties.getProperty(key)).floatValue

  private var objects = List[Physical]()
  def getObjects() = objects
  def addObject(o:Physical) = {objects = o :: objects}
  def addObjects(lo:List[Physical]) = {objects = lo ::: objects}

  private var handlers = List[THandler]()
  def getHandlers() = handlers
  def setDefaultHandlers() = {handlers = EventManager :: Physics :: Renderer :: Idler :: Nil}
  def addHandler(h:THandler) = {handlers = h :: handlers}
  def addHandlers(h:List[THandler]) = {handlers = h ::: handlers}

  private var isRunning = true
  def start() = {
    isRunning = true
    run()
  }
  def stop() = {isRunning = false}

  private var msek = System.currentTimeMillis
  private var frames:Int = 0
  var fps:Int = 0
  def countFPS() = {
    frames += 1
    if(System.currentTimeMillis - msek >= 1000) {
      fps = frames
      frames = 0
      msek = System.currentTimeMillis
    }
  }

  def run():Unit = {
    if(!isRunning) handlers.foreach(h => h.exitSequence)
    else {
      handlers.foreach(h => h.doAction)
      countFPS
      run
    }
  }
}
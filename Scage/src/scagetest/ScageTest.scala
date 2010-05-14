package scagetest

import su.msk.dunno.scage.main.Engine
import su.msk.dunno.scage.prototypes.TObject
import org.lwjgl.input.Keyboard
import su.msk.dunno.scage.handlers.eventmanager.{KeyListener, EventManager}
import su.msk.dunno.scage.support.{Vec, Color, TrueTypeFont}

object ScageTest {
  def main(args:Array[String]):Unit = {
    Engine.setDefaultHandlers

    val hello_world = new TObject {
        private var coord = Vec(20, Engine.getIntProperty("height")-40)

        EventManager.addKeyListener(Keyboard.KEY_UP, 100, () => coord += Vec(0,10))
        EventManager.addKeyListener(Keyboard.KEY_DOWN, 100, () => coord -= Vec(0,10))
        EventManager.addKeyListener(Keyboard.KEY_RIGHT, 100, () => coord += Vec(10,0))
        EventManager.addKeyListener(Keyboard.KEY_LEFT, 100, () => coord -= Vec(10,0))

        override def render():Unit = TrueTypeFont.instance().drawString("hello world", coord.x, coord.y, Color.BLACK);
    }

    Engine.addObjects(
      hello_world ::
      new TObject {
        val height = Engine.getIntProperty("height");

        var fps:Int = 0
        var frames:Int = 0
        var msek:Long = System.currentTimeMillis
        def getFPS():Int = {
          frames += 1
          if(System.currentTimeMillis - msek >= 1000) {
            fps = frames
            frames = 0
            msek = System.currentTimeMillis
          }
          fps
        }

        override def render():Unit = {
          TrueTypeFont.instance().drawString("fps: "+getFPS, 20, height-25, Color.BLACK);
        }
      }
    )
    Engine.start
  }
}
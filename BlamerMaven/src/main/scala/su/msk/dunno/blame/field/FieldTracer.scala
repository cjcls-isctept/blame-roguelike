package su.msk.dunno.blame.field

import su.msk.dunno.scage.screens.handlers.Renderer._
import su.msk.dunno.scage.single.support.ScageProperties._
import rlforj.los.{BresLos, ILosBoard, PrecisePermissive}
import collection.JavaConversions
import su.msk.dunno.blame.support.{BottomMessages, MyFont}
import su.msk.dunno.blame.screens.Blamer
import su.msk.dunno.scage.screens.support.tracer._
import org.newdawn.slick.util.pathfinding._
import su.msk.dunno.scage.single.support.{Vec, ScageColors, ScageColor}

trait FieldObject extends Trace {
  def getSymbol:Int
  def getColor:ScageColor
  def isTransparent:Boolean
  def isPassable:Boolean
  
  private var was_drawed = false
  def wasDrawed = was_drawed

  var is_draw_prevented = false
  
  def draw[T <: Trace](tracer:ScageTracer[T]) {
    if(!is_draw_prevented) drawDisplayList(getSymbol, tracer.pointCenter(point), getColor)
    was_drawed = true
  }
  def drawGray[T <: Trace](tracer:ScageTracer[T]) {
    if(!is_draw_prevented) drawDisplayList(getSymbol, tracer.pointCenter(point), ScageColors.GRAY)
  }
}

object FieldTracer extends ScageTracer[FieldObject] {
  def addTraceSecondToLast(p:Vec, fo:FieldObject) = {
    if(isPointOnArea(p)) {
      fo.point is p
      if(!point_matrix(p.ix)(p.iy).isEmpty) {
        point_matrix(p.ix)(p.iy) = point_matrix(p.ix)(p.iy).head :: fo :: point_matrix(p.ix)(p.iy).tail
      } else point_matrix(p.ix)(p.iy) = fo :: point_matrix(p.ix)(p.iy)
      traces_by_ids += fo.id -> fo
      traces_list = fo :: traces_list
      log.debug("added new field trace #"+fo.id+" in point ("+fo.point+")")
    }
    else log.warn("failed to add field trace: point ("+fo.point+") is out of area")
    fo.id
  }

  override def removeTracesById(trace_ids:Int*) {
    super.removeTracesById(trace_ids:_*)
    removeLightSources(trace_ids:_*)
  }

  def isPointPassable(x:Int, y:Int, trace_id:Int):Boolean = 
    isPointOnArea(Vec(x, y)) && (point_matrix(x)(y).length == 0 || point_matrix(x)(y).filter(_.id != trace_id).forall(_.isPassable))
  def isPointPassable(point:Vec, trace_id:Int = -1):Boolean = isPointPassable(point.ix, point.iy, trace_id)
  def isCoordPassable(coord:Vec) = isPointPassable(point(coord), -1)

  def isPointTransparent(x:Int, y:Int) = {
    isPointOnArea(Vec(x, y)) && (point_matrix(x)(y).length == 0 || point_matrix(x)(y).forall(_.isTransparent))
  }

  def randomPassablePoint(from:Vec = Vec(0, 0), to:Vec = Vec(N_x, N_y)):Option[Vec] = {
    log.debug("looking for new random passable point")

    var x = -1
    var y = -1

    val max_count = 10
    var count = max_count
    while(!isPointPassable(x, y, -1) && count > 0) {
      x = (from.x + math.random*(to.x - from.x)).toInt
      y = (from.y + math.random*(to.y - from.y)).toInt

      count -= 1
    }
    if(count == 0 && !isPointPassable(x, y, -1)) {
      log.warn("warning: cannot locate random passable point within "+max_count+" tries")
      None
    } else Some(Vec(x, y))
  }
  
  def direction(from:Vec, to:Vec) = {
    val diff = (to - from)
    Vec(math.signum(diff.x), math.signum(diff.y))
  }
  
  def isDirectionPassable(from:Vec, to:Vec) = {
    isPointPassable(from + direction(from, to))
  }
  
  def move2PointIfPassable(fo:FieldObject, new_point:Vec) {
    if(isPointPassable(new_point, fo.id)) {
      updateLocation(fo, new_point)
    }
  }
  
  def visibleObjectsNear(point:Vec, dov:Int, condition:(FieldObject) => Boolean) = {
    traces(point, -dov to dov, (obj) => isVisible(point, obj.point, dov) && condition(obj))
  }
  def findVisibleObject(point:Vec, dov:Int, condition:(FieldObject) => Boolean) = {
    traces(point, -dov to dov, (obj) => isVisible(point, obj.point, dov) && condition(obj)) match {
      case head :: tail => Some(head)
      case _ => None
    }
  }

  def objectsAtPoint(point:Vec) =
    if(isPointOnArea(point)) point_matrix(point.ix)(point.iy).toList
    else Nil
  def findObjectAtPoint(point:Vec, object_type:String) = objectsAtPoint(point).find(_.getState.contains(object_type))

  def isNearPlayer(point:Vec) = (Blamer.currentPlayer.point dist point) < visibility_distance
  def pourBlood(point:Vec, color:ScageColor) {
    traces(point, -1 to 1, (fieldObject) => fieldObject.getState.contains("tile")) match {
      case Nil =>
      case tiles:List[FieldObject] => {
        val random_pos = (math.random*tiles.size).toInt
        tiles(random_pos).changeState(tiles(random_pos), new State("color", color))
      }
    }
  }

  private val lineView = new ILosBoard {
    def contains(x:Int, y:Int):Boolean = isPointOnArea(Vec(x, y))
    def isObstacle(x:Int, y:Int):Boolean = !isPointTransparent(x, y)
    def visit(x:Int, y:Int) {}
  }
  private val bresenham = new BresLos(false)  
  def isVisible(p1:Vec, p2:Vec, dov:Int) = {
    if((p2 dist p1) > (dov+1)*(dov+1)) false
    else if(p2 == p1) true
    else bresenham.existsLineOfSight(lineView, p1.ix, p1.iy, p2.ix, p2.iy, false)
  }
  def line(p1:Vec, p2:Vec):List[Vec] = {
    bresenham.existsLineOfSight(lineView, p1.ix, p1.iy, p2.ix, p2.iy, true)
    JavaConversions.asScalaBuffer(bresenham.getProjectPath).foldLeft(List[Vec]())((line, point) => new Vec(point.x, point.y) :: line).reverse
  }
  
  def preventDraw(point:Vec) {
    if(!point_matrix(point.ix)(point.iy).isEmpty) point_matrix(point.ix)(point.iy).foreach(_.is_draw_prevented = true)
  }
  def allowDraw(point:Vec) {
    if(!point_matrix(point.ix)(point.iy).isEmpty) point_matrix(point.ix)(point.iy).foreach(_.is_draw_prevented = false)
  }

  private var light_sources:List[(() => Vec, () => Int, Int)] = Nil
  def addLightSource(point: => Vec, dov: => Int = 5, trace_id:Int) {
    light_sources = (() => point, () => dov, trace_id) :: light_sources
  }
  def removeLightSources(trace_ids:Int*) {
    light_sources = light_sources.filterNot(light_source => trace_ids.contains(light_source._3))
  }
  def isLightSource(trace_id:Int) = light_sources.exists(_._3 == trace_id)
  
  private val pp = new PrecisePermissive();  
  private val drawView = new ILosBoard() {
    def contains(x:Int, y:Int):Boolean = isPointOnArea(Vec(x, y))
    def isObstacle(x:Int, y:Int):Boolean = !isPointTransparent(x, y)
    def visit(x:Int, y:Int) {
      if(point_matrix(x)(y).length > 0) point_matrix(x)(y).head.draw(FieldTracer)
    }   
  }

  def drawField(player_point:Vec) = {
    drawGray(player_point)
    drawEnlighted(player_point)
  }

  val field_visible_width  = property("field.visible.width",  screen_width - Blamer.right_messages_width)
  val field_visible_height = property("field.visible.height", screen_height - BottomMessages.bottom_messages_height)

  private val half_visible_N_x:Int = field_visible_width/h_x/2
  private val half_visible_N_y:Int = field_visible_height/h_y/2

  val visibility_distance = math.min(half_visible_N_x, half_visible_N_y)*math.min(half_visible_N_x, half_visible_N_y)
  private def drawEnlighted(player_point:Vec) = {
    light_sources.filter(source => (source._1() dist2 player_point) < visibility_distance).foreach(source => {
      pp.visitFieldOfView(drawView, source._1().ix, source._1().iy, source._2())
    })
  }

  private def drawGray(player_point:Vec) {
    val from_x = math.max(0,     player_point.ix - half_visible_N_x)
    val to_x   = math.min(N_x-1, player_point.ix + half_visible_N_x)
    val from_y = math.max(0,     player_point.iy - half_visible_N_y)
    val to_y   = math.min(N_y-1, player_point.iy + half_visible_N_y)
    for {
      x <- from_x to to_x
      y <- from_y to to_y
      if !point_matrix(x)(y).isEmpty
      tile = point_matrix(x)(y).last
      if tile.wasDrawed && tile.getSymbol != MyFont.FLOOR
    } {
      point_matrix(x)(y).head.drawGray(this)
    }
  }

  private lazy val path_finder:PathFinder = new AStarPathFinder(new TileBasedMap {
    def getWidthInTiles  = N_x
    def getHeightInTiles = N_y
    def blocked(context:PathFindingContext, tx:Int, ty:Int) = {
      findObjectAtPoint(Vec(tx,ty), "tile") match {
        case Some(tile) => tile.getState.contains("wall")
        case None => false
      }
    }
    def getCost(context:PathFindingContext, tx:Int, ty:Int) = {
      findObjectAtPoint(Vec(tx,ty), "door") match {
        case Some(door) => {
          if(door.getState.contains("close")) 3
          else 1
        }
        case None => 1
      }
    }
    def pathFinderVisited(x:Int, y:Int) {}
  }, 500, true)
  def findPath(p1:Vec, p2:Vec):List[Vec] = {
    val path = path_finder.findPath(new Mover{}, p1.ix, p1.iy, p2.ix, p2.iy)
    if(path.getLength > 2)
    (path.getLength-2 to 1 by -1).foldLeft(List[Vec]())((coord_list, index) =>
      Vec(path.getX(index), path.getY(index)) :: coord_list)
    else Nil
  }
}

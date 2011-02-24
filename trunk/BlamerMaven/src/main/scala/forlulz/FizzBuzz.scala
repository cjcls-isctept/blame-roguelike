package forlulz

object FizzBuzz extends Application {
  def fizzbuzz = (1 to 100).foreach(i => {
    (if(i%3 == 0) "Fizz" else "") + (if(i%5 == 0) "Buzz" else "") match {
      case "" => println(i)
      case s:String => println(s)
    }
  })

  def reverse(str:String):String = {
    def _reverse(new_str:String, old_str:String):String = {
      if("" == old_str) new_str
      else _reverse(new_str+old_str.last, old_str.init)
    }
    _reverse("", str)
  }

  def mycase(other_str:String)(p: => Unit) = if(!selected && check_str == other_str) {
    todo = () => p
    selected = true
  }
  def mydefaultcase(p: => Unit) = {
    if(!selected) {
      todo = () => p
      selected = true
    }
  }
  private var check_str:String = ""
  private var selected = false
  private var todo:() => Unit = () => {}
  implicit def toWithMyMatch(str:String) = new ScalaObject {
    def mymatch(p: => Unit) = {
      check_str = str
      selected = false
      p
      if(selected) todo()
    }
  }

  "hello world".mymatch {
    mycase("hello" + "world") {println(1)}
    mydefaultcase {println(2)}
  }

  //println(reverse("Hello World!"))


}
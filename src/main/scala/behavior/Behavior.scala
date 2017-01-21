package behavior



class Behavior(f:() => Unit){
  private var toRun = f
  protected def init = {}
   def run = {init; f() }
}

object Behavior {
   def apply(f: => Unit) = new Behavior(()=>f)
}

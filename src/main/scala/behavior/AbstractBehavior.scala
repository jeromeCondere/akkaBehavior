package behavior



abstract class AbstractBehavior(toRun:() => Unit){
  //private var toRun = f
  
  protected def init = {}
   def run = {init; toRun() }
}


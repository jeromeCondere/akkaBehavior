package behavior

case class AskForRun(behavior:Behavior)

abstract class AbstractBehavior(toRun:() => Unit){
  
  protected def init = {}
   def run = {init; toRun() }
}
 


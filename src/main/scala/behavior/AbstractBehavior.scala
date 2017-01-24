package behavior

sealed trait BehaviorMessage
//message types
sealed trait AskMessage extends BehaviorMessage
sealed trait RequestMessage extends BehaviorMessage
sealed trait InformMessage extends BehaviorMessage
//Ask messages
case object AskForStop  extends AskMessage
case object AskForRun  extends AskMessage
//Request messages
case object Run
case object Stop



abstract class AbstractBehavior(toRun:() => Unit){
  protected var isInit = false 
  protected def init = {}
  final def setup ={ 
    if(isInit ==false)
      {
        init
        isInit=true
      }    
  }
   def run = {toRun() }
}
 


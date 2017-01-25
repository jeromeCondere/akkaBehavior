package behavior
import akka.actor.ActorRef
import akka.actor.Actor
import akka.actor.PoisonPill
sealed trait BehaviorMessage
//message types
sealed trait AskMessage extends BehaviorMessage
sealed trait RequestMessage extends BehaviorMessage
sealed trait InformMessage extends BehaviorMessage
//Ask messages
case object AskForStop  extends AskMessage
case object AskForRun  extends AskMessage
//Request messages
case object Run extends RequestMessage
case object Stop extends RequestMessage
case object Refuse extends RequestMessage
//Inform message
case object Finished extends InformMessage


abstract class AbstractBehavior(toRun:() => Unit)(implicit supervisor:ActorRef) extends Actor{
  private[this] var isInit = false 
  protected def init = {}
  final def setup ={ 
    if(isInit ==false)
      {
        init
        isInit=true
      }    
  }
  // kill self and inform the supervisor that the behavior is finished
  final protected def killSelf = {
    supervisor ! Finished
    self ! PoisonPill
  }
  protected def refused ={
    self ! PoisonPill
  }
   def run = {toRun() }
  final def receive =
   {
     case Run => run
     case Stop => killSelf
     case Refuse => refused
     case _ =>
   }
   
}
 


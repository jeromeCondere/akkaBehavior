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
case object Setup extends RequestMessage
//Inform message
case object Finished extends InformMessage

/**
 * Abstract Behavior 
 * The classes extending this class are used by the agent
 * @constructor 
 * @param toRun the callback used to run the behavior
 * @param supervisor reference to the actor that use the behavior
 */
abstract class AbstractBehavior(toRun:() => Unit)(implicit supervisor:ActorRef) extends Actor{
  private[this] var isInit = false 
  /** 
   *  -initialize the behavior
   *  override this method when extending this class
   *  */
  protected def init = {}
  /** initialize the behavior before it runs 
   *  ensure that the initialization is unique
   *  */
  final def setup ={ 
    if(isInit ==false)
      {
        init
        isInit=true
      }    
  }
  /** kill self and inform the supervisor that the behavior is finished*/
  final protected def killSelf = {
    supervisor ! Finished
    self ! PoisonPill
  }
  protected def refused = {
    self ! PoisonPill
  }
  
  /** run the behavior */
  def run = {toRun() }
  
  /** receive method from Actor */
  final def receive =
   {
     case Run => run
     case Stop => killSelf
     case Refuse => refused
     case Setup => setup
     case _ =>
   }
   
}
 


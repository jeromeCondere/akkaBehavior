package behavior
import akka.actor.ActorRef
import akka.actor.FSM
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
class Setup(implicit val supervisor: ActorRef) extends RequestMessage {}
object Setup extends RequestMessage {
  def apply()(implicit supervisor: ActorRef) = new Setup
  def unapply(s: Setup) = Some(s.supervisor)
}
case object Poke extends RequestMessage
//Inform message
case object Finished extends InformMessage

sealed trait BehaviorState
case object Idle extends BehaviorState
case object Ready extends BehaviorState
case object Running extends BehaviorState
case object FinishedRun extends BehaviorState
case object Ended extends BehaviorState
case object Waiting extends BehaviorState
case object Killed extends BehaviorState


sealed trait BehaviorData
case object Void extends BehaviorData

/**
 * Abstract Behavior <br>
 * The classes extending this class are used by the agent
 * @constructor 
 * @param toRun the callback used to run the behavior
 * @param supervisor reference to the actor that use the behavior
 */
abstract class AbstractBehavior(toRun:() => Unit) extends FSM[BehaviorState,BehaviorData]{
  private[this] var isInit = false
  private var supervisor :ActorRef  = _
  
  /** 
   *  initialize the behavior <br>
   *  override this method when extending this class
   *  */
  protected def init = {}
  
  /** run the behavior */
  def run = {
    toRun() 
    self ! FinishedRun
  }
  
  startWith(Idle, Void)
  
  when(Idle)
  {
     case  Event(Setup(s),_) => supervisor = s
                               init
                               goto(Ready) 
     case Event(Stop, _) => self ! Poke
                            goto(Killed)
     case _ =>              stay() 
     
  }

  when(Ready)
  {
     case Event(Run, _) => run
                           goto(Running) 
     case Event(Stop, _) => self ! Poke
                            goto(Killed)
     case _ => stay() 
     
  }
  
  when(Running)
  {
     case Event(FinishedRun, _) => self ! Poke
                                   goto(Ended) 
     case Event(Stop, Void) => goto(Killed)
     case _ => stay() 
  }
  
  when(Ended)
  {
     case _ => supervisor ! Finished
               stop()
  }
   
  when(Killed)
  {
     case _ => stop()
  }
  
   
}
 


package behavior
import akka.actor.ActorRef
import akka.actor.Props
import proxy.BehaviorProxy

/**
 * ParralelBehavior	 <br>
 * A behavior that runs a list of behaviors asynchronously
 * @constructor
 * @param list of behaviors to run asynchronously 
 * @param toRun the callback used to run the behavior
 * @param supervisor reference to the actor that use the behavior
 */
class ParralelBehavior[A <: AbstractBehavior](behaviorProxyList:List[BehaviorProxy[A]]) (toRun:() =>Unit)(implicit supervisor:ActorRef) extends AbstractBehavior(toRun){
 
  /** setup all Behaviors */
  override final protected def init ={
   behaviorProxyList.zipWithIndex.foreach{
      case(behaviorProxy,index) => val actor = context.actorOf(Props(behaviorProxy.behavior), self.path.name +"parallel"+index )
                              context.watch(actor)
                              actor ! Setup 
    }
  }
  
  /** run all behavior in the list of behaviors */
  override def run = {
    //send run message to all message
   context.children.foreach{
     behavior => behavior ! Run
   }
  }
}
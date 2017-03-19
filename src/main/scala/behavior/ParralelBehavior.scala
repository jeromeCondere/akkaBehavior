package behavior
import akka.actor.ActorRef
import akka.actor.Props
import proxy.BehaviorProxy
import scala.reflect.ClassTag 
import scala.reflect._
/**
 * ParralelBehavior	 <br>
 * A behavior that runs a list of behaviors asynchronously
 * @constructor
 * @param list of behaviors to run asynchronously 
 * @param toRun the callback used to run the behavior
 * @param supervisor reference to the actor that use the behavior
 */
class ParralelBehavior[A <: AbstractBehavior](behaviorProxyList:List[BehaviorProxy[A]]) (toRun:() =>Unit)(implicit supervisor:ActorRef) extends AbstractBehavior(toRun){
 
  //to do find a way to override init (if not it won't be used in setup)
  /** setup all Behaviors */
   final protected def init(implicit c: ClassTag[A]) = {
   behaviorProxyList.zipWithIndex.foreach{
      case(behaviorProxy,index) => val actor = context.actorOf(Props(behaviorProxy.behavior()), self.path.name +"parallel"+index )
                              context.watch(actor)
                              actor ! Setup 
    }
   println("issou")
  }
   

  /** run all behavior in the list of behaviors */
  override def run = {
    //send run message to all actors
   context.children.foreach{
     behavior => behavior ! Run
   }
  }
}
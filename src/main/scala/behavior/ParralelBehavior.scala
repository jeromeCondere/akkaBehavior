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
class ParralelBehavior[A <: AbstractBehavior : ClassTag](behaviorProxyList:List[BehaviorProxy[A]]) extends AbstractBehavior(() => {}){
 
  case class info(val name : String, isFinished: Boolean)
  private var behaviorsInfo: List[info]   = List()
  
  //var behaviorInfo : InfoData[]
  /** setup all Behaviors */
  override final protected def init = {
   behaviorProxyList.zipWithIndex.foreach{
      case(behaviorProxy,index) => val name = self.path.name +"_parallel_behavior_"+ index
                                   val actor = context.actorOf(Props(behaviorProxy.behavior()),name )
                                   context.watch(actor)
                                   behaviorsInfo = info(name, false)::behaviorsInfo
                                   actor ! Setup()
    }
  }
  
  /** run all behavior in the list of behaviors */
  override def run = {
    self ! ComplexRun
  }
  
  def complexRun = {
     context.children.foreach { 
       behavior => behavior ! Run
     }
   }
  
  when(ComplexRunning)
  {
    case Event(ComplexRun, _) => complexRun
                                  stay()
    
    case Event(Finished, _) => if(!behaviorsInfo.isEmpty) {
                                 println("finished message: "+ sender)
                                 val name = sender.path.name //obtenir le nom du sender
                                 behaviorsInfo = behaviorsInfo.filter { info => info.name != name}
                                

                                 if(behaviorsInfo.isEmpty) 
                                   self ! FinishedRun

                                 stay()
                                 
                               } 
                               else 
                                 self ! FinishedRun

    
    case Event(FinishedRun, _) => self ! Poke
                                   goto(Ended) 
                               
    case _ => stay()
  }
  
}
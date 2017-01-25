package behavior
import akka.actor.ActorRef
class ParralelBehavior(behaviorList:List[AbstractBehavior]) (toRun:() =>Unit)(implicit supervisor:ActorRef) extends AbstractBehavior(toRun){
  override def run ={

  }
}
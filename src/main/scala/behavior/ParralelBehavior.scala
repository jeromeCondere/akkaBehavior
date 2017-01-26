package behavior
import akka.actor.ActorRef
import akka.actor.Props

class ParralelBehavior(behaviorList:List[AbstractBehavior]) (toRun:() =>Unit)(implicit supervisor:ActorRef) extends AbstractBehavior(toRun){
 
  override final protected def init ={
   behaviorList.zipWithIndex.foreach{
      case(behavior,index) => val actor = context.actorOf(Props(behavior), self.path.name +"parallel"+index )
                              context.watch(actor)
                              actor ! Setup 
    }
  }
  override def run = {
    //send run message to all message
   context.children.foreach{
     behavior => behavior ! Run
   }
  }
}
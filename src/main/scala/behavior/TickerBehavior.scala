package behavior
import scala.concurrent.duration._
import akka.actor.ActorRef

class TickerBehavior (duration:FiniteDuration)(toRun:() => Unit)(implicit supervisor:ActorRef) extends  AbstractBehavior(toRun){
  
  //once the behavior finished to run it ask for run again
  private[this] var isStarted = false
  override final def run() =
  {
    if (isStarted==false)
    {
    val system=context.system
    import system.dispatcher
    system.scheduler.schedule(500 millis,duration){
        if(stop==false)
        {
          toRun() 
        }
        else
        {
          killSelf 
        }
   
      } 
    isStarted = true
    }
    
  }
  //this method retun true to finish the behavior
  protected def stop:Boolean ={false}
}
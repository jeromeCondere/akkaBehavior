package behavior
import scala.concurrent.duration._
import akka.actor.ActorRef

/**
 * TickerBehavior <br>
 * A behavior that run according to a frequency
 * 
 * @constructor 
 * @param toRun the callback used to run the behavior
 * @param supervisor reference to the actor that use the behavior
 * @param period amount of time between two runs
 */
class TickerBehavior (period:FiniteDuration)(toRun:() => Unit)(implicit supervisor:ActorRef) extends  AbstractBehavior(toRun){
  
  //once the behavior finished to run it ask for run again
  private[this] var isStarted = false
  override final def run() =
  {
    if (isStarted==false)
    {
    val system=context.system
    import system.dispatcher
    system.scheduler.schedule(500 millis,period){
        if(stop==false)
          toRun() 
        else
          killSelf 
      } 
    isStarted = true
    }
    
  }
  /**this method retun true to finish the behavior*/
  protected def stop:Boolean ={false}
}

object TickerBehavior {
  def apply(delay:FiniteDuration)(toRun: =>Unit)(implicit supervisor:ActorRef) = new TickerBehavior(delay)(()=> toRun)
}

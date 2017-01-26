package behavior
import scala.concurrent.duration._
import akka.actor.ActorRef

class TimerBehavior(duration:FiniteDuration)(toRun:() => Unit)(implicit supervisor:ActorRef) extends AbstractBehavior(toRun) {

  override final def run() =
  {
    val system=context.system
    import system.dispatcher
    system.scheduler.scheduleOnce(duration){ 
      toRun() 
      supervisor ! Finished
      } 
  }
}
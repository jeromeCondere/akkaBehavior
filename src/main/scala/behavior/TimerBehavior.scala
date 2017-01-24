package behavior
import scala.concurrent.duration._
import akka.actor.Actor

class TimerBehavior(duration:FiniteDuration,implicit val a:Actor)(toRun:() => Unit) extends AbstractBehavior(toRun) {

  override final def run() =
  {
    init;
    val system=a.context.system
    import system.dispatcher
    system.scheduler.scheduleOnce(duration){ toRun() } 
  }
}
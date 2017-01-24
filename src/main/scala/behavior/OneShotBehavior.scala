package behavior
import akka.actor.ActorRef
class OneShotBehavior(toRun:() =>Unit)(implicit supervisor:ActorRef) extends AbstractBehavior(toRun) {
 override def run = {toRun() }
}
object OneShotBehavior {
  def apply(toRun: =>Unit)(implicit supervisor:ActorRef) =new OneShotBehavior(()=> toRun)
  def doNothing = () => {}
}

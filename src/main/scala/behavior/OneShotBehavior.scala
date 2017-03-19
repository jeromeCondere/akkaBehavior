package behavior
import akka.actor.ActorRef


/**
 * OneShotBehavior <br>
 * A behavior that run only once and when finished send a Finished message to its supervisor
 * 
 * @constructor 
 * @param toRun the callback used to run the behavior
 * @param supervisor reference to the actor that use the behavior
 */
class OneShotBehavior(toRun:() =>Unit)(implicit supervisor:ActorRef) extends AbstractBehavior(toRun) {

  /** run the behavior and then finish*/
  final override def run = {
     toRun() 
     supervisor ! Finished //tell the supervisor we have finished
  }
}

object OneShotBehavior {
  def apply(toRun: =>Unit)(implicit supervisor:ActorRef) = new OneShotBehavior(()=> toRun)
  def doNothing = () => {}
}

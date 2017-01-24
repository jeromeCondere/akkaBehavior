package behavior

class OneShotBehavior(toRun:() =>Unit) extends AbstractBehavior( toRun)  {
 override def run = {toRun() }
}
object OneShotBehavior {
  def apply(toRun: =>Unit) =new OneShotBehavior(()=> toRun)
  def doNothing = () => {}
}

package behavior

class Behavior(toRun:() =>Unit) extends AbstractBehavior( toRun)  {
  
}
object Behavior {
  def apply(toRun: =>Unit) =new Behavior(()=> toRun)
  def doNothing = () => {}
}

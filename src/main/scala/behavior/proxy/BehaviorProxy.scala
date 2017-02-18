package behavior.proxy
import behavior.AbstractBehavior
/**
 *  a class that allows the user to store a future instance of a behavior
 */
class BehaviorProxy  [A <: AbstractBehavior] (f:() => A) {
  def behavior = f()
}
package behavior.proxy
import behavior.AbstractBehavior
/**
 *  a class that allows the user to store a future instance of a behavior
 */
final class BehaviorProxy  [A <: AbstractBehavior] (val behavior:() => A) {
  def apply(f:() => A) = {
    new BehaviorProxy(f)
  }
}

object BehaviorProxy {
  def apply[A <: AbstractBehavior](behavior: => A) = new BehaviorProxy[A](() => behavior)
}

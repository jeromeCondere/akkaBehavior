import org.scalatest._
import behavior._
import behavior.OneShotBehavior.doNothing
class BehaviorSpec extends FlatSpec {
  "A behavior" must "run the code properly" in {
    var a = 2
    val be = OneShotBehavior{
      a = a + 2
    }
    be.run
    assert(a==4)
   
  }
  it should "init properly" in {
    class MyBehavior(toRun:() =>Unit) extends OneShotBehavior(toRun)
    {
      var a =2
      override protected def init = a+=2
    }
    var be = new MyBehavior(doNothing)
    be.run
    assert(be.a==4)
   
  }
  it should "init before run" in
  {
    class MyBehavior(toRun: =>Unit) extends OneShotBehavior(()=>toRun)
    {
      var a =2
      
      override protected def init = a+=4
    }
     
    var be = new MyBehavior{a+=7}
    be.run
    assert(be.a==13)
  }
  it should "init only once" in {
    
  }
  it should "change its structure dynamically" in
  {
    
  }
}
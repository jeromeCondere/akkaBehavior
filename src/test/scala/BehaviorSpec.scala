import org.scalatest._
import behavior._

class BehaviorSpec extends FlatSpec {
  "A behavior" must "run the code properly" in {
    var a = 2
    val be = Behavior{
      a =a + 2
    }
    be.run
    assert(a==4)
   
  }
  it should "change its structure dynamically" in
  {
    
  }
}
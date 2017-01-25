import org.scalatest._
import behavior._
import behavior.OneShotBehavior.doNothing
import akka.actor._
import akka.testkit._
  class myActor extends Actor
   {
     def receive = {
       case _ =>
     }
   }
class BehaviorSpec extends TestKit(ActorSystem("MySpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
 
  
implicit val actorRef = TestActorRef[myActor]

  "A behavior" must {
  
    "run the code properly" in {
      var a = 2
      val beRef = TestActorRef(OneShotBehavior{
        a = a + 2
      }(actorRef) )
      
      val be = beRef.underlyingActor
      be.run
      assert(a==4)
     }
    
   "init properly" in {
      class MyBehavior(toRun:() =>Unit) extends OneShotBehavior(toRun)
      {
        
        var a =2
        override protected def init = a+=2
      }
      val beRef = TestActorRef (new MyBehavior(doNothing) )
      val be =beRef.underlyingActor
      be.setup
      assert(be.a==4)
   
  }
   
   "init only once" in {
     
      class MyBehavior2(toRun:() =>Unit) extends OneShotBehavior(toRun)
      {
        var b =2
        
        override protected def init = b+=4
      }

      var beRef = TestActorRef( new MyBehavior2(()=>{}) )
      val be = beRef.underlyingActor
      assert(be.b==2)
      be.setup
      assert(be.b==6)
      be.setup //setting up twice doesn't change the value
      assert(be.b== 6)
    }
  
}
 /* it should 
  it should 
  it should "init only once" in {
    
  }
  it should "change its structure dynamically" in
  {
    
  }
  */
 
}
import org.scalatest._
import behavior._
import behavior.OneShotBehavior.doNothing
import akka.actor._
import akka.testkit._
import scala.concurrent.duration._

  class SupervisorActor extends Actor
   {
     def receive = {
       case _ =>
     }
   }
class BehaviorSpec extends TestKit(ActorSystem("MySpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
 
  
implicit val actorRef = TestActorRef[SupervisorActor]

  "A behavior (general)" must {
  
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
   
  "change its structure dynamically" in
    {
      fail
    }
}

"A TimerBehavior" must {
  
  "execute after a determinded duration" in {
    var a =2
    var beRef =TestActorRef(TimerBehavior(1 seconds){
      info("youhou")
      a+=4
    })
    within(1 seconds)
    {
      
       beRef ! Run
      assert(a==2)
    }
    assert(a==4)
    fail
  }
  
  "send a Finished message after death to supervisor"  in {
    fail
  }
  
}

"A TickerBehavior" must {
  "exec repeatedly" in {
    fail
  }
    "send a Finished message after death to supervisor"  in {
    fail
  }
}

"A ParallelBehavior" must {
  
  "init all behaviors correctly" in {
    fail
  }
  
  "launch several behaviors asynchronously" in {
    fail
  }
  
  "receive a Finished message from every agent stopped"  in {
    fail
  }
  
}



}
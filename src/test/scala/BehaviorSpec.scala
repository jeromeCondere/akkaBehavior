import org.scalatest._
import behavior._
import behavior.proxy.BehaviorProxy
import behavior.OneShotBehavior.doNothing
import akka.actor._
import akka.testkit._
import scala.concurrent.duration._

class BehaviorSpec extends TestKit(ActorSystem("MySpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
 
// the supervisor is the implicit sender   
implicit val systemSupervisor = self

  "A behavior (general)" must {
  
    "run the code properly" in {
      var a = 2
      val beRef = TestActorRef(OneShotBehavior{
        a = a + 2
      } )
      assert(a==2)
      
      beRef ! Setup()
      beRef ! Run
      awaitCond(a == 4, 50 millis)
     }
    
   "init properly" in {
      class MyBehavior(toRun:() =>Unit) extends OneShotBehavior(toRun)
      {
        
        var a =2
        override protected def init = a+=2
      }
      val beRef = TestActorRef (new MyBehavior(doNothing) )
      val be = beRef.underlyingActor
      beRef ! Setup()
      assert(be.a==4)
      awaitCond(be.a == 4, 50 millis)
   
  }
   
   "init only once" in {
     
      class MyBehavior(toRun:() =>Unit) extends OneShotBehavior(toRun)
      {
        var b =2
        override protected def init = b+=4
      }

      var beRef = TestActorRef( new MyBehavior(()=>{}) )
      val be = beRef.underlyingActor
      assert(be.b==2)
      beRef ! Setup()
      awaitCond(be.b == 6, 50 millis)
      beRef ! Setup() //setting up twice doesn't change the value
      awaitCond(be.b == 6, 100 millis)
    }
}

"A TimerBehavior" must {
  
  "execute after a determinded duration" in {
    case object Okay
    var a =2
    var beRef = TestActorRef(TimerBehavior(100 millis){
      a+=3
    })
    
    beRef ! Setup()
    beRef ! Run
    awaitCond(a==5, 190 millis) // we give +90 millis to check if a has been updated
  }
  
  "send a Finished message after death to supervisor"  in {
    var beRef = TestActorRef(TimerBehavior(50 millis){
      
    })
    
    beRef ! Setup()
    beRef ! Run
    expectMsg(70 millis,Finished)
  }
  
}

"A TickerBehavior" must {
  "exec repeatedly" in {
    var e = 0
    var beRef = TestActorRef(TickerBehavior(50 millis){
      e+=2
    })
    beRef ! Setup()
    beRef ! Run
    // we test every 40 millis if the condition holds
    awaitCond(e==20, 600 millis, 40 millis)
    beRef ! Stop
    
  }
    "send a Finished message after death to supervisor"  in {
     var beRef = TestActorRef(TickerBehavior(50 millis){
      
    })
      beRef ! Setup()
      beRef ! Run
      beRef ! Stop
      expectMsg(Finished)
  }
}

"A ParallelBehavior" must {
  
  "init all behaviors correctly" in {
    var a1 = 4
    var a2 = 6
    class MyBehavior(toRun:() =>Unit) extends OneShotBehavior(toRun)
    {
      override protected def init = { a1+=2; print(a1+" ")}
       
    }
    class MyBehaviorBis(toRun:() =>Unit) extends OneShotBehavior(toRun)
    {
      override protected def init = a2+=3
    }
    //double Setup()
    val bp1 = BehaviorProxy{new MyBehavior(OneShotBehavior.doNothing)}
    val bp2 = BehaviorProxy{new MyBehavior(OneShotBehavior.doNothing)}
    val listBp = List(bp1,bp2)
    var beRef = TestActorRef(new ParralelBehavior(listBp),"parrallel")
    
    beRef ! Setup()
    awaitCond(a1==6 && a2==9, 300 millis)
    println("a1 "+a1)
    println("a2 "+a2)
    
  }
  
  "launch several behaviors asynchronously" in {
    fail
  }
  
  "receive a Finished message from every agent stopped"  in {
    fail
  }
  
}



}
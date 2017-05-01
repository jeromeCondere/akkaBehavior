import org.scalatest._
import behavior._
import behavior.proxy.BehaviorProxy
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
      } , "runBehavior")
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
      val beRef = TestActorRef (new MyBehavior(doNothing()), "initBehavior" )
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

      var beRef = TestActorRef( new MyBehavior(()=>{}) , "initBehavior2")
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
      
    }, "finishBehavior")
    
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
    }, "tickerBehavior")
    beRef ! Setup()
    beRef ! Run
    // we test every 40 millis if the condition holds
    awaitCond(e==20, 600 millis, 40 millis)
    beRef ! Stop
  }
    "send a Finished message after death to supervisor"  in {
     var beRef = TestActorRef(TickerBehavior(50 millis){
      
    },"finishTickerBehavior")
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
      override protected def init = a1+=2
       
    }
    class MyBehaviorBis(toRun:() =>Unit) extends OneShotBehavior(toRun)
    {
      override protected def init = a2+=3
    }
  
    val bp1 = BehaviorProxy[OneShotBehavior]{new MyBehavior(doNothing())}
    val bp2 = BehaviorProxy[OneShotBehavior]{new MyBehaviorBis(doNothing())}
    val listBp = List(bp1,bp2)
    var beRef = TestActorRef(new ParralelBehavior(listBp),"parrallelBehavior")
    
    beRef ! Setup()
    awaitCond(a1==6 && a2==9, 300 millis)
  }
  
  "launch several behaviors asynchronously" in {
    var a1 = 7
    var a2 = 9
    val bp1 = BehaviorProxy{OneShotBehavior{
      a1+=3
      a2+=1    
    }}

    val bp2 = BehaviorProxy{OneShotBehavior{
      a1+=7
      a2+=4    
    }}
    val listBp = List(bp1,bp2)
    var beRef = TestActorRef(new ParralelBehavior(listBp),"parrallelBehavior2")
    
    beRef ! Setup()
    beRef ! Run
    awaitCond(a1==17 && a2==14, 300 millis)
  }
  
  "receive a Finished message from every agent stopped"  in {
    val bp1 = BehaviorProxy{OneShotBehavior{
      var a = 1  
      for ( i<-1 to 10000)
            a+=45
    }}

    val bp2 = BehaviorProxy{OneShotBehavior{
      var a = 1 
      for ( i<-1 to 10000)
            for ( j<-1 to 1000)
              a+=25
    }}
    val listBp = List(bp1,bp2)
    var beRef = TestActorRef(new ParralelBehavior(listBp),"parrallelBehavior3")
    
    beRef ! Setup()
    beRef ! Run
    expectMsg(Finished)

  }  
}

}
import behavior._
import akka.actor._
import scala.concurrent.duration._

class myActor extends Actor
{
  def receive = {
    case _ => println("a")
  }
}

object Main extends App {
 val system = ActorSystem("system")
 

 
}

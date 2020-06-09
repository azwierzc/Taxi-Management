package actors
import akka.actor.{Actor, ActorRef}
import com.teamg.taxi.Order
import com.teamg.taxi.Ping
//case object Ping
//case object Pong

/*agent representing course order */
class OrderActor(manager: ActorRef) extends Actor {
  var countDown = 100

      def receive = {
        case Ping =>
          println(s"${self.path} zamowienie przyjete")
          manager ! Order
      }


}

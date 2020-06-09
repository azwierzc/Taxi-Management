package actors
import akka.actor.{Actor, ActorRef}
import com.teamg.taxi.Order
import com.teamg.taxi.NewOrder


/*agent representing course order */
class OrderActor(manager: ActorRef) extends Actor {
  var countDown = 100

      def receive = {
        case NewOrder =>
          println(s"${self.path} zamowienie przyjete")
          manager ! Order
      }


}

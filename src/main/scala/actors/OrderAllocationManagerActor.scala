package actors

import akka.actor.{Actor, PoisonPill}
import com.teamg.taxi.Ping
import com.teamg.taxi.Order
import com.teamg.taxi.ReportLocation

class OrderAllocationManagerActor extends Actor {

  var countDown = 100
  def receive = {
    case Order =>
      println(s"${self.path} manager otrzymal zamowienie, count down $countDown")

      if (countDown > 0) {
        countDown -= 1
        sender() ! Ping
      } else {
        sender() ! PoisonPill
        self ! PoisonPill
      }
    case ReportLocation =>
      println(s"${self.path} manager otrzymal lokalizacje")
  }

}


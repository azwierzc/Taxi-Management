package com.teamg.taxi.core.actors

import java.time.{Clock, ZoneId}

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props, Timers}
import akka.http.scaladsl.Http
import akka.stream.Materializer
import com.teamg.taxi.core.actors.OrderAllocationManagerActor.messages.SendTaxis
import com.teamg.taxi.core.actors.TaxiSystemActor.messages.{StartM, StopM, UpdateTaxiLocationsM}
import com.teamg.taxi.core.actors.resource.ResourceActor
import com.teamg.taxi.core.actors.resource.ResourceActor.messages.UpdateLocationM
import com.teamg.taxi.core.factory.AkkaOrderDispatcher
import com.teamg.taxi.core.map.MapProvider
import com.teamg.taxi.core.model.{Taxi, TaxiType}
import com.teamg.taxi.core.service.OrderService

import scala.concurrent.duration._
import scala.util.{Failure, Success}

class TaxiSystemActor(taxiIdToNode: Map[String, Int]) extends Actor with ActorLogging with Timers {

  implicit val clock: Clock = Clock.system(ZoneId.of("Europe/Warsaw"))
  private val scale = 20
  private val orderAllocationManager = context.actorOf(Props(classOf[OrderAllocationManagerActor], clock))
  private lazy val taxiActors = createTaxiActors(taxiIdToNode)
  private val cityMap = MapProvider.default

  private implicit val system: ActorSystem = ActorSystem("TaxiSystemManagement")
  private implicit val materializer: Materializer = Materializer(context)

  import system.dispatcher

  private val orderService = new OrderService(orderDispatcher = new AkkaOrderDispatcher(orderAllocationManager))
  private val bindingFuture = Http().bindAndHandle(orderService.route, "localhost", 8080).andThen {
    case Success(_) => println("Bind success")
    case Failure(_) => println("Bind failure")
  }

  def receive: Receive = {
    case StartM =>
      log.debug("StartM")
      orderAllocationManager ! SendTaxis(taxiActors)
      timers.startTimerAtFixedRate("UpdateKey", UpdateTaxiLocationsM, 1.second)

    case UpdateTaxiLocationsM =>
      taxiActors.foreach(entry => entry._2 ! UpdateLocationM(scale))

    case StopM =>
      bindingFuture
        .flatMap(_.unbind()) // trigger unbinding from the port
        .onComplete(_ => system.terminate()) // and shutdown when done

  }

  private def createTaxiActors(taxiIdToNode: Map[String, Int]): Map[String, ActorRef] = {
    taxiIdToNode.map(p =>
      p._1 -> context.actorOf(Props(classOf[ResourceActor], clock, Taxi(p._1, TaxiType.Car), orderAllocationManager, cityMap.getNode(p._2)))
    ).toMap
  }

}

object TaxiSystemActor {

  object messages {

    case object StartM

    case object StopM

    case object UpdateTaxiLocationsM

  }

}

package com.teamg.taxi
import akka.actor.{Actor, ActorSystem, Props}

import language.postfixOps
import scala.concurrent.duration._
import actors.{OrderActor, OrderAllocationManagerActor}

class HelloActor extends Actor {
  def receive = {
    case "hello" => println("hello back at you")
    case _       => println("huh?")
  }
}

case class Location(lat: Float, long: Float)
case class Taxi(name: String, location: Location)
case object NewOrder
case object Order
case object Disposition
case object ReportLocation



object Main extends App {

  val system = ActorSystem("HelloSystem")
  // default Actor constructor
  val helloActor = system.actorOf(Props[HelloActor], name = "helloactor")
  helloActor ! "hello"
  helloActor ! "buenos dias"


  val system2 = ActorSystem("pingpong")

  val manager = system2.actorOf(Props[OrderAllocationManagerActor], "manager")
  val order = system2.actorOf(Props(classOf[OrderActor], manager), "order")
  //  val resource = system2.actorOf(Props(classOf[ResourceActor], manager), "order")



  import system.dispatcher
  system2.scheduler.scheduleOnce(500 millis) {
    order ! NewOrder
  }
}

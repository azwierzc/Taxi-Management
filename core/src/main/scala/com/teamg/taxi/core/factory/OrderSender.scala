package com.teamg.taxi.core.factory

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import com.teamg.taxi.core.service.OrderService.OrderRequest
import io.circe.syntax._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class OrderSender(url: Uri)
                 (implicit actorSystem: ActorSystem) {

  def send(orderRequest: OrderRequest)
          (implicit executionContext: ExecutionContext): Future[HttpResponse] = {
    Http().singleRequest(HttpRequest(
      method = HttpMethods.POST,
      uri = url,
      entity = HttpEntity(ContentTypes.`application/json`, orderRequest.asJson.noSpaces)
    )).andThen {
      case Success(response) => println(s"Order send success, statusCode:${response.status.intValue()}")
      case Failure(_) => println(s"Order send failure")
    }
  }

}

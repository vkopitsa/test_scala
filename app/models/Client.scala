package models

import play.api.libs.json._

case class Client(id: Long, name: String, phone: String)

object Client {
  implicit val personFormat = Json.format[Client]
}
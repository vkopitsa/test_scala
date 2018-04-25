package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class ClientRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class ClientTable(tag: Tag) extends Table[Client](tag, "clients") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def phone = column[String]("phone")
    def * = (id, name, phone) <> ((Client.apply _).tupled, Client.unapply)
  }

  private val client = TableQuery[ClientTable]

  def create(name: String, phone: String): Future[Client] = db.run {
    (client.map(p => (p.name, p.phone))
      returning client.map(_.id)
      into ((namePhone, id) => Client(id, namePhone._1, namePhone._2))
      ) += (name, phone)
  }

  def add(c: Client): Future[String] = {
    db.run(client += c)
      .map(res => "Client successfully added")
      .recover {
        case ex: Exception => ex.getCause.getMessage
      }
  }

  def list(): Future[Seq[Client]] = db.run {
    client.result
  }

  def saveOrUpdate(c: Client): Future[String] = {
    if (c.id == 0) {
      add(c)
    } else {
      db.run(client
        .filter(_.id === c.id)
        .update(c))
        .map(res => s"Client $c.name successfully updated")
        .recover {
          case ex: Exception => ex.getCause.getMessage
        }
    }
  }

  def get(id: Long): Future[Option[Client]] = {
    db.run(client
      .filter(_.id === id)
      .result
      .headOption)
  }

  def delete(id: Long): Future[Int] = {
    db.run(client
      .filter(_.id === id)
      .delete)
  }
}
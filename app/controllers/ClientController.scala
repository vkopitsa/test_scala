package controllers

import javax.inject._

import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class ClientController @Inject()(repo: ClientRepository,
                                 cc: MessagesControllerComponents
                                )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {


  val createForm: Form[CreateClientForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "phone" -> nonEmptyText
    )(CreateClientForm.apply)(CreateClientForm.unapply)
  }

  val updateForm = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "phone" -> nonEmptyText
    )(Client.apply)(Client.unapply)
  }

  def index = Action { implicit request =>
    Ok(views.html.index(createForm))
  }

  def add = Action.async { implicit request =>
    createForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.index(errorForm)))
      },
      client => {
        repo.create(client.name, client.phone).map { _ =>
          Redirect(routes.ClientController.index).flashing("success" -> "Client created")
        }
      }
    )
  }

  def save(id: Long) = Action.async { implicit request =>
    updateForm.fill(repo.get(id)).bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.edit(errorForm)))
      },
      client => {
        client.id = id
        repo.saveOrUpdate(client).map { _ =>
          Redirect(routes.ClientController.index).flashing("success" -> "Client created")
        }
      }
    )
  }

  def list = Action.async { implicit request =>
    repo.list().map { clients =>
      Ok(Json.toJson(clients))
    }
  }

  def get(id: Long) = Action.async { implicit request =>
    repo.get(id).map { client =>
      Ok(Json.toJson(client))
    }
  }

  def edit(id: Long) = Action.async { implicit request =>
    repo.get(id).map { client =>
      val form = updateForm.fill(client.get)
      Ok(views.html.edit(form))
    }
  }

  def delete(id: Long) = Action.async { implicit request =>
    repo.delete(id).map { client =>
      Ok(Json.toJson(client))
    }
  }

}

case class CreateClientForm(name: String, phone: String)
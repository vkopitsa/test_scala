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

  private val addUrl = routes.ClientController.addForm()
  private val editUrl = (id: Long) => routes.ClientController.editForm(id)
  private val deleteUrl = (id: Long) => routes.ClientController.delete(id)

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

  def addForm = Action { implicit request =>
    Ok(views.html.index(createForm))
  }

  def add = Action.async { implicit request =>
    createForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.index(errorForm)))
      },
      client => {
        repo.create(client.name, client.phone).map { _ =>
          Redirect(routes.ClientController.list).flashing("success" -> "Client created")
        }
      }
    )
  }

  def save(id:Long) = Action.async { implicit request =>
    updateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.edit(errorForm)))
      },
      success => {
        repo.saveOrUpdate(updateForm.bindFromRequest.get)
        Future.successful(Redirect(routes.ClientController.list).flashing("success" -> "Client updated"))
      }
    )
  }

  def list = Action.async { implicit request =>
    repo.list().map { clients =>

      Ok(views.html.list(clients, addUrl, editUrl, deleteUrl))
    }
  }

  def get(id: Long) = Action.async { implicit request =>
    repo.get(id).map { client =>
      Ok(Json.toJson(client))
    }
  }

  def editForm(id: Long) = Action.async { implicit request =>
    repo.get(id).map { client =>
      val form = updateForm.fill(client.get)
      Ok(views.html.edit(form))
    }
  }

  def delete(id: Long) = Action.async { implicit request =>
    repo.delete(id).map { client =>
      Redirect(routes.ClientController.list).flashing("success" -> "Client removed")
    }
  }

}

case class CreateClientForm(name: String, phone: String)
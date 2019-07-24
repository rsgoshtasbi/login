package Oauth

// Akka
import akka.actor.{Actor, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.http.javadsl.model.FormData
import akka.japi.Pair

// Json
import spray.json.JsonParser
import net.liftweb.json._

// Scala
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.concurrent._
import ExecutionContext.Implicits.global

// Json Implicit Definitions
case class UserJson(id: String, username: String, full_name: String)
case class Token(access_token: String, user: UserJson)

class Instagram {
  implicit val system: ActorSystem = ActorSystem("http-client")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val formats = DefaultFormats

  def requestAccessToken(code: String): Route = {
    try {
      val requestURI = "https://api.instagram.com/oauth/access_token"
      var oAuthToken = Await.result(postAuthCode(requestURI, code), 10.seconds)
      val tokenJson = parse(oAuthToken).extract[Token]
      complete(oAuthToken)
    } catch {
      case e: MappingException =>
        e.printStackTrace()
        complete(StatusCodes.InternalServerError, "No Access Token Recieved")
    }
  }

  def postAuthCode(uri: String, code: String): Future[String] = {
    val formData = FormData.create(
      Pair.create("client_id", ""),
      Pair.create("client_secret", ""),
      Pair.create("grant_type", "authorization_code"),
      Pair.create("redirect_uri", "http://users-api.us-east-1.elasticbeanstalk.com/instagram?"),
//      Pair.create("redirect_uri", "http://localhost:5000/instagram?"),
      Pair.create("code", code))
    val request = HttpRequest(HttpMethods.POST, uri).withEntity(formData.toEntity())

    for {
      response <- Http().singleRequest(request)
      content <- Unmarshal(response.entity).to[String]
    } yield content
  }

  def processRequest(requestData: String): Route = {
    val requestBody = JsonParser(requestData).asJsObject.fields.getOrElse("access_token", None)
    if(requestBody != None)
      complete(requestBody.toString)
    else
      complete(StatusCodes.InternalServerError, "No Body Data Returned")
  }
}

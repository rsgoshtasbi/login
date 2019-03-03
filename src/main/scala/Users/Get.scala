package Users

import Oauth.Instagram
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, extractUnmatchedPath, get, parameters, pathEndOrSingleSlash, pathPrefix}
import akka.http.scaladsl.server.Route

import scala.concurrent._

case class User(id: String)

class Get() extends Directives {
  val route: Route =
    get {
      pathEndOrSingleSlash {
        parameters('code) { code =>
          new Instagram().requestAccessToken(code)
        } ~
          complete(StatusCodes.BadRequest, "Invalid Authorization")
      } ~
      pathPrefix("users") {
        pathEndOrSingleSlash {
          complete("in /users")
        } ~
        extractUnmatchedPath { remaining =>
          complete("in /users/...somepath...")
        }
      }
    }
}
/*
extractStrictEntity(FiniteDuration.apply(0, "seconds"))  { entity =>
            val requestBodyData = entity.data.utf8String
            if(requestBodyData.nonEmpty)
              new Instagram().processRequest(requestBodyData)
            else
              complete(StatusCodes.InternalServerError, "No Body Data Provided")
          }
 */



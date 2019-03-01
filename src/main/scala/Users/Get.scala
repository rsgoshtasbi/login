package Users

import akka.http.javadsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives

case class User(id: String)

class Get() extends Directives {
  val route =
    get {
      pathEndOrSingleSlash {
        complete("error")
      } ~
      ignoreTrailingSlash {
        extractUnmatchedPath { remain =>
          complete(remain.toString.substring(1))
        }
      }
    }
}

import Oauth.Instagram
import Users.Get
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.FiniteDuration
import scala.io.StdIn

object Server {
  def main(args: Array[String]) {
    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher // needed for the future flatMap/onComplete in the end

    val userRoute = new Get().route
    val routes = userRoute
    val bindingFuture = Http().bindAndHandle(routes, "localhost", 5000)

    println(s"Server online at http://localhost:5000/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}

//          extractRequestEntity { entity =>
//            complete(s"${entity.toStrict(FiniteDuration.apply(0, "seconds")).map(_.getData.utf8String)}")
////            complete(s"${entity.toStrict(FiniteDuration.apply(0, "seconds")).map(_.data.utf8String)}")
//          }
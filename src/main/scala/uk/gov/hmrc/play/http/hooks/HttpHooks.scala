package uk.gov.hmrc.play.http.hooks

import uk.gov.hmrc.play.http.{HeaderCarrier, HttpResponse}

import scala.concurrent.Future

object HttpHooks {

  val hooks = Seq[() => Unit]()

}

trait HttpHooks extends HttpHooks {

  protected def executeHooks(url: String, verb: String, body: Option[_], responseToAuditF: Future[HttpResponse])(implicit hc: HeaderCarrier): Unit = {
    val request = HttpRequest(url, verb, body, now)

    hooks.forEach()

    responseToAuditF.map {
      response =>
        audit(request, response)
    }.recover {
      case e: Throwable => auditRequestWithException(request, e.getMessage)
    }
  }

}

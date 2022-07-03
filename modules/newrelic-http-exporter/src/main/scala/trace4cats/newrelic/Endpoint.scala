package trace4cats.newrelic

import org.http4s.Uri

sealed trait Endpoint {
  def uri: Uri
}
object Endpoint {
  private def standardUri(subdomain: String) = Uri.unsafeFromString(s"https://$subdomain.newrelic.com/trace/v1")

  case object US extends Endpoint {
    override val uri: Uri = standardUri("trace-api")
  }

  case object EU extends Endpoint {
    override val uri: Uri = standardUri("trace-api.eu")
  }

  case class Observer(uri: Uri) extends Endpoint
  object Observer {
    @deprecated("Use Observer(uri: Uri) constructor", since = "0.13.0")
    def apply(url: String): Observer = apply(Uri.unsafeFromString(url))
  }
}

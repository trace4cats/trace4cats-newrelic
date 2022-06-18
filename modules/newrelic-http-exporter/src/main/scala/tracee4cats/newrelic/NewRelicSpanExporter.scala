package tracee4cats.newrelic

import cats.Foldable
import cats.effect.kernel.Temporal
import io.circe.Json
import org.http4s.circe.CirceEntityCodec._
import org.http4s.client.Client
import org.http4s.headers.`Content-Type`
import org.http4s.{Header, MediaType}
import trace4cats.HttpSpanExporter
import trace4cats.kernel.SpanExporter
import trace4cats.model.Batch

object NewRelicSpanExporter {
  def apply[F[_]: Temporal, G[_]: Foldable](client: Client[F], apiKey: String, endpoint: Endpoint): SpanExporter[F, G] =
    HttpSpanExporter[F, G, Json](
      client,
      endpoint.uri,
      (batch: Batch[G]) => Convert.toJson(batch),
      List[Header.ToRaw](
        `Content-Type`(MediaType.application.json),
        "Api-Key" -> apiKey,
        "Data-Format" -> "newrelic",
        "Data-Format-Version" -> "1"
      )
    )
}

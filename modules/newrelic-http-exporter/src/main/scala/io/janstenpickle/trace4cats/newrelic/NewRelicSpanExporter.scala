package io.janstenpickle.trace4cats.newrelic

import cats.Foldable
import cats.effect.kernel.Temporal
import io.circe.Json
import io.janstenpickle.trace4cats.`export`.HttpSpanExporter
import io.janstenpickle.trace4cats.kernel.SpanExporter
import io.janstenpickle.trace4cats.model.Batch
import org.http4s.circe.CirceEntityCodec._
import org.http4s.client.Client
import org.http4s.headers.`Content-Type`
import org.http4s.{Header, MediaType}

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

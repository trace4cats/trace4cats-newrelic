package tracee4cats.newrelic

import cats.effect.kernel.{Async, Resource}
import fs2.Chunk
import org.http4s.client.Client
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import trace4cats.kernel.SpanCompleter
import trace4cats.model.TraceProcess
import trace4cats.{CompleterConfig, QueuedSpanCompleter}

object NewRelicSpanCompleter {
  def apply[F[_]: Async](
    client: Client[F],
    process: TraceProcess,
    apiKey: String,
    endpoint: Endpoint,
    config: CompleterConfig = CompleterConfig()
  ): Resource[F, SpanCompleter[F]] =
    Resource.eval(Slf4jLogger.create[F]).flatMap { implicit logger: Logger[F] =>
      QueuedSpanCompleter[F](process, NewRelicSpanExporter[F, Chunk](client, apiKey, endpoint), config)
    }
}

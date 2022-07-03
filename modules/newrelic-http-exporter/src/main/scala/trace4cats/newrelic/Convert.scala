package trace4cats.newrelic

import cats.Foldable
import cats.syntax.foldable._
import cats.syntax.show._
import io.circe.syntax._
import io.circe.{Encoder, Json}
import trace4cats.SemanticTags
import trace4cats.model.{AttributeValue, Batch, CompletedSpan}

import scala.collection.mutable.ListBuffer

// Based on API docs found here:
// https://docs.newrelic.com/docs/understand-dependencies/distributed-tracing/trace-api/report-new-relic-format-traces-trace-api
object Convert {
  implicit val traceValueEncoder: Encoder[AttributeValue] = Encoder.instance {
    case AttributeValue.StringValue(value) => Json.fromString(value.value)
    case AttributeValue.BooleanValue(value) => Json.fromBoolean(value.value)
    case AttributeValue.LongValue(value) => Json.fromLong(value.value)
    case AttributeValue.DoubleValue(value) => Json.fromDoubleOrString(value.value)
    case value: AttributeValue.AttributeList => Json.fromString(value.show)
  }

  def attributesJson(attributes: Map[String, AttributeValue]): Json =
    attributes.asJson

  def spanJson(span: CompletedSpan): Json =
    Json.obj(
      "trace.id" := span.context.traceId.show,
      "id" := span.context.spanId.show,
      "attributes" :=
        attributesJson(
          span.allAttributes ++ SemanticTags.kindTags(span.kind) ++ SemanticTags.statusTags("")(span.status) ++
            Map[String, AttributeValue](
              "duration.ms" -> AttributeValue.LongValue(span.end.toEpochMilli - span.start.toEpochMilli),
              "name" -> span.name
            ) ++ span.context.parent.map { parent =>
              "parent.id" -> AttributeValue.StringValue(parent.spanId.show)
            }.toMap
        )
    )

  def toJson[G[_]: Foldable](batch: Batch[G]): Json =
    List(Json.obj("spans" := batch.spans.foldLeft(ListBuffer.empty[Json]) { (buf, span) =>
      buf += spanJson(span)
    })).asJson
}

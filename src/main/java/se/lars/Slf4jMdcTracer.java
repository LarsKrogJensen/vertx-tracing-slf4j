package se.lars;

import io.vertx.core.Context;
import io.vertx.core.MultiMap;
import io.vertx.core.spi.tracing.SpanKind;
import io.vertx.core.spi.tracing.TagExtractor;
import io.vertx.core.spi.tracing.VertxTracer;
import io.vertx.core.tracing.TracingPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import static java.util.Optional.ofNullable;

public class Slf4jMdcTracer implements VertxTracer<Slf4jMdcTracer.Trace, Slf4jMdcTracer.Trace> {
  private static final Logger log = LoggerFactory.getLogger(Slf4jMdcTracer.class);

  @Override
  public <R> Trace receiveRequest(Context context,
                                  SpanKind kind,
                                  TracingPolicy policy,
                                  R request,
                                  String operation,
                                  Iterable<Map.Entry<String, String>> headers,
                                  TagExtractor<R> tagExtractor) {

    log.info("receiveRequest() request {} operation {}", request != null ? request.getClass().getName() : "null", operation);

    Trace trace = decode(headers);
    context.putLocal("trace", trace);
    MDC.put("trace", trace.id);
    return trace;
  }

  @Override
  public <R> void sendResponse(Context context, R response, Trace trace, Throwable failure, TagExtractor<R> tagExtractor) {
    log.info("sendResponse() trace {}", trace);
  }

  @Override
  public <R> Trace sendRequest(Context context,
                               SpanKind kind,
                               TracingPolicy policy,
                               R request,
                               String operation,
                               BiConsumer<String, String> headers,
                               TagExtractor<R> tagExtractor) {
    log.info("sendRequest() request {} operation {}", request != null ? request.getClass().getName() : "null", operation);
    Trace trace = ofNullable(context.<Trace>getLocal("trace")).orElseGet(Trace::new);
    headers.accept("trace-id", "" + trace.id);
    return trace;
  }

  @Override
  public <R> void receiveResponse(Context context, R response, Trace trace, Throwable failure, TagExtractor<R> tagExtractor) {
    log.info("receiveResponse() trace {}", trace);

    if (trace != null) {
      // need this???
      MDC.put("trace", trace.id);
      context.putLocal("trace", trace);
    }
  }

  private Trace decode(Iterable<Map.Entry<String, String>> headers) {
    String traceId = null;

    if (headers instanceof MultiMap) {
      MultiMap httpHeaders = (MultiMap) headers;
      traceId = httpHeaders.get("trace-id");
    } else {
      for (Map.Entry<String, String> header : headers) {
        if ("trace-id".equals(header.getKey())) {
          traceId = header.getValue();
          break;
        }
      }
    }
    return ofNullable(traceId).map(Trace::new).orElseGet(Trace::new);
  }

  public static class Trace {
    public final String id;

    Trace(String id) {
      this.id = id;
    }

    Trace() {
      this(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
      return "Trace{id='" + id + '\'' + '}';
    }
  }

}

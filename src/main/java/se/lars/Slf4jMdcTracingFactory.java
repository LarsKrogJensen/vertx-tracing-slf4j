package se.lars;

import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.VertxTracerFactory;
import io.vertx.core.spi.tracing.VertxTracer;
import io.vertx.core.tracing.TracingOptions;

public class Slf4jMdcTracingFactory implements VertxTracerFactory {
  @Override
  public VertxTracer tracer(TracingOptions options) {
    return new Slf4jMdcTracer();
  }

  @Override
  public TracingOptions newOptions() {
    return new TracingOptions();
  }

  @Override
  public TracingOptions newOptions(JsonObject jsonObject) {
    return new TracingOptions(jsonObject);
  }

}

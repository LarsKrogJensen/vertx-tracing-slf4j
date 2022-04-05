package se.lars;

import io.vertx.core.spi.VertxTracerFactory;
import io.vertx.core.tracing.TracingOptions;

public class Slf4jMdcTracingFactory implements VertxTracerFactory {
  @Override
  public Slf4jMdcTracer tracer(TracingOptions options) {
    return new Slf4jMdcTracer();
  }
}

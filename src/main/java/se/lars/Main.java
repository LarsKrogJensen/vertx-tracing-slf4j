package se.lars;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.tracing.TracingOptions;

import static java.lang.System.setProperty;

public class Main {

  public static void main(String[] args) {
    setProperty(
      "vertx.logger-delegate-factory-class-name",
      "io.vertx.core.logging.SLF4JLogDelegateFactory"
    );
    VertxOptions vertxOptions = new VertxOptions().setTracingOptions(new TracingOptions().setFactory(new Slf4jMdcTracingFactory()));
    var vertx = Vertx.vertx(vertxOptions);
    vertx.deployVerticle(new Server1Verticle());
    vertx.deployVerticle(new Server2Verticle());
    vertx.deployVerticle(new Service());
  }
}

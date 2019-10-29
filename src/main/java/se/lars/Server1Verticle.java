package se.lars;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server1Verticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(Server1Verticle.class);

  @Override
  public void start(Promise<Void> startPromise) {
    vertx.createHttpServer().requestHandler(req -> {
      log.info("Server1 handling request");
      vertx.eventBus().<String>request("service", "ignored", ar -> {
        log.info("Server1 got service response");
        req.response()
          .putHeader("content-type", "text/plain")
          .end(ar.result().body());
      });
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        log.info("HTTP server started on port {}", http.result().actualPort());
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}

package se.lars;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server2Verticle extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(Server2Verticle.class);

  @Override
  public void start(Promise<Void> startPromise) {
    vertx.createHttpServer().requestHandler(req -> {
      log.info("Server2 handling request with trace");
      req.response()
        .putHeader("content-type", "text/plain")
        .end(" hello from server 2!");
    }).listen(8889, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        log.info("HTTP server started on port {}", http.result().actualPort());
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}

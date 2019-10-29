package se.lars;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Service extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(Service.class);
  private WebClient client;
  @Override
  public void start(Promise<Void> startPromise) {
    vertx.eventBus().consumer("service", this::handleRequest);
    client = WebClient.create(vertx);
    log.info("Service 1 started");
    startPromise.complete();
  }

  private void handleRequest(Message<String> msg) {
    log.info("service 1 handling request");
    client.getAbs("http://localhost:8889").send(ar -> {
      log.info("service 1 got a response");
      msg.reply(ar.result().bodyAsString());
    });
  }
}

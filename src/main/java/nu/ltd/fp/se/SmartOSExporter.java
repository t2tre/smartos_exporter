package nu.ltd.fp.se;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class SmartOSExporter extends AbstractVerticle {

  private SmartOSExporter smartOSExporter;
  private String mainPage;

  @Override
  public void start(Future<Void> future) {
    initializeVars();
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    // Route / url
    router.route("/").handler(routingContext ->  {
      HttpServerResponse response = routingContext.response();
      response.putHeader("content-type", "text/html").end(mainPage);
    });

    // Route /metrics url

    // Set up HTTP server
    createHttpServer(future, router);
  }

  private void initializeVars() {
    smartOSExporter = this;
    mainPage = "<h1>SmartOS Node exporter</h1>\n<p><a href=\"/metrics\">Metrics</a></p>";
  }

  private void createHttpServer(Future<Void> future, Router router) {
    vertx
      .createHttpServer()
      .requestHandler(router::accept)
      .listen(
        config().getInteger(Constant.SERVICE_PORT, 8080),
        result -> {
          if (result.succeeded()) {
            future.complete();
          } else {
            future.fail(result.cause());
          }
        }
      );
  }

}

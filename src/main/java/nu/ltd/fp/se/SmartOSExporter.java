package nu.ltd.fp.se;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class SmartOSExporter extends AbstractVerticle {

  private SmartOSExporter smartOSExporter;
  private String mainPage;
  private SystemMetrics systemMetrics;
  private MetricsHandler metricsHandler;

  @Override
  public void start(Future<Void> future) {
    initializeVars();
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    // Route / url
    router.route(Constant.ROOT_PATH).handler(routingContext ->  {
      HttpServerResponse response = routingContext.response();
      response.putHeader("content-type", "text/html").end(mainPage);
    });

    // Route /metrics url
    router.get(Constant.METRICS_PATH).handler(smartOSExporter::handleMetricsRequest);

    // Set up HTTP server
    createHttpServer(future, router);
  }

  private void initializeVars() {
    smartOSExporter = this;
    mainPage = "<h1>SmartOS Node exporter</h1>\n<p><a href=\"/metrics\">Metrics</a></p>";
    systemMetrics = new SystemMetrics();
    metricsHandler = new MetricsHandler();
  }

  private void createHttpServer(Future<Void> future, Router router) {
    vertx
      .createHttpServer()
      .requestHandler(router::accept)
      .listen(
        config().getInteger(Constant.SERVICE_PORT, Constant.LISTEN_PORT),
        result -> {
          if (result.succeeded()) {
            future.complete();
          } else {
            future.fail(result.cause());
          }
        }
      );
  }

  private void handleMetricsRequest(RoutingContext routingContext) {
    systemMetrics.pollCollectorChain();
    metricsHandler.handle(routingContext);
  }

}

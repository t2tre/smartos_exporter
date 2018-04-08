package nu.ltd.fp.se;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

/**
 * See https://github.com/prometheus/client_java/blob/master/simpleclient_vertx/src/main/java/io/prometheus/client/vertx/MetricsHandler.java
 */

public class MetricsHandler implements Handler<RoutingContext> {
  private static class BufferWriter extends Writer {

    private final Buffer buffer = Buffer.buffer();

    @Override
    public void write(char[] characterBuffer, int off, int len) throws IOException {
      buffer.appendString(new String(characterBuffer, off, len));
    }

    @Override
    public void flush() throws IOException { }

    @Override
    public void close() throws IOException {  }

    Buffer getBuffer() {
      return buffer;
    }
  }

  private CollectorRegistry registry;

  public MetricsHandler() {
    this(CollectorRegistry.defaultRegistry);
  }

  public MetricsHandler(CollectorRegistry registry) {
    this.registry = registry;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    try {
      final BufferWriter writer = new BufferWriter();
      TextFormat.write004(writer, registry.filteredMetricFamilySamples(parse(routingContext.request())));
      routingContext.response()
        .setStatusCode(200)
        .putHeader("Content-Type", TextFormat.CONTENT_TYPE_004)
        .end(writer.getBuffer());
    } catch (IOException e) {
      routingContext.fail(e);
    }
  }

  private Set<String> parse(HttpServerRequest request) {
    return new HashSet(request.params().getAll("name[]"));
  }
}

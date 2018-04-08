package nu.ltd.fp.se;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import nu.ltd.fp.se.collector.MetricCollector;
import nu.ltd.fp.se.collector.SmartOSCollector;
import nu.ltd.fp.se.collector.UnameCollector;

public class SystemMetrics {

  private MetricCollector metricCollector;

  public SystemMetrics() {
    MetricCollector smartOSCollector = new SmartOSCollector();
    MetricCollector unameCollector = new UnameCollector(smartOSCollector);

    // this.metricCollector should be set to the last one
    this.metricCollector = unameCollector;
  }

  public void pollCollectorChain() {
    metricCollector.collectMetric();
  }
}

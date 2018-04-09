package nu.ltd.fp.se;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import nu.ltd.fp.se.collector.LoadAverageCollector;
import nu.ltd.fp.se.collector.MetricCollector;
import nu.ltd.fp.se.collector.SmartOSCollector;
import nu.ltd.fp.se.collector.UnameCollector;

public class SystemMetrics {

  private MetricCollector metricCollector;

  public SystemMetrics() {
    MetricCollector smartOSCollector = new SmartOSCollector();
    MetricCollector unameCollector = new UnameCollector(smartOSCollector);
    MetricCollector loadAverageCollector = new LoadAverageCollector(unameCollector);

    // this.metricCollector should be set to the last one
    this.metricCollector = loadAverageCollector;
  }

  public void pollCollectorChain() {
    metricCollector.collectMetric();
  }
}

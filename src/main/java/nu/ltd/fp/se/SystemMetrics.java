package nu.ltd.fp.se;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import nu.ltd.fp.se.collector.*;

public class SystemMetrics {

  private MetricCollector metricCollector;

  public SystemMetrics() {
    MetricCollector smartOSCollector = new SmartOSCollector();
    MetricCollector unameCollector = new UnameCollector(smartOSCollector);
    MetricCollector loadAverageCollector = new LoadAverageCollector(unameCollector);
    MetricCollector timeCollector = new TimeCollector(loadAverageCollector);
    MetricCollector meminfoCollector = new MeminfoCollector(timeCollector);

    // this.metricCollector should be set to the last one
    this.metricCollector = meminfoCollector;
  }

  public void pollCollectorChain() {
    metricCollector.collectMetric();
  }
}

package nu.ltd.fp.se.collector;

import io.prometheus.client.Gauge;
import nu.ltd.fp.se.Constant;

public class SmartOSCollector extends AbstractMetricCollector implements MetricCollector {
  static final Gauge unameGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "smartos_info")
    .help("Collector name")
    .labelNames("collector_name")
    .register();

  public SmartOSCollector() {
    unameGauge.labels("SmartOS Collector").set(1);
  }

  public SmartOSCollector(MetricCollector nextCollector) {
    this();
    this.setNextCollector(nextCollector);
  }

  public void collectMetric() {
    // Nothing to be done, call next collector
    if (this.getNextCollector() != null) {
      this.getNextCollector().collectMetric();
    }
  }
}

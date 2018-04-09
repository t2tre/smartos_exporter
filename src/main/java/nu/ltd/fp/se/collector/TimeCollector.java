package nu.ltd.fp.se.collector;

import io.prometheus.client.Gauge;
import nu.ltd.fp.se.Constant;

public class TimeCollector extends AbstractMetricCollector implements MetricCollector {
  static final Gauge systemTimeGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "time")
    .help("System time in seconds since epoch (1970).")
    .register();

  public TimeCollector() {
  }

  public TimeCollector(MetricCollector nextCollector) {
    this();
    this.setNextCollector(nextCollector);
  }

  private Double getSystemTime() {
    return new Double(System.currentTimeMillis() / 1000);
  }

  public void collectMetric() {
    systemTimeGauge.set(this.getSystemTime());

    // Call next collector
    if (this.getNextCollector() != null) {
      this.getNextCollector().collectMetric();
    }
  }
}

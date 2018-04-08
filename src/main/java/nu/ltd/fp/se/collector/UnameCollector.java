package nu.ltd.fp.se.collector;

import io.prometheus.client.Gauge;

public class UnameCollector extends AbstractMetricCollector implements MetricCollector {
  static final Gauge unameGauge = Gauge.build()
    .name("node_uname_info")
    .help("Labeled system information as provided by the uname system call")
    .labelNames("domainname", "machine", "nodename", "release", "sysname", "version")
    .register();

  public UnameCollector() {
    unameGauge.labels(this.runCommand("domainname"),
      System.getProperty("os.arch"),
      this.runCommand("hostname"),
      System.getProperty("os.version"),
      System.getProperty("os.name"),
      this.runCommand("uname -v")).set(1);
  }

  public UnameCollector(MetricCollector nextCollector) {
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

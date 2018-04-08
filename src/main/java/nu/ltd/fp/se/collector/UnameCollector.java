package nu.ltd.fp.se.collector;

import io.prometheus.client.Gauge;

public class UnameCollector extends AbstractMetricCollector implements MetricCollector {
  static final Gauge unameGauge = Gauge.build()
    .name("node_uname_info")
    .help("Labeled system information as provided by the uname system call")
    .labelNames("domainname", "machine", "nodename", "sysname", "version")
    .register();

  public UnameCollector() {
    unameGauge.labels("fp.ltd.nu", "somehostname", "x64", "l4", "l5").set(1);
  }

  public UnameCollector(MetricCollector nextCollector) {
    this();
    this.setNextCollector(nextCollector);
  }

  // node_uname_info{domainname="(none)",machine="x86_64",nodename="lnx-host-01",release="4.4.0-87-generic",sysname="Linux",version="#110-Ubuntu SMP Tue Jul 18 12:55:35 UTC 2017"} 1
  // node_uname_info{nodename="get",} 1.0
  public void collectMetric() {
    // Nothing to be done, call next collector
    if (this.getNextCollector() != null) {
      this.getNextCollector().collectMetric();
    }
  }
}

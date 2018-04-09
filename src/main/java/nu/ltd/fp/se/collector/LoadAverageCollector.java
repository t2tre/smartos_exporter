package nu.ltd.fp.se.collector;

import io.prometheus.client.Gauge;
import nu.ltd.fp.se.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LoadAverageCollector extends AbstractMetricCollector implements MetricCollector {
  static final Gauge load1mGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "load1")
    .help("1m load average.")
    .register();

  static final Gauge load5mGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "load5")
    .help("5m load average.")
    .register();

  static final Gauge load15mGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "load15")
    .help("15m load average.")
    .register();

  public LoadAverageCollector() { }

  public LoadAverageCollector(MetricCollector nextCollector) {
    this();
    this.setNextCollector(nextCollector);
  }

  private double getLoadAverage(Integer m) {
    File loadAverageFile = new File(Constant.LXPROC_PATH + "/loadavg");
    Double loadAverageValue = 0.0;
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(loadAverageFile));
      String line = null;
      while ((line = reader.readLine()) != null) {
        String[] tokens = line.split(" ");
        loadAverageValue = Double.parseDouble(tokens[m]);
      }
    } catch (Exception e) {
      loadAverageValue = 0.0;
    } finally {
      try { if (reader != null) { reader.close(); } } catch (Exception e) {}
    }
    return loadAverageValue;
  }

  public void collectMetric() {
    load1mGauge.set(getLoadAverage(0));
    load5mGauge.set(getLoadAverage(1));
    load15mGauge.set(getLoadAverage(2));
    // Nothing to be done, call next collector
    if (this.getNextCollector() != null) {
      this.getNextCollector().collectMetric();
    }
  }
}

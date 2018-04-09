package nu.ltd.fp.se.collector;

import io.prometheus.client.Gauge;
import nu.ltd.fp.se.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class StatMetricCollector extends AbstractMetricCollector implements MetricCollector {
  static final Gauge cpuGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "cpu")
    .help("Seconds the cpus spent in each mode.")
    .labelNames("cpu","mode")
    .register();

  private HashMap<String, HashMap<String, Double>> cpuStat;


  public StatMetricCollector() {
    this.cpuStat = new HashMap<String, HashMap<String, Double>>();
  }

  public StatMetricCollector(MetricCollector nextCollector) {
    this();
    this.setNextCollector(nextCollector);
  }

  private void pollStats() {
    File meminfoFile = new File(Constant.LXPROC_PATH + "/stat");
    BufferedReader reader = null;

    try {
      reader = new BufferedReader(new FileReader(meminfoFile));
      String line = null;
      int i = 0;
      // Skip the 1st line in the stat file
      while ((line = reader.readLine()) != null) {
        if (i > 0) {
          String[] tokens = line.split("\\s+");
          if (tokens[0].matches("^cpu\\d+")) {
            HashMap<String, Double> statsInfo = cpuStat.get(tokens[0]);
            if (statsInfo == null) { statsInfo = new HashMap<String, Double>(); }
            statsInfo.put("user", Double.parseDouble(tokens[1])/100.0);
            statsInfo.put("nice", Double.parseDouble(tokens[2])/100.0);
            statsInfo.put("system", Double.parseDouble(tokens[3])/100.0);
            statsInfo.put("idle", Double.parseDouble(tokens[4])/100.0);
            statsInfo.put("iowait", Double.parseDouble(tokens[5])/100.0);
            statsInfo.put("irq", Double.parseDouble(tokens[6])/100.0);
            statsInfo.put("softirq", Double.parseDouble(tokens[7])/100.0);
            cpuStat.put(tokens[0], statsInfo);
          }
        }
        i++;
      }
    } catch (Exception e) {
      System.out.println("got exception here.." + e.getMessage());
    } finally {
      try { if (reader != null) { reader.close(); } } catch (Exception e) {}
    }
  }

  public void collectMetric() {
    pollStats();
    for (Map.Entry<String, HashMap<String, Double>> entry : cpuStat.entrySet()) {
      String currentCpu = entry.getKey();
      HashMap<String, Double> currentCpuStats = entry.getValue();
      for (String stat : currentCpuStats.keySet()) {
        cpuGauge.labels(currentCpu, stat).set(currentCpuStats.get(stat));
      }
    }

    // Call next collector
    if (this.getNextCollector() != null) {
      this.getNextCollector().collectMetric();
    }
  }
}

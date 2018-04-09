package nu.ltd.fp.se.collector;

import io.prometheus.client.Gauge;
import nu.ltd.fp.se.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class MeminfoCollector extends AbstractMetricCollector implements MetricCollector {
  static final Gauge meminfoMemTotalGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "memory_MemTotal").help("Memory information field MemTotal.").register();

  static final Gauge meminfoMemFreeGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "memory_MemFree").help("Memory information field MemFree.").register();

  static final Gauge meminfoMemSharedGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "memory_MemShared").help("Memory information field MemShared.").register();

  static final Gauge meminfoBuffersGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "memory_Buffers").help("Memory information field Buffers.").register();

  static final Gauge meminfoCachedGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "memory_Cached").help("Memory information field Cached.").register();

  static final Gauge meminfoSwapCachedGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "memory_SwapCached").help("Memory information field SwapCached.").register();

  static final Gauge meminfoActiveGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "memory_Active").help("Memory information field Active.").register();

  static final Gauge meminfoInactiveGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "memory_Inactive").help("Memory information field Inactive.").register();

  static final Gauge meminfoHighTotalGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "memory_HighTotal").help("Memory information field HighTotal.").register();

  static final Gauge meminfoHighFreeGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "memory_HighFree").help("Memory information field HighFree.").register();

  static final Gauge meminfoLowTotalGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "memory_LowTotal").help("Memory information field LowTotal.").register();

  static final Gauge meminfoLowFreeGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "memory_LowFree").help("Memory information field LowFree.").register();

  static final Gauge meminfoSwapTotalGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "memory_SwapTotal").help("Memory information field SwapTotal.").register();

  static final Gauge meminfoSwapFreeGauge = Gauge.build()
    .name(Constant.EXPORTER_NAMESPACE + "memory_SwapFree").help("Memory information field SwapFree.").register();

  private HashMap<String, Double> memInfo;

  public MeminfoCollector() {
    this.memInfo = new HashMap<String, Double>();
  }

  public MeminfoCollector(MetricCollector nextCollector) {
    this();
    this.setNextCollector(nextCollector);
  }

  private void pollMemInfo() {
    Double fieldValue = 0.0;
    File meminfoFile = new File(Constant.LXPROC_PATH + "/meminfo");
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(meminfoFile));
      String line = null;
      int i = 0;
      // Skip the 1st 3 lines in the meminfo file
      while ((line = reader.readLine()) != null && i > 2) {
        String[] tokens = line.split(" ");
        String[] fieldNameTokens = tokens[0].split(":");
        memInfo.put(fieldNameTokens[0], Double.parseDouble(tokens[1]) * 1024);
        i++;
      }
    } catch (Exception e) {
    } finally {
      try { if (reader != null) { reader.close(); } } catch (Exception e) {}
    }
  }

  public void collectMetric() {
    pollMemInfo();

    meminfoMemTotalGauge.set(memInfo.get("MemTotal"));
    meminfoMemFreeGauge.set(memInfo.get("MemFree"));
    meminfoMemSharedGauge.set(memInfo.get("MemShared"));
    meminfoBuffersGauge.set(memInfo.get("Buffers"));
    meminfoCachedGauge.set(memInfo.get("Cached"));
    meminfoSwapCachedGauge.set(memInfo.get("SwapCached"));
    meminfoActiveGauge.set(memInfo.get("Active"));
    meminfoInactiveGauge.set(memInfo.get("Inactive"));
    meminfoHighTotalGauge.set(memInfo.get("HighTotal"));
    meminfoHighFreeGauge.set(memInfo.get("HighFree"));
    meminfoLowTotalGauge.set(memInfo.get("LowTotal"));
    meminfoLowFreeGauge.set(memInfo.get("LowFree"));
    meminfoSwapTotalGauge.set(memInfo.get("SwapTotal"));
    meminfoSwapFreeGauge.set(memInfo.get("SwapFree"));

    // Call next collector
    if (this.getNextCollector() != null) {
      this.getNextCollector().collectMetric();
    }
  }
}

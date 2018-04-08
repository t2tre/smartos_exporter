package nu.ltd.fp.se.collector;

public class AbstractMetricCollector {
  private MetricCollector nextCollector;

  public void setNextCollector(MetricCollector nextCollector) {
    this.nextCollector = nextCollector;
  }

  public MetricCollector getNextCollector() {
    return this.nextCollector;
  }
}

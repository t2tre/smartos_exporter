package nu.ltd.fp.se.collector;

public interface MetricCollector {
  public void collectMetric();
  public void setNextCollector(MetricCollector successor);
}

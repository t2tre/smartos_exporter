package nu.ltd.fp.se.collector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AbstractMetricCollector {
  private MetricCollector nextCollector;

  public void setNextCollector(MetricCollector nextCollector) {
    this.nextCollector = nextCollector;
  }

  public String runCommand(String command) {
    StringBuffer stdOut = new StringBuffer();
    try {
      Process p = Runtime.getRuntime().exec(command);
      BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String s = null;
      while ((s = stdInput.readLine()) != null) { stdOut.append(s); }
    } catch (IOException io) {
      stdOut.append(io.getMessage());
    }

    return stdOut.toString();
  }

  public MetricCollector getNextCollector() {
    return this.nextCollector;
  }
}

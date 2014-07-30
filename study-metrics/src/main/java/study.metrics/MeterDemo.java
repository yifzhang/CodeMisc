package study.metrics;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

public class MeterDemo {
    public static void main(String[] args){
        Meter requests = MetricsHolder.metrics.meter(MetricRegistry.name(MeterDemo.class, "requests"));
        while(true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                requests.mark();
        }
    }
}

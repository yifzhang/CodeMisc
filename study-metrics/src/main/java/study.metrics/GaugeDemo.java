package study.metrics;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import java.util.Random;

public class GaugeDemo {

    public static void main(String args[]){
        MetricsHolder.metrics.register(MetricRegistry.name(GaugeDemo.class, "gauge", "size"),
                new Gauge<Integer>() {
                    Random random = new Random();

                    @Override
                    public Integer getValue() {
                        return random.nextInt(1000);
                    }
                });
    }
}
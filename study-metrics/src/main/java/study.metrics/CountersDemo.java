package study.metrics;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

import java.util.Random;


public class CountersDemo {

    public static void main(String[] args){
        Counter pendingJobs = MetricsHolder.metrics.counter(MetricRegistry.name(CountersDemo.class,"pending-jobs"));
        Random random = new Random();
        while(true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            if(random.nextInt(100) > 30){
                pendingJobs.inc();
            } else {
                pendingJobs.dec();
            }
        }


    }
}

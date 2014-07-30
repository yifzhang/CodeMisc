package study.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import java.util.Random;


public class TimerDemo {

    public static void main(String[] args){
        Timer responses = MetricsHolder.metrics.timer(MetricRegistry.name(TimerDemo.class, "responses"));
        Random random = new Random();
        while(true){
            Timer.Context context = responses.time();
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            context.stop();
        }


    }
}

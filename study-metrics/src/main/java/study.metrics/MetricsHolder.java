package study.metrics;

import com.codahale.metrics.*;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MetricsHolder {
    public static final MetricRegistry metrics = new MetricRegistry();
    final static Graphite graphite = new Graphite(new InetSocketAddress("localhost", 2003));
    final static GraphiteReporter reporter = GraphiteReporter.forRegistry(metrics)
            .prefixedWith("example")
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .filter(MetricFilter.ALL)
            .build(graphite);
    static{
        reporter.start(1, TimeUnit.MINUTES);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Thread(){
            @Override
            public void run() {
                SortedMap<String, Gauge> gaugesMap = metrics.getGauges();
                Iterator<String> gaugesIt = gaugesMap.keySet().iterator();
                System.out.println("########################################");
                System.out.println("#############   Gauges    ##############");
                System.out.println("########################################");
                while(gaugesIt.hasNext()){
                    String name = gaugesIt.next();
                    Gauge each = gaugesMap.get(name);
                    System.out.println(name + "\t:\t" + each.getValue());
                }
                System.out.println();

                SortedMap<String, Counter> counters = metrics.getCounters();
                Iterator<String> counterIt = counters.keySet().iterator();
                System.out.println("########################################");
                System.out.println("#############   Counters  ##############");
                System.out.println("########################################");
                while(counterIt.hasNext()){
                    String name = counterIt.next();
                    Counter each = counters.get(name);
                    System.out.println(name + "\t:\t" + each.getCount());
                }
                System.out.println();

                SortedMap<String,Histogram> histograms = metrics.getHistograms();
                Iterator<String> histogramsIt = histograms.keySet().iterator();
                System.out.println("########################################");
                System.out.println("#############  Histograms ##############");
                System.out.println("########################################");
                while(histogramsIt.hasNext()){
                    String name = histogramsIt.next();
                    Histogram each = histograms.get(name);
                    System.out.println(name + "\t:\t" + each.getCount() + "\t:\t" + each.getSnapshot());
                }
                System.out.println();

                SortedMap<String,Meter> meters = metrics.getMeters();
                Iterator<String> metersIt = meters.keySet().iterator();
                System.out.println("########################################");
                System.out.println("##############  Metrics ################");
                System.out.println("########################################");
                while(metersIt.hasNext()){
                    String name = metersIt.next();
                    Meter each = meters.get(name);
                    System.out.println(name + "\t:\t" + each.getCount() + "\t:\t" + each.getFifteenMinuteRate() + "\t:\t" + each.getFiveMinuteRate() + "\t:\t" + each.getMeanRate() + "\t:\t" + each.getOneMinuteRate());
                }
                System.out.println();

                SortedMap<String, Timer> timers = metrics.getTimers();
                Iterator<String> timersIt = timers.keySet().iterator();
                System.out.println("########################################");
                System.out.println("##############  Timers ################");
                System.out.println("########################################");
                while(timersIt.hasNext()){
                    String name = timersIt.next();
                    Timer each = timers.get(name);
                    System.out.println(name + "\t:\t" + each.getCount() + "\t:\t" + each.getFifteenMinuteRate() + "\t:\t" + each.getFiveMinuteRate() + "\t:\t" + each.getMeanRate() + "\t:\t" + each.getOneMinuteRate() + "\t:\t" + each.getSnapshot());
                }
                System.out.println();
                System.out.println("--------------------------------------------------------------------------------------");
                System.out.println();
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
}

package study.archaius;

import com.netflix.config.*;
import com.netflix.config.source.ZooKeeperConfigurationSource;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import com.netflix.curator.utils.DebugUtils;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;


public class ZookeeperConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperConfiguration.class);

    private static final String CONFIG_ROOT_PATH = "/smc/config";
    private static CuratorFramework client;
    public static ZooKeeperConfigurationSource zkConfigSource;
    private static String zkServer = "localhost:2181";
    private static final Charset charset = Charset.forName("UTF-8");
    static{
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void init() throws Exception {
        System.setProperty(DebugUtils.PROPERTY_DONT_LOG_CONNECTION_ISSUES, "true");
        logger.info("Initialized local ZK with connect string [{}]", zkServer);

        client = CuratorFrameworkFactory.newClient(zkServer, new ExponentialBackoffRetry(1000, 3));
        client.start();

        zkConfigSource = new ZooKeeperConfigurationSource(client, CONFIG_ROOT_PATH);
        try {
            zkConfigSource.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final DynamicWatchedConfiguration zkDynamicOverrideConfig = new DynamicWatchedConfiguration(zkConfigSource);

        final ConcurrentCompositeConfiguration compositeConfig = new ConcurrentCompositeConfiguration();
        compositeConfig.addConfiguration(zkDynamicOverrideConfig, "zk dynamic override configuration");

        ConfigurationManager.install(compositeConfig);
    }


    private static void setZkProperty(String key, String value) throws Exception {
        final String path = CONFIG_ROOT_PATH + "/" + key;

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        zkConfigSource.addUpdateListener(new WatchedUpdateListener() {
            @Override
            public void updateConfiguration(WatchedUpdateResult result) {
                countDownLatch.countDown();
            }
        });
        byte[] data = value.getBytes(charset);

        try {
            // attempt to create (intentionally doing this instead of checkExists())
            client.create().creatingParentsIfNeeded().forPath(path, data);
        } catch (KeeperException.NodeExistsException exc) {
            // key already exists - update the data instead
            client.setData().forPath(path, data);
        }
        countDownLatch.await();
    }

    public static void main(String[] args) throws Exception {
//        init();
        System.out.println(DynamicPropertyFactory.getInstance()
                .getStringProperty("test.key1", "default").get());

        Thread.sleep(3000);

        System.out.println(DynamicPropertyFactory.getInstance()
                .getStringProperty("test.key1", "default").get());

        System.out.println("================================================");
        Thread.sleep(10000);

        System.out.println(DynamicPropertyFactory.getInstance()
                .getStringProperty("test.key1", "default").get());


        System.out.println(DynamicPropertyFactory.getInstance()
                .getStringProperty("test.key2", "default").get());
        System.out.println(DynamicPropertyFactory.getInstance()
                .getStringProperty("test.key3", "default").get());
        System.out.println(DynamicPropertyFactory.getInstance()
                .getStringProperty("test.key4", "default").get());
        System.out.println(DynamicPropertyFactory.getInstance()
                .getStringProperty("test.key5", "default").get());
        setZkProperty("test.key1", "value1");

        System.out.println(DynamicPropertyFactory.getInstance()
                .getStringProperty("test.key1", "default").get());

        ConfigurationManager.getConfigInstance().setProperty("test.key1", "setValue1");
    }

}

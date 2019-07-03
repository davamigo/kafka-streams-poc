package com.example.kafka.streams.poc.kafka.monitor;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Unit tests for KafkaStreamProcessesStatusMonitor class
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestKafkaStreamProcessesStatusMonitor {

    @Mock
    private StreamsBuilderFactoryBean bean1Mock;

    @Mock
    private StreamsBuilderFactoryBean bean2Mock;

    @Test
    public void testGetBeanQualifiersWhenNotEmpty() {

        Map<String, StreamsBuilderFactoryBean> beansMap = createBeansMap();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        List<String> qualifiers = monitor.getBeanQualifiers();

        Assert.assertEquals(2, qualifiers.size());
        Assert.assertTrue(qualifiers.contains("&bean1"));
        Assert.assertTrue(qualifiers.contains("&bean2"));
        Assert.assertFalse(qualifiers.contains("&bean3"));
    }

    @Test
    public void testGetBeanQualifiersWhenEmpty() {

        Map<String, StreamsBuilderFactoryBean> beansMap = new HashMap<>();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        List<String> qualifiers = monitor.getBeanQualifiers();

        Assert.assertEquals(0, qualifiers.size());
        Assert.assertFalse(qualifiers.contains("&bean1"));
    }

    @Test
    public void testGetBeanWhenExist() {

        Map<String, StreamsBuilderFactoryBean> beansMap = createBeansMap();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        Assert.assertNotNull(monitor.get("&bean1"));
    }

    @Test
    public void testGetBeanWhenNotExist() {

        Map<String, StreamsBuilderFactoryBean> beansMap = createBeansMap();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        Assert.assertNull(monitor.get("&bean3"));
    }

    @Test
    public void testSafeGetBeanWhenExist() throws BeanNotFoundException {

        Map<String, StreamsBuilderFactoryBean> beansMap = createBeansMap();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        Assert.assertNotNull(monitor.safeGet("&bean1"));
    }

    @Test(expected = BeanNotFoundException.class)
    public void testSafeGetBeanWhenNotExist() throws BeanNotFoundException {

        Map<String, StreamsBuilderFactoryBean> beansMap = createBeansMap();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        monitor.safeGet("&bean3");
    }

    @Test
    public void testIsRunningWhenIsRunning() throws BeanNotFoundException {

        Map<String, StreamsBuilderFactoryBean> beansMap = createBeansMap();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        Mockito.when(bean1Mock.isRunning()).thenReturn(true);

        Assert.assertTrue(monitor.isRunning("&bean1"));
    }

    @Test
    public void testIsRunningWhenIsStopeed() throws BeanNotFoundException {

        Map<String, StreamsBuilderFactoryBean> beansMap = createBeansMap();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        Mockito.when(bean1Mock.isRunning()).thenReturn(false);

        Assert.assertFalse(monitor.isRunning("&bean1"));
    }

    @Test(expected = BeanNotFoundException.class)
    public void testIsRunningWhenNoExist() throws BeanNotFoundException {

        Map<String, StreamsBuilderFactoryBean> beansMap = createBeansMap();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        monitor.isRunning("&bean3");
    }

    @Test
    public void testStartWhenExists() throws BeanNotFoundException {

        Map<String, StreamsBuilderFactoryBean> beansMap = createBeansMap();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        monitor.start("&bean1");

        Mockito.verify(bean1Mock, times(1)).start();
    }

    @Test(expected = BeanNotFoundException.class)
    public void testStartWhenNotExists() throws BeanNotFoundException {

        Map<String, StreamsBuilderFactoryBean> beansMap = createBeansMap();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        monitor.start("&bean3");
    }

    @Test
    public void testStopWhenExists() throws BeanNotFoundException {

        Map<String, StreamsBuilderFactoryBean> beansMap = createBeansMap();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        monitor.stop("&bean1");

        Mockito.verify(bean1Mock, times(1)).stop();
    }

    @Test(expected = BeanNotFoundException.class)
    public void testStopWhenNotExists() throws BeanNotFoundException {

        Map<String, StreamsBuilderFactoryBean> beansMap = createBeansMap();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        monitor.stop("&bean3");
    }

    @Test
    public void testStartAllWhenBeansRunning() {

        Map<String, StreamsBuilderFactoryBean> beansMap = createBeansMap();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        Mockito.when(bean1Mock.isRunning()).thenReturn(true);
        Mockito.when(bean2Mock.isRunning()).thenReturn(true);

        monitor.startAll();

        Mockito.verify(bean1Mock, times(0)).start();
        Mockito.verify(bean2Mock, times(0)).start();
    }

    @Test
    public void testStartAllWhenBeansNotRunning() {

        Map<String, StreamsBuilderFactoryBean> beansMap = createBeansMap();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        Mockito.when(bean1Mock.isRunning()).thenReturn(false);
        Mockito.when(bean2Mock.isRunning()).thenReturn(false);

        monitor.startAll();

        Mockito.verify(bean1Mock, times(1)).start();
        Mockito.verify(bean2Mock, times(1)).start();
    }

    @Test
    public void testStopAllWhenBeansRunning() {

        Map<String, StreamsBuilderFactoryBean> beansMap = createBeansMap();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        Mockito.when(bean1Mock.isRunning()).thenReturn(true);
        Mockito.when(bean2Mock.isRunning()).thenReturn(true);

        monitor.stopAll();

        Mockito.verify(bean1Mock, times(1)).stop();
        Mockito.verify(bean2Mock, times(1)).stop();
    }

    @Test
    public void testStopAllWhenBeansNotRunning() {

        Map<String, StreamsBuilderFactoryBean> beansMap = createBeansMap();
        KafkaStreamProcessesStatusMonitor monitor = new KafkaStreamProcessesStatusMonitor(beansMap);

        Mockito.when(bean1Mock.isRunning()).thenReturn(false);
        Mockito.when(bean2Mock.isRunning()).thenReturn(false);

        monitor.stopAll();

        Mockito.verify(bean1Mock, times(0)).stop();
        Mockito.verify(bean2Mock, times(0)).stop();
    }

    private Map<String, StreamsBuilderFactoryBean> createBeansMap() {
        Map<String, StreamsBuilderFactoryBean> beansMap = new HashMap<>();
        beansMap.put("&bean1", bean1Mock);
        beansMap.put("&bean2", bean2Mock);
        return beansMap;
    }
}

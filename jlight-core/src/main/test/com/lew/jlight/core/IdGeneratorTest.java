package com.lew.jlight.core;

import com.google.common.collect.Lists;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGeneratorTest {

    private CountDownLatch countDownLatch = new CountDownLatch(1000);
    private List list = Lists.newCopyOnWriteArrayList();

    @Test
    public void testNextId() throws Exception {
        String id =IdGenerator.getInstance().nextId();
        Assert.assertNotNull(id);
    }

    @Ignore
    @Test
    public void testMulThread() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(5000);
        for(int i=0;i<1000;i++){
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    String id = IdGenerator.getInstance().nextId();
                    list.add(id);
                    System.out.println(id);
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        System.out.println(list.size());
    }
}

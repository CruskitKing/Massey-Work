package tests.nz.ac.massey.cs.sdc.log4jassignment.s12110243;

import nz.ac.massey.cs.sdc.log4jassignment.s12110243.MemoryAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Created by cloud202 on 19/08/14.
 */
public class StressTest {

    @After
    public void teardown() {
        System.gc();
    }

    /*
     * This test will continuously append to the Memory Appender for 20 seconds.
     * This will fail if the JVM Heap becomes full
     */
    @Test
    public void testArrayGarbageCollection() throws InterruptedException {
        MemoryAppender ma = new MemoryAppender(new ArrayList<LoggingEvent>());
        Logger logger = Logger.getLogger("logger3");
        logger.addAppender(ma);
        for (long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(20); stop > System.nanoTime(); ) {
            logger.info("Test");
        }
        System.out.println(ma.getLogs().size());
    }

    @Test
    public void testLinkedGarbageCollection() throws InterruptedException {
        MemoryAppender ma = new MemoryAppender(new LinkedList<LoggingEvent>());
        Logger logger = Logger.getLogger("logger3");
        logger.addAppender(ma);
        for (long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(20); stop > System.nanoTime(); ) {
            logger.info("Test");
        }
        System.out.println(ma.getLogs().size());
    }
}
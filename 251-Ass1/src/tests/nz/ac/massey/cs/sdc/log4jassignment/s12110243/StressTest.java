package tests.nz.ac.massey.cs.sdc.log4jassignment.s12110243;

import nz.ac.massey.cs.sdc.log4jassignment.s12110243.MemoryAppender;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by cloud202 on 19/08/14.
 */
public class StressTest {

    /*
     * This test will continuously append to the Memory Appender for 20 seconds.
     * This will fail if the JVM Heap is full
     */
    @Test
    public void testGarbageCollection() throws InterruptedException {
        MemoryAppender ma = new MemoryAppender("ArrayList");
        Logger logger = Logger.getLogger("logger");
        logger.addAppender(ma);
        for (long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(20); stop > System.nanoTime(); ) {
            logger.info("Test");
        }
        System.out.println(ma.getLogs().size());
    }


}
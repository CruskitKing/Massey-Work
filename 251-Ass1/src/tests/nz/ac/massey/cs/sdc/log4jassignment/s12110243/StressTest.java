package tests.nz.ac.massey.cs.sdc.log4jassignment.s12110243;

import nz.ac.massey.cs.sdc.log4jassignment.s12110243.MemoryAppender;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by cloud202 on 19/08/14.
 */
public class StressTest {
    @Test
    public void testGarbageCollection() throws InterruptedException {
        MemoryAppender ma = new MemoryAppender(20);
        Logger logger = Logger.getLogger("logger");
        logger.addAppender(ma);
        for (long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(200); stop > System.nanoTime(); ) {
            logger.info("Test");
        }
        System.out.println(ma.getLogs().size());
    }
}
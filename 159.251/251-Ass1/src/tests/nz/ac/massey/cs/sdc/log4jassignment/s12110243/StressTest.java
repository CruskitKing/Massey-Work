package tests.nz.ac.massey.cs.sdc.log4jassignment.s12110243;

import nz.ac.massey.cs.sdc.log4jassignment.s12110243.MemoryAppender;
import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class StressTest {

    @After
    public void teardown() throws InterruptedException {
        System.gc();
        Thread.sleep(10);
    }

    /*
     * This test will continuously append to the Memory Appender for 20 seconds.
     * This will fail if the JVM Heap becomes full
     */
    @Test
    public void testArrayMemoryUsage() throws InterruptedException {
        MemoryAppender ma = new MemoryAppender(new ArrayList<LoggingEvent>());
        Logger logger = Logger.getLogger("1logger");
        logger.addAppender(ma);
        for (long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(20); stop > System.nanoTime(); ) {
            logger.info("Test");
        }
    }

    @Test
    public void testLinkedMemoryUsage() throws InterruptedException {
        MemoryAppender ma = new MemoryAppender(new LinkedList<LoggingEvent>());
        Logger logger = Logger.getLogger("2logger");
        logger.addAppender(ma);
        for (long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(20); stop > System.nanoTime(); ) {
            logger.info("Test");
        }
    }

    @Test
    public void testConsoleAppenderMemoryUsage() throws InterruptedException {
        ConsoleAppender ca = new ConsoleAppender();
        Logger logger = Logger.getLogger("8logger");
        logger.addAppender(ca);
        for (long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(20); stop > System.nanoTime(); ) {
            logger.info("Test");
        }
    }

    @Test
    public void testFileAppenderMemoryUsage() throws InterruptedException, IOException {
        FileAppender fa = new FileAppender(new TTCCLayout(),"logs.txt");
        Logger logger = Logger.getLogger("7logger");
        logger.addAppender(fa);
        for (long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(20); stop > System.nanoTime(); ) {
            logger.info("Test");
        }
    }

    @Test
    public void testLinkedListPerformance() {
        long start = System.nanoTime();
        MemoryAppender ma = new MemoryAppender(new LinkedList<LoggingEvent>());
        Logger logger = Logger.getLogger("3logger");
        logger.addAppender(ma);
        for (int i=0;i<20;i++) {
            logger.info("Test");
        }
        long before = System.nanoTime();
        for (int i=0;i<3000;i++) {
            logger.info("Test");
        }
        long after = System.nanoTime();
        System.out.println("\nLinked MemoryAppender");
        System.out.println("Setup: " + Long.toString(before-start));
        System.out.println("Complete: " + Long.toString(after-start));
    }

    @Test
    public void testArrayListPerformance() {
        long start = System.nanoTime();
        MemoryAppender ma = new MemoryAppender(new ArrayList<LoggingEvent>());
        Logger logger = Logger.getLogger("4logger");
        logger.addAppender(ma);
        for (int i=0;i<20;i++) {
            logger.info("Test");
        }
        long before = System.nanoTime();
        for (int i=0;i<3000;i++) {
            logger.info("Test");
        }
        long after = System.nanoTime();
        System.out.println("\nArray MemoryAppender");
        System.out.println("Setup: " + Long.toString(before-start));
        System.out.println("Complete: " + Long.toString(after-start));
    }

    @Test
    public void testConsoleAppenderPerformance() {
        long start = System.nanoTime();
        ConsoleAppender ca = new ConsoleAppender();
        Logger logger = Logger.getLogger("5logger");
        logger.addAppender(ca);
        for (int i=0;i<20;i++) {
            logger.info("Test");
        }
        long before = System.nanoTime();
        for (int i=0;i<3000;i++) {
            logger.info("Test");
        }
        long after = System.nanoTime();
        System.out.println("\nConsoleAppender");
        System.out.println("Setup: " + Long.toString(before-start));
        System.out.println("Complete: " + Long.toString(after-start));
    }

    @Test
    public void testFileAppenderPerformance() throws IOException {
        long start = System.nanoTime();
        FileAppender fa = new FileAppender(new TTCCLayout(),"logs.txt");
        Logger logger = Logger.getLogger("6logger");
        logger.addAppender(fa);
        for (int i=0;i<20;i++) {
            logger.info("Test");
        }
        long before = System.nanoTime();
        for (int i=0;i<3000;i++) {
            logger.info("Test");
        }
        long after = System.nanoTime();
        System.out.println("\nFileAppender");
        System.out.println("Setup: " + Long.toString(before-start));
        System.out.println("Complete: " + Long.toString(after-start));
    }
}
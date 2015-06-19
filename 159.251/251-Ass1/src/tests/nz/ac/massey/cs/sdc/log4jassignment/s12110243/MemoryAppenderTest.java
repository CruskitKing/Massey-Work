package tests.nz.ac.massey.cs.sdc.log4jassignment.s12110243;

import jdk.nashorn.internal.runtime.Logging;
import nz.ac.massey.cs.sdc.log4jassignment.s12110243.MemoryAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class MemoryAppenderTest {

    @Test
    public void testCreateArrayListAppender() {
        MemoryAppender ma = new MemoryAppender(new ArrayList<LoggingEvent>());
        assertTrue(ma.getSecondLogs().getClass() == ArrayList.class);
    }

    @Test
    public void testCreateLinkedListAppender() {
        MemoryAppender ma = new MemoryAppender(new LinkedList<LoggingEvent>());
        assertTrue(ma.getSecondLogs().getClass() == LinkedList.class);
    }

    /*
     * This test is to show that the logs in MemoryAppender cannot be adjusted when obtained by the method MemoryAppender.getLogs()
     */
    @Test
    public void testGetLogs() {
        MemoryAppender ma = new MemoryAppender(new ArrayList<LoggingEvent>());
        Logger logger = Logger.getLogger("logger");
        logger.addAppender(ma);
        logger.info("Test");
        List<LoggingEvent> log = ma.getLogs();
        log.add(new LoggingEvent("logger",logger, Level.INFO,"Test",new Throwable()));
        assertTrue(log.size() > ma.getLogs().size());
    }

    /*
     * This test shows that when the primary log reaches maxSize, it will move logs to secondLogs
     */
    @Test
    public void testMaxSize() {
        MemoryAppender ma = new MemoryAppender(new ArrayList<LoggingEvent>());
        Logger logger = Logger.getLogger("logger1");
        logger.addAppender(ma);
        for (int i = 0; i < ma.getMaxSize() + 5; i++) {
            logger.info("Test");
        }
        assertTrue(ma.getSecondLogs().size() == 20 && ma.getLogs().size() == 25);
    }

    /*
     * This test is to show that a closed appender can no longer be appended to
     */
    @Test (expected = UnsupportedOperationException.class)
    public void testCloseAppender() {
        MemoryAppender ma = new MemoryAppender(new ArrayList<LoggingEvent>());
        Logger logger = Logger.getLogger("logger2");
        logger.addAppender(ma);
        ma.close();
        logger.info("Test");
    }
}

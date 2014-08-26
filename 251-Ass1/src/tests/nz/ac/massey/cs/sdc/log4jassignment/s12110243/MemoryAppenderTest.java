package tests.nz.ac.massey.cs.sdc.log4jassignment.s12110243;

import nz.ac.massey.cs.sdc.log4jassignment.s12110243.MemoryAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by cloud202 on 19/08/14.
 */
public class MemoryAppenderTest {

    @Test
    public void testCreateArrayListAppender() {
        MemoryAppender ma = new MemoryAppender("ArrayList");
        assertTrue(ma.getSecondLogs().getClass() == ArrayList.class);
    }

    @Test
    public void testCreateLinkedListAppender() {
        MemoryAppender ma = new MemoryAppender("LinkedList");
        assertTrue(ma.getSecondLogs().getClass() == LinkedList.class);
    }

    /*
     * This test is to show that the logs in MemoryAppender cannot be adjusted when obtained by the method MemoryAppender.getLogs()
     */
    @Test
    public void testGetLogs() {
        MemoryAppender ma = new MemoryAppender("ArrayList");
        Logger logger = Logger.getLogger("logger");
        logger.addAppender(ma);
        logger.info("Test");
        List<WeakReference<LoggingEvent>> log = ma.getLogs();
        log.add(new WeakReference<LoggingEvent>(new LoggingEvent("logger",logger, Level.INFO,"Test",new Throwable())));
        assertTrue(log.size() > ma.getLogs().size());
    }

    /*
     * This test is to show that a closed appender can no longer be appended to
     */
    @Test (expected = UnsupportedOperationException.class)
    public void testCloseAppender() {
        MemoryAppender ma = new MemoryAppender("ArrayList");
        Logger logger = Logger.getLogger("logger");
        logger.addAppender(ma);
        ma.close();
        logger.info("Test");
    }
}

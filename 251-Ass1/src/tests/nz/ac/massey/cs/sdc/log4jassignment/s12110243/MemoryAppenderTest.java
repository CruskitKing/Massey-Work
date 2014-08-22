package tests.nz.ac.massey.cs.sdc.log4jassignment.s12110243;

import nz.ac.massey.cs.sdc.log4jassignment.s12110243.MemoryAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by cloud202 on 19/08/14.
 */
public class MemoryAppenderTest {

    @Test
    public void testGetLogs() throws InterruptedException {
        MemoryAppender ma = new MemoryAppender(20);
        Logger logger = Logger.getLogger("logger");
        logger.addAppender(ma);
        logger.info("Test");
        List<WeakReference<LoggingEvent>> log = ma.getLogs();
//        log.add(new WeakReference<LoggingEvent>(new LoggingEvent()));
        assertTrue(log.size() > ma.getLogs().size());
    }

    @Test
    public void testCloseAppender() {
        MemoryAppender ma = new MemoryAppender(20);
        Logger logger = Logger.getLogger("logger");
        logger.addAppender(ma);
        ma.close();
        logger.info("Test");
    }
}

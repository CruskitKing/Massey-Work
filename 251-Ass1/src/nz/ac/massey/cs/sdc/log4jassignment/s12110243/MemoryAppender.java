package nz.ac.massey.cs.sdc.log4jassignment.s12110243;

import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class MemoryAppender extends AppenderSkeleton{

    private int maxSize = 20;
    private Boolean closed = false;
    private List<LoggingEvent> logs;
    private List<LoggingEvent> secondLogs;
    private Runtime runtime = Runtime.getRuntime();
    private MVELLayout layout = new MVELLayout();

    public MemoryAppender(List<LoggingEvent> list) {
        super();
        try {
            this.logs = list;
            this.secondLogs = list.getClass().newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<LoggingEvent> getLogs() {
        try {
            List<LoggingEvent> log = this.logs.getClass().newInstance();
            log.addAll(this.secondLogs);
            log.addAll(this.logs);
            return log;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        setLogs(null);
        setSecondLogs(null);
        setClosed(true);
    }

    public boolean requiresLayout() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void append(LoggingEvent log){
        if (!getClosed()) {
            if ((float)runtime.totalMemory()/runtime.maxMemory() > 0.8) {
                for (int i=0;i<this.secondLogs.size()/10;i++) {
                    this.secondLogs.remove(this.secondLogs.size()-1);
                }
                try {
                    System.gc();
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (this.logs.size() >= this.maxSize) { // move logs to secondLogs if maxSize is reached
                this.secondLogs.addAll(this.logs);
                this.logs.clear();
            }
            this.logs.add(log);
//            System.out.println(layout.evaluate(log));
        } else {
            throw new UnsupportedOperationException("Appender is closed");
        }
    }

    public Integer getMaxSize() { return maxSize; }
    public void setMaxSize(Integer maxSize) { this.maxSize = maxSize; }
    public void setLogs(List<LoggingEvent> logs) { this.logs = logs; }
    public Boolean getClosed() { return closed; }
    public void setClosed(Boolean closed) { this.closed = closed; }
    public List<LoggingEvent> getSecondLogs() { return secondLogs; }
    public void setSecondLogs(List<LoggingEvent> secondLogs) { this.secondLogs = secondLogs; }
}
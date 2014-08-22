package nz.ac.massey.cs.sdc.log4jassignment.s12110243;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class MemoryAppender extends AppenderSkeleton{

    private int maxSize;
    private String listType;
    private Boolean closed = false;
    private List<WeakReference<LoggingEvent>> logs = new ArrayList<WeakReference<LoggingEvent>>();
    private List<WeakReference<LoggingEvent>> secondLogs = new ArrayList<WeakReference<LoggingEvent>>();

    public MemoryAppender(int maxSize) {
        super();
        this.maxSize = maxSize;
//        HashMap<String,List<WeakReference<LoggingEvent>>> hm = new HashMap<String, List<WeakReference<LoggingEvent>>>();
//        hm.put("ArrayList", new ArrayList<WeakReference<LoggingEvent>>());
//        hm.put("LinkedList", new LinkedList<WeakReference<LoggingEvent>>());
//        setLogs(); //TODO
//        setSecondLogs(hm.get(listType));
    }

    public List<WeakReference<LoggingEvent>> getLogs() {
        List<WeakReference<LoggingEvent>> log = new ArrayList<WeakReference<LoggingEvent>>(this.secondLogs);
        log.addAll(logs);
        return log;
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
            try {
                int size = getLogs().size();
                if (this.logs.size() < this.getMaxSize()) {
                    WeakReference<LoggingEvent> wLog = new WeakReference<LoggingEvent>(log);
                    this.logs.add(wLog);
                } else {
                    for (WeakReference<LoggingEvent> o : this.logs) {
                        this.secondLogs.add(o);
                        System.gc();
                        Thread.sleep(10);
                    }
                    this.logs.clear();
                    WeakReference<LoggingEvent> wLog = new WeakReference<LoggingEvent>(log);
                    this.logs.add(wLog);
                }
                System.gc();
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            throw new UnsupportedOperationException("Appender is closed");
        }
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }
    public Integer getMaxSize() {
        return maxSize;
    }
    public void setLogs(List<WeakReference<LoggingEvent>> logs) { this.logs = logs; }
    public Boolean getClosed() { return closed; }
    public void setClosed(Boolean closed) { this.closed = closed; }
    public List<WeakReference<LoggingEvent>> getSecondLogs() { return secondLogs; }
    public void setSecondLogs(List<WeakReference<LoggingEvent>> secondLogs) { this.secondLogs = secondLogs; }
}
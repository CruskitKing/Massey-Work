package nz.ac.massey.cs.sdc.log4jassignment.s12110243;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.mvel2.templates.TemplateRuntime;

import java.util.Date;
import java.util.HashMap;

public class MVELLayout extends PatternLayout {
    public MVELLayout() {
    }

    public MVELLayout(String pattern) {
        super(pattern);
    }

    @Override
    public String format(LoggingEvent event) {
        HashMap<String,String> vars = new HashMap<String,String>();
        vars.put("level",event.getLevel().toString());
        vars.put("loggerName",event.getLoggerName());
        vars.put("message",event.getMessage().toString());
        vars.put("timestamp",new Date(event.getTimeStamp()).toString());
        return (String) TemplateRuntime.eval(this.getConversionPattern(),vars);
    }
}

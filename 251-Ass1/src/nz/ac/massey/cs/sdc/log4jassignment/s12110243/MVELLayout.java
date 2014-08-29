package nz.ac.massey.cs.sdc.log4jassignment.s12110243;

import org.apache.log4j.spi.LoggingEvent;
import org.mvel2.templates.TemplateRuntime;

import java.util.Date;
import java.util.HashMap;

public class MVELLayout {

    private String template = "@{timestamp} [@{level}] @{loggerName} - @{message}";

    public MVELLayout() {
    }

    public String evaluate(LoggingEvent event) {
        HashMap vars = new HashMap<String,String>();
        vars.put("level",event.getLevel().toString());
        vars.put("loggerName",event.getLoggerName());
        vars.put("message",event.getMessage().toString());
        vars.put("timestamp",new Date(event.getTimeStamp()));
        return (String) TemplateRuntime.eval(template,vars);
    }

    public String getTemplate() { return template; }
    public void setTemplate(String template) { this.template = template; }
}
package nz.ac.massey.cs.sdc.log4jassignment.s12110243;

import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

/**
 * Created by mitchell on 27/08/14.
 */
public class MVELLayout {

    private String template;
    private CompiledTemplate compiled;

    public MVELLayout(String template) {
        this.template = template;
        this.compiled = TemplateCompiler.compileTemplate(template);
    }

    public String execute() {
        compile();
        return (String) TemplateRuntime.execute(compiled);
    }
    public void compile() {
        compiled = TemplateCompiler.compileTemplate(template);
    }

    public String getTemplate() {
        return template;
    }
    public void setTemplate(String template) {
        this.template = template;
    }
    public CompiledTemplate getCompiled() {
        return compiled;
    }
    public void setCompiled(CompiledTemplate compiled) {
        this.compiled = compiled;
    }
}

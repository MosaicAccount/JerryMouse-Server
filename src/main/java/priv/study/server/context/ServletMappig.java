package priv.study.server.context;

import jakarta.servlet.Servlet;

import java.util.regex.Pattern;

/**
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public class ServletMappig {

    private Pattern pattern;

    private Servlet servlet;

    public ServletMappig(String pattern, Servlet servlet) {
        this.pattern = Pattern.compile(pattern);
        this.servlet = servlet;
    }

    public Servlet match(String path) {
        if (pattern.matcher(path).matches()) {
            return servlet;
        }
        return null;
    }
}

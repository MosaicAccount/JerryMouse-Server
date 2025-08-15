package priv.study.server.context;

import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;

import java.util.regex.Pattern;

/**
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public class FilterMapping {

    private final Pattern pattern;

    private final Filter filter;

    public FilterMapping(String pattern, Filter filter) {
        this.pattern = Pattern.compile(pattern);
        this.filter = filter;
    }

    public Filter match(String path) {
        if (pattern.matcher(path).matches()) {
            return filter;
        }
        return null;
    }
}

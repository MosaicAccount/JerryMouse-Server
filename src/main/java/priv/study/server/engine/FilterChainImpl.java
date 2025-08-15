package priv.study.server.engine;

import jakarta.servlet.*;

import java.io.IOException;

/**
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public class FilterChainImpl implements FilterChain {

    private int index;
    private final Filter[] filters;
    private final Servlet servlet;
    private final int total;

    public FilterChainImpl(Filter[] filters, Servlet servlet) {
        this.filters = filters;
        this.servlet = servlet;
        this.total = filters.length;
        this.index = 0;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {

        if (index < total) {
            Filter filter = filters[index];
            index++;
            filter.doFilter(request, response, this);
        } else {
            servlet.service(request, response);
        }
    }
}

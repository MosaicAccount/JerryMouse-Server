package priv.study.server.engine;

import jakarta.servlet.*;

import java.io.IOException;

/**
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public class FilterChainImpl implements FilterChain {

    private int index = 0;
    private final Filter[] filters;
    private final Servlet servlet;

    public FilterChainImpl(Filter[] filters, Servlet servlet) {
        this.filters = filters;
        this.servlet = servlet;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {

        if (index < filters.length) {
            Filter filter = filters[index];
            index++;
            filter.doFilter(request, response, this);
        } else {
            servlet.service(request, response);
        }
    }
}

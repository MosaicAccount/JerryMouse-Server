package priv.study.server.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
@WebFilter(filterName = "helloFilter", urlPatterns = {"/hello", "/noHello"})
public class HelloFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(HelloFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("经过了 helloFilter");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

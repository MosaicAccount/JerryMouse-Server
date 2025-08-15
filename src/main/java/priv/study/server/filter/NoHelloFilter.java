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
@WebFilter(filterName = "noHelloFilter", urlPatterns = {"/noHello"})
public class NoHelloFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(NoHelloFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("经过了 noHelloFilter");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

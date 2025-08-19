package priv.study.server.connector;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import priv.study.server.context.ServletContextImpl;
import priv.study.server.engine.HttpServletRequestImpl;
import priv.study.server.engine.HttpServletResponseImpl;
import priv.study.server.filter.BasicFilter;
import priv.study.server.filter.HelloFilter;
import priv.study.server.filter.NoHelloFilter;
import priv.study.server.servlet.HelloServletImpl;
import priv.study.server.servlet.LoginServlet;
import priv.study.server.servlet.NoHelloServletImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * 处理 http 请求
 *
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public class HttpConnector implements HttpHandler, AutoCloseable {

    private final HttpServer httpServer;
    private final ServletContextImpl servletContext;

    private final Logger logger = LoggerFactory.getLogger(HttpConnector.class);

    public HttpConnector() throws IOException {
        servletContext = new ServletContextImpl();
        servletContext.initialize(List.of(HelloServletImpl.class, NoHelloServletImpl.class, LoginServlet.class), List.of(NoHelloFilter.class, HelloFilter.class, BasicFilter.class));
        String host = "0.0.0.0";
        int port = 9999;
        this.httpServer = HttpServer.create(new InetSocketAddress(host, port), 0, "/", this);
        this.httpServer.start();
        logger.info("web 服务已启动，端口为{}", port);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpExchangeAdapter httpExchangeAdapter = new HttpExchangeAdapter(exchange);
        HttpServletResponseImpl httpServletResponse = new HttpServletResponseImpl(httpExchangeAdapter);
        HttpServletRequestImpl httpServletRequest = new HttpServletRequestImpl(httpExchangeAdapter, servletContext, httpServletResponse);
        process(httpServletRequest, httpServletResponse);
    }

    void process(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            servletContext.process(httpServletRequest, httpServletResponse);
        } catch (IOException | ServletException e) {
            logger.error("请求处理失败。", e);
        }
    }

    @Override
    public void close() throws Exception {
        this.httpServer.stop(3);
        logger.info("web 服务已停止");

    }
}

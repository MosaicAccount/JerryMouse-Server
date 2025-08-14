package priv.study.server.connector;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import priv.study.server.engine.HttpServletRequestImpl;
import priv.study.server.engine.HttpServletResponseImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

/**
 * 处理 http 请求
 *
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public class HttpConnector implements HttpHandler, AutoCloseable{

    private final HttpServer httpServer;
    private final Logger logger = LoggerFactory.getLogger(HttpConnector.class);

    public HttpConnector() throws IOException {
        String host = "0.0.0.0";
        int port = 9999;
        this.httpServer =  HttpServer.create(new InetSocketAddress(host, port), 0, "/", this);
        this.httpServer.start();
        logger.info("web 服务已启动，端口为{}", port);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpExchangeAdapter httpExchangeAdapter = new HttpExchangeAdapter(exchange);
        HttpServletRequestImpl httpServletRequest = new HttpServletRequestImpl(httpExchangeAdapter);
        HttpServletResponseImpl httpServletResponse = new HttpServletResponseImpl(httpExchangeAdapter);
        process(httpServletRequest, httpServletResponse);
    }

    void process(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String name = httpServletRequest.getParameter("name");
        httpServletResponse.setContentType("text/html");
        String responseBody = "<html><body><h1>Hello! " + name + "</h1></body></html>";
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(responseBody);
        writer.close() ;
    }

    @Override
    public void close() throws Exception {
        this.httpServer.stop(3);
        logger.info("web 服务已停止");

    }
}

package priv.study.server.connector;

import com.sun.net.httpserver.Headers;

import java.net.URI;

/**
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public interface HttpExchangeRequest {
    String getRequestMethod();

    URI getRequestURI();

    Headers getRequestHeaders();
}

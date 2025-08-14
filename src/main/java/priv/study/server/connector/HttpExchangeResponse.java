package priv.study.server.connector;

import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public interface HttpExchangeResponse {

    Headers getResponseHeaders();

    void sendResponseHeaders(int rCode, long responseLength) throws IOException;

    OutputStream getResponseBody();

}

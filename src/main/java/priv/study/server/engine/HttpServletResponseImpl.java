package priv.study.server.engine;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import priv.study.server.connector.HttpExchangeResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public class HttpServletResponseImpl implements HttpServletResponse {

    private final HttpExchangeResponse httpExchangeResponse;

    private boolean committed; // 响应是否已经提交

    private long contentLength;

    private int status = 200;

    private int bufferSize = 1024;

    private ServletOutputStream outputStream;

    public HttpServletResponseImpl(HttpExchangeResponse httpExchangeResponse) {
        this.httpExchangeResponse = httpExchangeResponse;
    }

    @Override
    public void addCookie(Cookie cookie) {

        this.httpExchangeResponse.getResponseHeaders().add(cookie.getName(), cookie.getValue());

    }

    @Override
    public boolean containsHeader(String s) {
        return this.httpExchangeResponse.getResponseHeaders().containsKey(s);
    }

    @Override
    public String encodeURL(String s) {
        return "";
    }

    @Override
    public String encodeRedirectURL(String s) {
        return "";
    }

    @Override
    public void sendError(int i, String s) throws IOException {
        this.checkCommitted();
        this.contentLength = -1;
        this.commitStatus(500);
    }

    @Override
    public void sendError(int i) throws IOException {
        this.sendError(i, "Error");
    }

    @Override
    public void sendRedirect(String s) throws IOException {
        this.checkCommitted();
        this.contentLength = -1;
        this.commitStatus(500);
    }

    @Override
    public void setDateHeader(String s, long l) {

    }

    @Override
    public void addDateHeader(String s, long l) {

    }

    @Override
    public void setHeader(String s, String s1) {
        this.httpExchangeResponse.getResponseHeaders().set(s, s1);
    }

    @Override
    public void addHeader(String s, String s1) {
        this.setHeader(s, s1);
    }

    @Override
    public void setIntHeader(String s, int i) {
        this.httpExchangeResponse.getResponseHeaders().set(s, String.valueOf(i));
    }

    @Override
    public void addIntHeader(String s, int i) {
        this.setIntHeader(s, i);
    }

    @Override
    public void setStatus(int i) {
        try {
            this.checkCommitted();
            this.httpExchangeResponse.sendResponseHeaders(i, -1);
            this.committed = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public String getHeader(String s) {
        return String.valueOf(this.httpExchangeResponse.getResponseHeaders().get(s));
    }

    @Override
    public Collection<String> getHeaders(String s) {
        return List.of(String.valueOf(this.httpExchangeResponse.getResponseHeaders().get(s)));
    }

    @Override
    public Collection<String> getHeaderNames() {
        return this.httpExchangeResponse.getResponseHeaders().keySet();
    }

    @Override
    public String getCharacterEncoding() {
        return "";
    }

    @Override
    public String getContentType() {
        return String.valueOf(this.httpExchangeResponse.getResponseHeaders().get("Content-Type"));
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        OutputStream responseBody = this.httpExchangeResponse.getResponseBody();




        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        this.checkCommitted();
        this.committed = true;
        return new PrintWriter(this.httpExchangeResponse.getResponseBody(), true, StandardCharsets.UTF_8);
    }

    @Override
    public void setCharacterEncoding(String s) {

    }

    @Override
    public void setContentLength(int i) {
        this.contentLength = i;
    }

    @Override
    public void setContentLengthLong(long l) {
        this.contentLength = l;
    }

    @Override
    public void setContentType(String s) {
        this.checkCommitted();
        this.httpExchangeResponse.getResponseHeaders().set("Content-Type", s);
    }

    @Override
    public void setBufferSize(int i) {
        this.bufferSize = i;
    }

    @Override
    public int getBufferSize() {
        return this.bufferSize;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (Objects.isNull(outputStream)) {
            throw new IllegalStateException("输出流为空");
        }

        outputStream.flush();

        this.committed = true;
    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return this.committed;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    private void checkCommitted() {
        if (this.committed) {
            throw new IllegalStateException("响应已经被提交");
        }
    }

    private void commitStatus(int status) {
        this.status = status;
        try {
            this.httpExchangeResponse.sendResponseHeaders(status, this.contentLength);
            this.committed = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

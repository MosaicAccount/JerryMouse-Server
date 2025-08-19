package priv.study.server.engine;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import priv.study.server.connector.HttpExchangeRequest;
import priv.study.server.engine.support.HttpHeaders;
import priv.study.server.context.ServletContextImpl;
import priv.study.server.context.SessionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;
import java.util.jar.Attributes;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public class HttpServletRequestImpl implements HttpServletRequest {

    private final HttpExchangeRequest httpExchangeRequest;

    private final ServletContextImpl servletContext;

    private final HttpServletResponse httpServletResponse;

    private final Attributes attributes;

    private final HttpHeaders headers;

    public HttpServletRequestImpl(HttpExchangeRequest httpExchangeRequest, ServletContextImpl servletContext, HttpServletResponse httpServletResponse) {
        this.httpExchangeRequest = httpExchangeRequest;
        this.servletContext = servletContext;
        this.httpServletResponse = httpServletResponse;
        this.attributes = new Attributes();
        this.headers = new HttpHeaders(httpExchangeRequest.getRequestHeaders());
    }

    @Override
    public String getAuthType() {
        return "";
    }

    @Override
    public Cookie[] getCookies() {
        String header = this.getHeader("Cookie");
        return this.parseCookies(header).toArray(Cookie[]::new);
    }

    @Override
    public long getDateHeader(String s) {
        return headers.getDateHeader(s);
    }

    @Override
    public String getHeader(String s) {
        return headers.getHeader(s);
    }

    @Override
    public Enumeration<String> getHeaders(String s) {
        List<String> headerList = headers.getHeaders(s);

        return Collections.enumeration(headerList);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(headers.getHeaderNames());
    }

    @Override
    public int getIntHeader(String s) {
        return headers.getIntHeader(s);
    }

    @Override
    public String getMethod() {
        return httpExchangeRequest.getRequestMethod();
    }

    @Override
    public String getPathInfo() {
        return "";
    }

    @Override
    public String getPathTranslated() {
        return "";
    }

    @Override
    public String getContextPath() {
        return "";
    }

    @Override
    public String getQueryString() {
        return "";
    }

    @Override
    public String getRemoteUser() {
        return "";
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return "";
    }

    @Override
    public String getRequestURI() {
        return httpExchangeRequest.getRequestURI().getRawPath();
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return "";
    }

    @Override
    public HttpSession getSession(boolean b) {
        String sessionId = null;
        SessionManager sessionManager = this.servletContext.getSessionManager();
        // 通过 cookie 获取 sessionId
        Cookie[] cookies = this.getCookies();
        for (Cookie cookie : cookies) {
            if ("JSESSIONID".equals(cookie.getName())) {
                sessionId = cookie.getValue();
                break;
            }
        }
        if (Objects.isNull(sessionId) && !b) {
            return null;
        }

        // 创建一个 sessionId
        if (Objects.isNull(sessionId)) {
            if (httpServletResponse.isCommitted()) {
                throw new IllegalStateException("当前请求已经被提交，无法设置session");
            }
            // 创建随机字符串作为SessionID:
            sessionId = UUID.randomUUID().toString();
            // 构造一个名为JSESSIONID的Cookie:
            String cookieValue = "JSESSIONID=" + sessionId + "; Path=/; SameSite=Strict; HttpOnly";
            // 添加到HttpServletResponse的Header:
            this.httpServletResponse.addHeader("Set-Cookie", cookieValue);
        }
        return sessionManager.getSession(sessionId);
    }

    @Override
    public HttpSession getSession() {
        return this.getSession(true);
    }

    @Override
    public String changeSessionId() {
        return "";
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String s, String s1) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return List.of();
    }

    @Override
    public Part getPart(String s) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return this.attributes.get(s);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        Set<Object> keyset = this.attributes.keySet();
        return Collections.enumeration(keyset.stream().map(String::valueOf).collect(Collectors.toSet()));
    }

    @Override
    public String getCharacterEncoding() {
        return "";
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        return "";
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String s) {
        URI uri = this.httpExchangeRequest.getRequestURI();
        String query = uri.getRawQuery();
        if (query == null) {
            return "";
        }
        Map<String, String> params = parseQuery(query);
        return params.getOrDefault(s, "");
    }

    Map<String, String> parseQuery(String query) {
        if (query == null || query.isEmpty()) {
            return Map.of();
        }
        String[] ss = Pattern.compile("\\&").split(query);
        Map<String, String> map = new HashMap<>();
        for (String s : ss) {
            int n = s.indexOf('=');
            if (n >= 1) {
                String key = s.substring(0, n);
                String value = s.substring(n + 1);
                map.putIfAbsent(key, URLDecoder.decode(value, StandardCharsets.UTF_8));
            }
        }
        return map;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Map.of();
    }

    @Override
    public String getProtocol() {
        return "";
    }

    @Override
    public String getScheme() {
        return "";
    }

    @Override
    public String getServerName() {
        return "";
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return "";
    }

    @Override
    public String getRemoteHost() {
        return "";
    }

    @Override
    public void setAttribute(String s, Object o) {
        this.attributes.put(new Attributes.Name(s), o);
    }

    @Override
    public void removeAttribute(String s) {
        this.attributes.remove(s);
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return "";
    }

    @Override
    public String getLocalAddr() {
        return "";
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }

    @Override
    public String getRequestId() {
        return "";
    }

    @Override
    public String getProtocolRequestId() {
        return "";
    }

    @Override
    public ServletConnection getServletConnection() {
        return null;
    }

    private List<Cookie> parseCookies(String cookieHeader) {
        if (cookieHeader == null || cookieHeader.isEmpty()) {
            return Collections.emptyList();
        }

        List<Cookie> cookies = new ArrayList<>();
        String[] cookiePairs = cookieHeader.split(";\\s*");

        for (String cookiePair : cookiePairs) {
            String[] nameValue = cookiePair.split("=", 2);
            if (nameValue.length == 2) {
                String name = nameValue[0].trim();
                String value = nameValue[1].trim();

                // 处理被引号包围的值
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }

                try {
                    value = URLDecoder.decode(value, StandardCharsets.UTF_8.name());
                } catch (UnsupportedEncodingException e) {
                    // 忽略异常
                }

                Cookie cookie = new Cookie(name, value);
                cookies.add(cookie);
            }
        }

        return cookies;
    }
}

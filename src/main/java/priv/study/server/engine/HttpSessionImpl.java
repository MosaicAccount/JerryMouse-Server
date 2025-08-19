package priv.study.server.engine;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import priv.study.server.context.ServletContextImpl;
import priv.study.server.context.SessionManager;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.stream.Collectors;

/**
 * HttpSession 的实现类
 *
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public class HttpSessionImpl implements HttpSession {

    private final ServletContextImpl servletContext; // servlet 上下文

    private final String sessionId; // sessionId

    private int inactiveInterval; // session 过期时间

    private final long creationTime; // 创建时间

    private long lastAccessedTime; // 上次修改时间

    private int maxInactiveInterval;

    private final Attributes attributes;

    public HttpSessionImpl(ServletContextImpl servletContext, String sessionId, int inactiveInterval) {
        this.servletContext = servletContext;
        this.sessionId = sessionId;
        this.inactiveInterval = inactiveInterval;
        this.creationTime = System.currentTimeMillis();
         this.lastAccessedTime = this.creationTime;
        this.attributes = new Attributes();
    }

    @Override
    public long getCreationTime() {
        return this.creationTime;
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    @Override
    public int getMaxInactiveInterval() {
        return this.maxInactiveInterval;
    }

    @Override
    public Object getAttribute(String name) {
        this.updateLastAccessedTime();
        return attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        this.updateLastAccessedTime();
        Set<Object> keyset = this.attributes.keySet();
        return Collections.enumeration(keyset.stream().map(String::valueOf).collect(Collectors.toSet()));
    }

    @Override
    public void setAttribute(String name, Object value) {
        this.attributes.put(new Attributes.Name(name), value);
        this.updateLastAccessedTime();
    }

    @Override
    public void removeAttribute(String name) {
        this.attributes.remove(name);
        this.updateLastAccessedTime();
    }

    @Override
    public void invalidate() {
        SessionManager sessionManager = this.servletContext.getSessionManager();
        sessionManager.remove(this.sessionId);
    }

    @Override
    public boolean isNew() {
        return false;
    }

    private void updateLastAccessedTime() {
        this.lastAccessedTime = System.currentTimeMillis();
    }
}

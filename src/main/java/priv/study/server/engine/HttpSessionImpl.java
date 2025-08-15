package priv.study.server.engine;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import priv.study.server.context.ServletContextImpl;
import priv.study.server.context.SessionManager;

import java.util.Enumeration;
import java.util.jar.Attributes;

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

    private long creationTime; // 创建时间

    private long lastAccessedTime; // 上次修改时间

    private final Attributes attributes;

    public HttpSessionImpl(ServletContextImpl servletContext, String sessionId, int inactiveInterval) {
        this.servletContext = servletContext;
        this.sessionId = sessionId;
        this.inactiveInterval = inactiveInterval;
        this.creationTime = System.currentTimeMillis();
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
    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public void setAttribute(String name, Object value) {

    }

    @Override
    public void removeAttribute(String name) {

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
}

package priv.study.server.context;

import jakarta.servlet.http.HttpSession;
import priv.study.server.engine.HttpSessionImpl;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * session 管理
 *
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public class SessionManager {

    private final ServletContextImpl servletContext;

    private final Map<String, HttpSession> sessions;

    private int inactiveInterval = 300; // 默认 session 过期时间

    public SessionManager(ServletContextImpl servletContext) {
        this.servletContext = servletContext;
        this.sessions = new ConcurrentHashMap<>();
    }

    public HttpSession getSession(String sessionId) {
        HttpSession session = sessions.get(sessionId);
        if (Objects.nonNull(session)) {
            return session;
        }
        session = new HttpSessionImpl(servletContext, sessionId, inactiveInterval);
        sessions.put(sessionId, session);
        return session;
    }

    public void remove(String sessionId) {
        sessions.remove(sessionId);
    }

}

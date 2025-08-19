package priv.study.server.context;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SessionManager implements Runnable {

    private final ServletContextImpl servletContext;

    private final Map<String, HttpSession> sessions;

    private int inactiveInterval = 300; // 默认 session 过期时间

    private final Logger logger = LoggerFactory.getLogger(SessionManager.class);

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

    @Override
    public void run() {
        this.autoCheckSession();
    }

    /**
     * 自动检查session是否过期
     */
    private void autoCheckSession() {
        while (true) {
            try {
                Thread.sleep(60000);
                long now = System.currentTimeMillis();
                for (HttpSession session : sessions.values()) {
                    if (session.getLastAccessedTime() + session.getMaxInactiveInterval() * 1000L < now) {
                        session.invalidate();
                    }
                }
            } catch (Exception e) {
                logger.error("检查过期session过程中出现异常", e);
                break;
            }
        }
    }

}

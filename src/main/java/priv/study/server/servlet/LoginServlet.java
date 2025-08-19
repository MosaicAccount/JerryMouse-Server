package priv.study.server.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
@WebServlet(name = "loginServlet", urlPatterns = "/login")
public class LoginServlet implements Servlet {

    private final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession();
        String username = String.valueOf(session.getAttribute("username"));
        logger.info("session 中的用户名称为：{}", username);
        PrintWriter writer = response.getWriter();

        if (Objects.nonNull(username)) {
            writer.write("登录成功！");
            response.setContentLength("登录成功！".getBytes().length);
            response.setStatus(200);
        } else {
            writer.write("请登录！");
            response.setContentLength("请登录！".getBytes().length);
            response.setStatus(500);
        }
        writer.flush();
        writer.close();
    }

    @Override
    public String getServletInfo() {
        return "";
    }

    @Override
    public void destroy() {

    }
}

package priv.study.server.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServletImpl implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        String name = servletRequest.getParameter("name");
        servletResponse.setContentType("text/html");
        String responseBody = "<html><body><h1>Hello! " + name + "</h1></body></html>";
        PrintWriter writer = servletResponse.getWriter();
        writer.write(responseBody);
        writer.close() ;
    }

    @Override
    public String getServletInfo() {
        return "";
    }

    @Override
    public void destroy() {

    }
}

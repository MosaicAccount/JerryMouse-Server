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
@WebServlet(name = "noHelloServlet", urlPatterns = "/noHello")
public class NoHelloServletImpl implements Servlet {
    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        String name = req.getParameter("name");
        res.setContentType("text/html");
        String responseBody = "<html><body><h1>No Hello! " + name + "</h1></body></html>";
        PrintWriter writer = res.getWriter();
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

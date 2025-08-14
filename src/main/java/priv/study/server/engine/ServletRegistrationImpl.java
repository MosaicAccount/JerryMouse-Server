package priv.study.server.engine;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.ServletSecurityElement;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public class ServletRegistrationImpl implements ServletRegistration.Dynamic {

    private String name;
    private Servlet servlet;

    public ServletRegistrationImpl(String name, Servlet servlet) {
        this.name = name;
        this.servlet = servlet;
    }

    public Servlet getServlet() {
        return servlet;
    }

    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setLoadOnStartup(int i) {

    }

    @Override
    public Set<String> setServletSecurity(ServletSecurityElement servletSecurityElement) {
        return Set.of();
    }

    @Override
    public void setMultipartConfig(MultipartConfigElement multipartConfigElement) {

    }

    @Override
    public void setRunAsRole(String s) {

    }

    @Override
    public void setAsyncSupported(boolean b) {

    }

    @Override
    public Set<String> addMapping(String... strings) {
        return Set.of();
    }

    @Override
    public Collection<String> getMappings() {
        return List.of();
    }

    @Override
    public String getRunAsRole() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getClassName() {
        return "";
    }

    @Override
    public boolean setInitParameter(String s, String s1) {
        return false;
    }

    @Override
    public String getInitParameter(String s) {
        return "";
    }

    @Override
    public Set<String> setInitParameters(Map<String, String> map) {
        return Set.of();
    }

    @Override
    public Map<String, String> getInitParameters() {
        return Map.of();
    }
}

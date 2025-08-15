package priv.study.server.engine;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.ServletSecurityElement;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public class ServletRegistrationImpl implements ServletRegistration.Dynamic {

    private String name;
    private Servlet servlet;
    private final List<String> urlPatterns = new ArrayList<>();

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

    public List<String> getUrlPatterns() {
        return urlPatterns;
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
        urlPatterns.addAll(Arrays.asList(strings));
        return new HashSet<>(urlPatterns);
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

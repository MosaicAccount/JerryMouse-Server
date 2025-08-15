package priv.study.server.engine;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterRegistration;

import java.util.*;

/**
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public class FilterRegistrationImpl implements FilterRegistration.Dynamic {


    private final String name;

    private final Filter filter;

    private final List<String> urlPatterns;

    public FilterRegistrationImpl(String name, Filter filter) {
        this.name = name;
        this.filter = filter;
        this.urlPatterns = new ArrayList<>();
    }

    public Filter getFilter() {
        return filter;
    }

    public List<String> getUrlPatterns() {
        return urlPatterns;
    }

    @Override
    public void addMappingForServletNames(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter, String... servletNames) {

    }

    @Override
    public Collection<String> getServletNameMappings() {
        return List.of();
    }

    @Override
    public void addMappingForUrlPatterns(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter, String... urlPatterns) {
        this.urlPatterns.addAll(Arrays.asList(urlPatterns));
    }

    @Override
    public Collection<String> getUrlPatternMappings() {
        return this.urlPatterns;
    }

    @Override
    public void setAsyncSupported(boolean isAsyncSupported) {

    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getClassName() {
        return "";
    }

    @Override
    public boolean setInitParameter(String name, String value) {
        return false;
    }

    @Override
    public String getInitParameter(String name) {
        return "";
    }

    @Override
    public Set<String> setInitParameters(Map<String, String> initParameters) {
        return Set.of();
    }

    @Override
    public Map<String, String> getInitParameters() {
        return Map.of();
    }
}

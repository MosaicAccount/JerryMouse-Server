package priv.study.server.context;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.descriptor.JspConfigDescriptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import priv.study.server.engine.FilterChainImpl;
import priv.study.server.engine.FilterRegistrationImpl;
import priv.study.server.engine.ServletRegistrationImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Servlet 上下文管理（全局只有一个）
 *
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public class ServletContextImpl implements ServletContext {

    private static final Logger log = LoggerFactory.getLogger(ServletContextImpl.class);
    private final List<ServletMappig> servletMappings = new ArrayList<>();
    private final Map<String, ServletRegistration.Dynamic> nameToRegistrationMap = new HashMap<>();

    private final List<FilterMapping> filterMappings = new ArrayList<>();
    private final Map<String, FilterRegistration.Dynamic> nameToFilterRegistrationMap = new HashMap<>();

    private final SessionManager sessionManager;

    // Servlet监听器
    private List<ServletContextListener> servletContextListeners = null;
    private List<ServletContextAttributeListener> servletContextAttributeListeners = null;
    private List<ServletRequestListener> servletRequestListeners = null;
    private List<ServletRequestAttributeListener> servletRequestAttributeListeners = null;
    private List<HttpSessionAttributeListener> httpSessionAttributeListeners = null;
    private List<HttpSessionListener> httpSessionListeners = null;

    public ServletContextImpl() {
        this.sessionManager = new SessionManager(this);
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public List<ServletContextListener> getServletContextListeners() {
        return servletContextListeners;
    }

    public List<ServletContextAttributeListener> getServletContextAttributeListeners() {
        return servletContextAttributeListeners;
    }

    public List<ServletRequestListener> getServletRequestListeners() {
        return servletRequestListeners;
    }

    public List<ServletRequestAttributeListener> getServletRequestAttributeListeners() {
        return servletRequestAttributeListeners;
    }

    public List<HttpSessionAttributeListener> getHttpSessionAttributeListeners() {
        return httpSessionAttributeListeners;
    }

    public List<HttpSessionListener> getHttpSessionListeners() {
        return httpSessionListeners;
    }

    public void initialize(List<Class<? extends Servlet>> servletClasses, List<Class<? extends Filter>> filterClasses, List<Class<? extends EventListener>> listenerClasses) {
        this.initializeServlet(servletClasses);
        this.initializeFilter(filterClasses);
        this.initializeListeners(listenerClasses);
        // 触发上下文监听器
        if (Objects.nonNull(servletContextListeners)) {
            for (ServletContextListener listener : servletContextListeners) {
                listener.contextInitialized(new ServletContextEvent(this));
            }
        }
        // 启动 session 管理器
        Thread thread = new Thread(sessionManager);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * 初始化所有的Servlet
     *
     * @param servletClasses servlet 类
     */
    private void initializeServlet(List<Class<? extends Servlet>> servletClasses) {
        // 根据 Servlet 类型创建对象，并且进行注册
        for (Class<? extends Servlet> servletClass : servletClasses) {
            WebServlet annotation = servletClass.getAnnotation(WebServlet.class);
            ServletRegistration.Dynamic registration = this.addServlet(annotation.name(), servletClass);
            // 添加映射关系
            registration.addMapping(annotation.urlPatterns());
        }
        // 初始化 servletMappings 列表
        for (String key : nameToRegistrationMap.keySet()) {
            ServletRegistrationImpl registration = (ServletRegistrationImpl) nameToRegistrationMap.get(key);
            // 创建 ServletMapping 对象
            List<String> urlPatterns = registration.getUrlPatterns();
            for (String pattern : urlPatterns) {
                ServletMappig mapping = new ServletMappig(pattern, registration.getServlet());
                servletMappings.add(mapping);
            }
        }
    }

    /**
     * 初始化过滤器
     *
     * @param filterClasses filter 类
     */
    private void initializeFilter(List<Class<? extends Filter>> filterClasses) {

        // 初始化 filter 列表
        for (Class<? extends Filter> filterClass : filterClasses) {
            WebFilter webFilter = filterClass.getAnnotation(WebFilter.class);
            String filterName = webFilter.filterName();
            FilterRegistration.Dynamic filterRegistration = addFilter(filterName, filterClass);
            filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, webFilter.urlPatterns());
        }

        // 初始化 filterMapping
        for (String name : nameToFilterRegistrationMap.keySet()) {
            FilterRegistrationImpl filterRegistration = (FilterRegistrationImpl) nameToFilterRegistrationMap.get(name);
            Collection<String> urlPatternMappings = filterRegistration.getUrlPatternMappings();
            for (String urlPatternMapping : urlPatternMappings) {
                FilterMapping filterMapping = new FilterMapping(urlPatternMapping, filterRegistration.getFilter());
                filterMappings.add(filterMapping);
            }
        }
    }

    private void initializeListeners(List<Class<? extends EventListener>> listenerClasses) {
        if (Objects.nonNull(listenerClasses)) {
            for (Class<? extends EventListener> listenerClass : listenerClasses) {
                if (listenerClass.isAnnotationPresent(WebListener.class)) {
                    this.addListener(listenerClass);
                }
            }
        }
    }

    public void process(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        String path = httpServletRequest.getRequestURI();
        Servlet servlet = null;
        for (ServletMappig servletMapping : servletMappings) {
            servlet = servletMapping.match(path);
            if (Objects.nonNull(servlet)) {
                break;
            }
        }
        if (Objects.isNull(servlet)) {
            // 404 处理
            PrintWriter pw = httpServletResponse.getWriter();
            pw.write("<h1>404 Not Found</h1><p>No mapping for URL: " + path + "</p>");
            pw.close();
            return;
        }

        // 查找能够处理该请求的过滤器
        Filter[] filters = filterMappings.stream().filter(filterMapping -> filterMapping.match(path) != null).map(filterMapping -> filterMapping.match(path)).toArray(Filter[]::new);
        // 执行过滤去链
        FilterChainImpl chain = new FilterChainImpl(filters, servlet);
        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public String getContextPath() {
        return "";
    }

    @Override
    public ServletContext getContext(String s) {
        return null;
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public int getEffectiveMajorVersion() {
        return 0;
    }

    @Override
    public int getEffectiveMinorVersion() {
        return 0;
    }

    @Override
    public String getMimeType(String s) {
        return "";
    }

    @Override
    public Set<String> getResourcePaths(String s) {
        return Set.of();
    }

    @Override
    public URL getResource(String s) throws MalformedURLException {
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String s) {
        return null;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String s) {
        return null;
    }

    @Override
    public void log(String s) {

    }

    @Override
    public void log(String s, Throwable throwable) {

    }

    @Override
    public String getRealPath(String s) {
        return "";
    }

    @Override
    public String getServerInfo() {
        return "";
    }

    @Override
    public String getInitParameter(String s) {
        return "";
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return null;
    }

    @Override
    public boolean setInitParameter(String s, String s1) {
        return false;
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public String getServletContextName() {
        return "";
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String s, String s1) {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String s, Servlet servlet) {
        if (Objects.isNull(servlet)) {
            throw new IllegalArgumentException("注册 Servlet 的类型参数为空");
        }
        ServletRegistrationImpl servletRegistration = new ServletRegistrationImpl(s, servlet);
        nameToRegistrationMap.put(s, servletRegistration);
        return servletRegistration;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String s, Class<? extends Servlet> aClass) {
        if (Objects.isNull(aClass)) {
            throw new IllegalArgumentException("注册 Servlet 的类型参数为空");
        }
        // 创建 Servlet 对象
        try {
            Servlet servlet = aClass.getDeclaredConstructor().newInstance();
            return addServlet(s, servlet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServletRegistration.Dynamic addJspFile(String s, String s1) {
        return null;
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> aClass) throws ServletException {
        return null;
    }

    @Override
    public ServletRegistration getServletRegistration(String s) {
        return null;
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return Map.of();
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String s, String s1) {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String s, Filter filter) {
        if (Objects.isNull(filter)) {
            throw new IllegalArgumentException("注册 Servlet 的类型参数为空");
        }
        FilterRegistrationImpl filterRegistration = new FilterRegistrationImpl(s, filter);
        nameToFilterRegistrationMap.put(s, filterRegistration);
        return filterRegistration;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String s, Class<? extends Filter> aClass) {
        if (Objects.isNull(aClass)) {
            throw new IllegalArgumentException("注册 Servlet 的类型参数为空");
        }
        // 创建 Servlet 对象
        try {
            Filter servlet = aClass.getDeclaredConstructor().newInstance();
            return addFilter(s, servlet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> aClass) throws ServletException {
        return null;
    }

    @Override
    public FilterRegistration getFilterRegistration(String s) {
        return null;
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return Map.of();
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        return null;
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> set) {

    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return Set.of();
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return Set.of();
    }

    @Override
    public void addListener(String s) {
        try {
            addListener((Class<? extends EventListener>) Class.forName(s));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends EventListener> void addListener(T t) {
        if (Objects.isNull(t)) {
            throw new IllegalArgumentException("监听器参数为空");
        }
        if (t instanceof ServletContextListener listener) {
            if (Objects.isNull(servletContextListeners)) {
                servletContextListeners = new ArrayList<>();
            }
            servletContextListeners.add(listener);
        } else if (t instanceof ServletContextAttributeListener listener) {
            if (Objects.isNull(servletContextAttributeListeners)) {
                servletContextAttributeListeners = new ArrayList<>();
            }
            servletContextAttributeListeners.add(listener);
        } else if (t instanceof ServletRequestListener listener) {
            if (Objects.isNull(servletRequestListeners)) {
                servletRequestListeners = new ArrayList<>();
            }
            servletRequestListeners.add(listener);
        } else if (t instanceof ServletRequestAttributeListener listener) {
            if (Objects.isNull(servletRequestAttributeListeners)) {
                servletRequestAttributeListeners = new ArrayList<>();
            }
            servletRequestAttributeListeners.add(listener);
        } else if (t instanceof HttpSessionAttributeListener listener) {
            if (Objects.isNull(httpSessionAttributeListeners)) {
                httpSessionAttributeListeners = new ArrayList<>();
            }
            httpSessionAttributeListeners.add(listener);
        } else if (t instanceof HttpSessionListener listener) {
            if (Objects.isNull(httpSessionListeners)) {
                httpSessionListeners = new ArrayList<>();
            }
            httpSessionListeners.add(listener);
        }


    }

    @Override
    public void addListener(Class<? extends EventListener> aClass) {
        try {
            addListener(aClass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            log.error("创建监听器出错，异常信息为：{}", e.getMessage());
        }
    }

    @Override
    public <T extends EventListener> T createListener(Class<T> aClass) throws ServletException {
        return null;
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public void declareRoles(String... strings) {

    }

    @Override
    public String getVirtualServerName() {
        return "";
    }

    @Override
    public int getSessionTimeout() {
        return 0;
    }

    @Override
    public void setSessionTimeout(int i) {

    }

    @Override
    public String getRequestCharacterEncoding() {
        return "";
    }

    @Override
    public void setRequestCharacterEncoding(String s) {

    }

    @Override
    public String getResponseCharacterEncoding() {
        return "";
    }

    @Override
    public void setResponseCharacterEncoding(String s) {

    }
}

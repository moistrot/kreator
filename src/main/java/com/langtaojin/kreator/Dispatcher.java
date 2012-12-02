package com.langtaojin.kreator;

import com.langtaojin.kreator.annotation.Mapping;
import com.langtaojin.kreator.container.ContainerFactory;
import com.langtaojin.kreator.container.GuiceContainerFactory;
import com.langtaojin.kreator.converter.ConverterFactory;
import com.langtaojin.kreator.exception.ConfigException;
import com.langtaojin.kreator.velocity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class Dispatcher {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private ServletContext servletContext;
    private ContainerFactory containerFactory;
    private UrlMatcher[] urlMatchers = null;
    private Map<UrlMatcher, Action> urlMap = new HashMap();

    private ConverterFactory converterFactory = new ConverterFactory();
    private ExceptionHandler exceptionHandler = null;

    Profiler profiler;

    public void init(Config config) throws ServletException {
        this.log.info("Init Dispatcher...");
        this.servletContext = config.getServletContext();
        try {
            initAll(config);
        } catch (ServletException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException("Dispatcher init failed.", e);
        }
    }

    void initAll(Config config) throws Exception {
        this.containerFactory = new GuiceContainerFactory();
        this.containerFactory.init(config);
        List beans = this.containerFactory.findAllBeans();
        initComponents(beans);

        initTemplateFactory(config);
    }

    void initTemplateFactory(Config config) {
            TemplateFactory tf = new VelocityTemplateFactory();
            tf.init(config);
            log.info("Template factory '" + tf.getClass().getName() + "' init ok.");
            TemplateFactory.setTemplateFactory(tf);
        }


    void initComponents(List<Object> beans) {
        List intList = new ArrayList();
        for (Iterator i$ = beans.iterator(); i$.hasNext(); ) {
            Object bean = i$.next();

            if ((this.exceptionHandler == null) && ((bean instanceof ExceptionHandler)))
                this.exceptionHandler = ((ExceptionHandler) bean);
            addActions(bean);
        }
        if (this.exceptionHandler == null)
            this.exceptionHandler = new DefaultExceptionHandler();

        this.urlMatchers = this.urlMap.keySet().toArray(new UrlMatcher[this.urlMap.size()]);

        Arrays.sort(this.urlMatchers, new Comparator() {
            public int compare(UrlMatcher o1, UrlMatcher o2) {
                String u1 = o1.url;
                String u2 = o2.url;
                int n = u1.compareTo(u2);
                if (n == 0)
                    throw new ConfigException("Cannot mapping one url '" + u1 + "' to more than one action method.");
                return n;
            }


            public int compare(Object o, Object o1) {
                return 0;
            }
        });
    }

    private void addActions(Object bean) {
        Class clazz = bean.getClass();
        Method[] ms = clazz.getMethods();
        for (Method m : ms)
            if (isActionMethod(m)) {
                Mapping mapping = (Mapping) m.getAnnotation(Mapping.class);
                String url = mapping.value();
                UrlMatcher matcher = new UrlMatcher(url);
                if (matcher.getArgumentCount() != m.getParameterTypes().length) {
                    warnInvalidActionMethod(m, "Arguments in URL '" + url + "' does not match the arguments of method.");
                } else {
                    this.log.info("Mapping url '" + url + "' to method '" + m.toGenericString() + "'.");
                    this.urlMap.put(matcher, new Action(bean, m));
                }
            }
    }

    private boolean isActionMethod(Method m) {
        Mapping mapping = m.getAnnotation(Mapping.class);
        if (mapping == null)
            return false;
        if (mapping.value().length() == 0) {
            warnInvalidActionMethod(m, "Url mapping cannot be empty.");
            return false;
        }
        if (Modifier.isStatic(m.getModifiers())) {
            warnInvalidActionMethod(m, "method is static.");
            return false;
        }
        Class[] argTypes = m.getParameterTypes();
        for (Class argType : argTypes) {
            if (!this.converterFactory.canConvert(argType)) {
                warnInvalidActionMethod(m, "unsupported parameter '" + argType.getName() + "'.");
                return false;
            }
        }
        Class retType = m.getReturnType();
        if ((retType.equals(Void.TYPE)) || (retType.equals(String.class)) || (retType.equals(Renderer.class))) {
            return true;
        }
        warnInvalidActionMethod(m, "unsupported return type '" + retType.getName() + "'.");
        return false;
    }

    private void warnInvalidActionMethod(Method m, String string) {
        this.log.warn("Invalid Action method '" + m.toGenericString() + "': " + string);
    }

    public boolean service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        String path = req.getContextPath();

        profiler = (Profiler) req.getAttribute("profiler");
        profiler.start("url match");

        if (url.contains("/resources")) {
            return false;
        }

        if (path.length() > 0) {
            url = url.substring(path.length());
        }
        if (req.getCharacterEncoding() == null)
            req.setCharacterEncoding("UTF-8");


        Execution execution = null;
        for (UrlMatcher matcher : this.urlMatchers) {
            String[] args = matcher.getMatchedParameters(url);
            if (args != null) {
                Action action = this.urlMap.get(matcher);
                Object[] arguments = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    Class type = action.arguments[i];
                    if (type.equals(String.class))
                        arguments[i] = args[i];
                    else
                        arguments[i] = this.converterFactory.convert(type, args[i]);
                }
                execution = new Execution(req, resp, action, arguments);
                break;
            }
        }
        profiler.start("execute..");
        if (execution != null) {
            handleExecution(execution, req, resp);
        }
        return execution != null;
    }

    private void handleExecution(Execution execution, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionContext.setActionContext(this.servletContext, request, response);
        try {
            handleResult(request, response, execution.execute());
            log.info("coming!!");
        } catch (Exception e) {
            handleException(request, response, e);
        } finally {
            ActionContext.removeActionContext();
        }
    }

    private void handleResult(HttpServletRequest request, HttpServletResponse response, Object result) throws Exception {
        if (result == null)
            return;
        if (result instanceof Renderer) {
            profiler.start("render velocity template..");
            Renderer r = (Renderer) result;
            r.render(this.servletContext, request, response);
            return;
        }
        if (result instanceof String) {
            String s = (String) result;
            if (s.startsWith("redirect:")) {
                response.sendRedirect(s.substring("redirect:".length()));
                return;
            }
            profiler.start("render text");
            new TextRenderer(s).render(servletContext, request, response);
            return;
        }
        throw new ServletException("Cannot handle result with type '" + result.getClass().getName() + "'.");
    }

    void handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws ServletException, IOException {
        try {
            this.exceptionHandler.handle(request, response, ex);
        } catch (ServletException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }


    public void destroy() {
        this.log.info("Destroy Dispatcher...");
        this.containerFactory.destroy();
    }
}

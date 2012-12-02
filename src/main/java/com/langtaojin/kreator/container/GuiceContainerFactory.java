package com.langtaojin.kreator.container;

import com.google.inject.*;
import com.langtaojin.kreator.Config;
import com.langtaojin.kreator.Destroyable;
import com.langtaojin.kreator.exception.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.util.*;

public class GuiceContainerFactory implements ContainerFactory {

    private Logger log = LoggerFactory.getLogger(getClass());
    private Injector injector;

    public List<Object> findAllBeans() {
        Map<Key<?>, Binding<?>> map = injector.getBindings();
        Set<Key<?>> keys = map.keySet();
        List<Object> list = new ArrayList<Object>(keys.size());
        for (Key<?> key : keys) {
            Object bean = injector.getInstance(key);
            list.add(bean);
        }
        return list;
    }

    public void init(Config config) {
        String value = config.getInitParameter("modules");
        if (value == null)
            throw new ConfigException("Init guice failed. Missing parameter '<modules>'.");
        String[] ss = value.split(",");
        List moduleList = new ArrayList(ss.length);
        for (String s : ss) {
            Module m = initModule(s, config.getServletContext());
            if (m != null)
                moduleList.add(m);
        }
        if (moduleList.isEmpty())
            throw new ConfigException("No module found.");
        this.injector = Guice.createInjector(Stage.PRODUCTION, moduleList);
        config.getServletContext().setAttribute(Injector.class.getName(), this.injector);
    }

    Module initModule(String s, ServletContext servletContext) {
        s = trim(s);
        if (s.length() > 0) {
            if (this.log.isDebugEnabled())
                this.log.debug("Initializing module '" + s + "'...");
            try {
                Object o = Class.forName(s).newInstance();
                if ((o instanceof Module)) {
                    if ((o instanceof ServletContextAware)) {
                        ((ServletContextAware) o).setServletContext(servletContext);
                    }
                    return (Module) o;
                }
                throw new ConfigException("Class '" + s + "' does not implement '" + Module.class.getName() + "'.");
            } catch (InstantiationException e) {
                throw new ConfigException("Cannot instanciate class '" + s + "'.", e);
            } catch (IllegalAccessException e) {
                throw new ConfigException("Cannot instanciate class '" + s + "'.", e);
            } catch (ClassNotFoundException e) {
                throw new ConfigException("Cannot instanciate class '" + s + "'.", e);
            }
        }
        return null;
    }

    String trim(String s) {
        while (s.length() > 0) {
            char c = s.charAt(0);
            if (" \t\r\n".indexOf(c) == -1) break;
            s = s.substring(1);
        }

        while (s.length() > 0) {
            char c = s.charAt(s.length() - 1);
            if (" \t\r\n".indexOf(c) == -1) break;
            s = s.substring(0, s.length() - 1);
        }

        return s;
    }

    public void destroy() {
        List beans = findAllBeans();
        for (Iterator i$ = beans.iterator(); i$.hasNext(); ) {
            Object bean = i$.next();
            if ((bean instanceof Destroyable))
                ((Destroyable) bean).destroy();
        }
    }
}

package com.langtaojin.kreator;

import com.langtaojin.kreator.container.ContainerFactory;
import com.langtaojin.kreator.exception.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;

public class Utils {
    static final Logger log = LoggerFactory.getLogger(Utils.class);

      public static ContainerFactory createContainerFactory(String name) throws ServletException {
        ContainerFactory cf = tryInitContainerFactory(name);
        if (cf == null)
          cf = tryInitContainerFactory(ContainerFactory.class.getPackage().getName() + "." + name + ContainerFactory.class.getSimpleName());
        if (cf == null)
          throw new ConfigException("Cannot create container factory by name '" + name + "'.");
        return cf;
      }

      static ContainerFactory tryInitContainerFactory(String clazz) {
        try {
          Object obj = Class.forName(clazz).newInstance();
          if ((obj instanceof ContainerFactory))
            return (ContainerFactory)obj;
        } catch (Exception e) {
        }
        return null;
      }

}

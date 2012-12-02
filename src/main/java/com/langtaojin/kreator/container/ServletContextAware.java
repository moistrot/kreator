package com.langtaojin.kreator.container;

import javax.servlet.ServletContext;

public interface ServletContextAware {
    public abstract void setServletContext(ServletContext paramServletContext);
}

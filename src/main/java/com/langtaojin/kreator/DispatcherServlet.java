package com.langtaojin.kreator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DispatcherServlet extends GenericServlet {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Dispatcher dispatcher;
    private StaticFileHandler staticFileHandler;

    public void init(final ServletConfig config)
            throws ServletException {
        super.init(config);
        this.log.info("Init DispatcherServlet...");
        this.dispatcher = new Dispatcher();
        this.dispatcher.init(new Config() {
            public String getInitParameter(String name) {
                return config.getInitParameter(name);
            }

            public ServletContext getServletContext() {
                return config.getServletContext();
            }
        });
        this.staticFileHandler = new StaticFileHandler(config);
    }

    public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) resp;
        String method = httpReq.getMethod();
        if (("GET".equals(method)) || ("POST".equals(method))) {
            if (!this.dispatcher.service(httpReq, httpResp)) {
                this.staticFileHandler.handle(httpReq, httpResp);
            }
            return;
        }
        httpResp.sendError(405);
    }

    public void destroy() {
        this.log.info("Destroy DispatcherServlet...");
        this.dispatcher.destroy();
    }
}

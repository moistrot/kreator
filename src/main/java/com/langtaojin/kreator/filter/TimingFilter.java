package com.langtaojin.kreator.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TimingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(TimingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        Profiler profiler = new Profiler(req.getRequestURL().toString());
        profiler.start("request");
        request.setAttribute("profiler", profiler);
        chain.doFilter(request, response);
        profiler.stop();
        System.out.println(profiler.toString());
        logger.warn(profiler.toString());

    }

    @Override
    public void destroy() {
    }
}

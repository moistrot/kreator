package com.langtaojin.kreator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public final class ActionContext {
    private static final ThreadLocal<ActionContext> actionContextThreadLocal = new ThreadLocal();
    private ServletContext context;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public ServletContext getServletContext() {
        return this.context;
    }

    public HttpServletRequest getHttpServletRequest() {
        return this.request;
    }

    public HttpServletResponse getHttpServletResponse() {
        return this.response;
    }

    public HttpSession getHttpSession() {
        return this.request.getSession();
    }

    public static ActionContext getActionContext() {
        return (ActionContext) actionContextThreadLocal.get();
    }

    static void setActionContext(ServletContext context, HttpServletRequest request, HttpServletResponse response) {
        ActionContext ctx = new ActionContext();
        ctx.context = context;
        ctx.request = request;
        ctx.response = response;
        actionContextThreadLocal.set(ctx);
    }

    static void removeActionContext() {
        actionContextThreadLocal.remove();
    }
}

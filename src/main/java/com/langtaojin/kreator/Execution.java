package com.langtaojin.kreator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

public class Execution {
    public final HttpServletRequest request;
      public final HttpServletResponse response;
      private final Action action;
      private final Object[] args;

      public Execution(HttpServletRequest request, HttpServletResponse response, Action action, Object[] args)
      {
        this.request = request;
        this.response = response;
        this.action = action;
        this.args = args;
      }

      public Object execute() throws Exception {
        try {
          return this.action.method.invoke(this.action.instance, this.args);
        }
        catch (InvocationTargetException e) {
          Throwable t = e.getCause();
          if ((t != null) && ((t instanceof Exception)))
            throw ((Exception)t);
          throw e;
        }
      }
}

package com.langtaojin.kreator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface ExceptionHandler {
    public abstract void handle(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Exception paramException)
      throws Exception;
}

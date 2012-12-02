package com.langtaojin.kreator;

import javax.servlet.ServletContext;

public abstract interface Config
{
  public abstract ServletContext getServletContext();

  public abstract String getInitParameter(String paramString);
}
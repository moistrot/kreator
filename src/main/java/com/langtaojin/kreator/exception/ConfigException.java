package com.langtaojin.kreator.exception;

public class ConfigException extends IllegalArgumentException
{
  public ConfigException()
  {
  }

  public ConfigException(String message)
  {
    super(message);
  }

  public ConfigException(Throwable cause) {
    super(cause);
  }

  public ConfigException(String message, Throwable cause) {
    super(message, cause);
  }
}
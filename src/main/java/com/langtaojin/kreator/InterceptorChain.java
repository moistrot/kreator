package com.langtaojin.kreator;

public abstract interface InterceptorChain {
    public abstract void doInterceptor(Execution paramExecution)
        throws Exception;
}

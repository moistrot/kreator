package com.langtaojin.kreator;

public abstract interface Interceptor {
    public abstract void intercept(Execution paramExecution, InterceptorChain paramInterceptorChain)
        throws Exception;
}

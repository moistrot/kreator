package com.langtaojin.kreator.velocity;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Renderer {

    protected String contentType;

    /**
     * Get response content type.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Set response content type, for example, "text/xml". The default content 
     * type is "text/html". DO NOT add "charset=xxx".
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Render the output of http response.
     * 
     * @param context ServletContext object.
     * @param request HttpServletRequest object.
     * @param response HttpServletResponse object.
     * @throws Exception If any Exception occur.
     */
    public abstract void render(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws Exception;

}

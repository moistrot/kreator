package com.langtaojin.kreator.velocity;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TemplateRenderer extends Renderer {

    private String path;
    private Map<String, Object> model;

    public TemplateRenderer(String path) {
        this.path = path;
        this.model = new HashMap<String, Object>();
    }

    public TemplateRenderer(String path, Map<String, Object> model) {
        this.path = path;
        this.model = model;
    }

    public TemplateRenderer(String path, String modelKey, Object modelValue) {
        this.path = path;
        this.model = new HashMap<String, Object>();
        this.model.put(modelKey, modelValue);
    }

    @Override
    public void render(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println(TemplateFactory.getTemplateFactory());
        TemplateFactory.getTemplateFactory()
                .loadTemplate(path)
                .render(request, response, model);
    }

}

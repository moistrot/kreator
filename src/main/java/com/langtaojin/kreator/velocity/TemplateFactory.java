package com.langtaojin.kreator.velocity;

import com.langtaojin.kreator.Config;
import org.apache.commons.logging.LogFactory;

public abstract class TemplateFactory {

    private static TemplateFactory instance;

    public static void setTemplateFactory(TemplateFactory templateFactory) {
        instance = templateFactory;
    }

    public static TemplateFactory getTemplateFactory() {
        return instance;
    }

    public abstract void init(Config config);

    public abstract VelocityTemplate loadTemplate(String path) throws Exception;

}

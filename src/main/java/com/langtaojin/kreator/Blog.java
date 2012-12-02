package com.langtaojin.kreator;

import com.langtaojin.kreator.annotation.Mapping;
import com.langtaojin.kreator.velocity.Renderer;
import com.langtaojin.kreator.velocity.TemplateRenderer;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class Blog {

    private List<String> titles;

    @Mapping("/create/$1")
    public Renderer create(int userId) {
        return new TemplateRenderer("/index.vm", "titles", getTitles());
    }

    public List<String> getTitles() {
        if(CollectionUtils.isEmpty(titles)) {
            titles = new ArrayList<String>();
            for(int i = 0; i < 50; i++) {
                titles.add("|-- Total        [http://localhost:8088/create/1]    88.521 milliseconds.<br/>");
            }
        }
        return titles;
    }
}

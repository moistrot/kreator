package com.langtaojin.kreator;

import com.google.inject.Binder;
import com.google.inject.Module;

public class BlogModule implements Module {

    public void configure(Binder binder) {
        binder.bind(Blog.class).asEagerSingleton();
    }
}

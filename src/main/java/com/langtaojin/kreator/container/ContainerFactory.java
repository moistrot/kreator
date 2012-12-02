package com.langtaojin.kreator.container;

import com.langtaojin.kreator.Config;

import java.util.List;

public interface ContainerFactory {

    public abstract void init(Config paramConfig);

    public List<Object> findAllBeans();

    public void destroy();
}

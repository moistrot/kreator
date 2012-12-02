package com.langtaojin.kreator;

import com.langtaojin.kreator.annotation.Mapping;

public class Blog {

    @Mapping("/create/$1")
    public void create(int userId) {
        System.out.println(userId);
    }

}

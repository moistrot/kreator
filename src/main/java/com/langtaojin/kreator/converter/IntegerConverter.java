package com.langtaojin.kreator.converter;

public class IntegerConverter implements Converter {
    public Integer convert(String paramString) {
        return Integer.valueOf(Integer.parseInt(paramString));
    }
}

package com.langtaojin.kreator.converter;

public class BooleanConverter implements Converter {

    public Boolean convert(String paramString) {
        return Boolean.valueOf(Boolean.parseBoolean(paramString));
    }
}

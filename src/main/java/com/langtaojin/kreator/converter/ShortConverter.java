package com.langtaojin.kreator.converter;

public class ShortConverter implements Converter {
    public Short convert(String paramString) {
        return Short.valueOf(Short.parseShort(paramString));
    }
}

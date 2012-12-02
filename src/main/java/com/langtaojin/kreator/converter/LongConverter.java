package com.langtaojin.kreator.converter;

public class LongConverter implements Converter {
    public Long convert(String paramString) {
        return Long.valueOf(Long.parseLong(paramString));
    }
}

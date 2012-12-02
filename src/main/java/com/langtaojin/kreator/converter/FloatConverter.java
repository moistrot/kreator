package com.langtaojin.kreator.converter;

public class FloatConverter implements Converter {
    public Float convert(String paramString) {
        return Float.valueOf(Float.parseFloat(paramString));
    }
}

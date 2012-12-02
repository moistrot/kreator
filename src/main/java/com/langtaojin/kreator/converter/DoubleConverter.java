package com.langtaojin.kreator.converter;

public class DoubleConverter implements Converter {
    public Double convert(String paramString) {
        return Double.valueOf(Double.parseDouble(paramString));
    }
}

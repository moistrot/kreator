package com.langtaojin.kreator.converter;

public abstract interface Converter<T> {
    public abstract T convert(String paramString);

}

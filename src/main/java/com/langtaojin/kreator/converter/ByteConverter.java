package com.langtaojin.kreator.converter;

public class ByteConverter implements Converter {
    public Byte convert(String paramString) {
        return Byte.valueOf(Byte.parseByte(paramString));
    }
}

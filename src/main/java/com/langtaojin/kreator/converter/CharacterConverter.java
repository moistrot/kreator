package com.langtaojin.kreator.converter;

public class CharacterConverter implements Converter {

    public Character convert(String paramString) {
        if (paramString.length() == 0)
              throw new IllegalArgumentException("Cannot convert empty string to char.");
            return Character.valueOf(paramString.charAt(0));
    }
}

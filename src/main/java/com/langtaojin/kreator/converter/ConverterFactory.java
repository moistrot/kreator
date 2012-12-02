package com.langtaojin.kreator.converter;

import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ConverterFactory {
    private final Logger log = LoggerFactory.getLogger(getClass());

      private Map<Class<?>, Converter<?>> map = new HashMap();

      public ConverterFactory() {
        loadInternal();
      }

      void loadInternal() {
        Converter c = null;

        c = new BooleanConverter();
        this.map.put(Boolean.TYPE, c);
        this.map.put(Boolean.class, c);

        c = new CharacterConverter();
        this.map.put(Character.TYPE, c);
        this.map.put(Character.class, c);

        c = new ByteConverter();
        this.map.put(Byte.TYPE, c);
        this.map.put(Byte.class, c);

        c = new ShortConverter();
        this.map.put(Short.TYPE, c);
        this.map.put(Short.class, c);

        c = new IntegerConverter();
        this.map.put(Integer.TYPE, c);
        this.map.put(Integer.class, c);

        c = new LongConverter();
        this.map.put(Long.TYPE, c);
        this.map.put(Long.class, c);

        c = new FloatConverter();
        this.map.put(Float.TYPE, c);
        this.map.put(Float.class, c);

        c = new DoubleConverter();
        this.map.put(Double.TYPE, c);
        this.map.put(Double.class, c);
      }

      public void loadExternalConverter(String typeClass, String converterClass) {
        try {
          loadExternalConverter(Class.forName(typeClass), (Converter)Class.forName(converterClass).newInstance());
        }
        catch (Exception e) {
          this.log.warn("Cannot load converter '" + converterClass + "' for type '" + typeClass + "'.", e);
        }
      }

      public void loadExternalConverter(Class<?> clazz, Converter<?> converter) {
        if (clazz == null)
          throw new NullPointerException("Class is null.");
        if (converter == null)
          throw new NullPointerException("Converter is null.");
        if (this.map.containsKey(clazz)) {
          this.log.warn("Cannot replace the exist converter for type '" + clazz.getName() + "'.");
          return;
        }
        this.map.put(clazz, converter);
      }

      public boolean canConvert(Class<?> clazz) {
        return (clazz.equals(String.class)) || (this.map.containsKey(clazz));
      }

      public Object convert(Class<?> clazz, String s) {
        Converter c = (Converter)this.map.get(clazz);
        return c.convert(s);
      }
}

package ir.sam.XO.server.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.Properties;

public class Config extends Properties {
    public Config(String address) {
        try {
            Reader fileReader = new FileReader(address);
            this.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <E> Optional<E> getProperty(Class<E> c, String propertyName) {
        if (containsKey(propertyName)) return Optional.of(getObject(c, getProperty(propertyName)));
        else return Optional.empty();
    }

    private <E> E getObject(Class<E> c, String value) {
        E e = null;
        try {
            Constructor<E> constructor = c.getConstructor(String.class);
            e = constructor.newInstance(value);
        } catch (ReflectiveOperationException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
        }
        return e;
    }
}
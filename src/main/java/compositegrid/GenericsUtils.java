package compositegrid;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

public class GenericsUtils {
    public static <T> T[] createArray(Class<T> clazz, int length) {
        T[] array = (T[]) Array.newInstance(clazz, length);
        return array;
    }

    public static Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}

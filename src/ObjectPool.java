import Logger.Logger;
import poolObject.PoolObjectInterface;

import java.util.LinkedList;

public class ObjectPool<T extends PoolObjectInterface> {
    private static ObjectPool<?> instance;
    private final LinkedList<T> objects = new LinkedList<>();
    private final int INITIAL_SIZE = 5;
    private final Class<T> type;

    private ObjectPool(Class<T> type) {
        this.type = type;
        try {
            for (int i = 0; i < INITIAL_SIZE; i++) {
                objects.add(createNewObject());
            }
        } catch (Exception e) {
            throw new RuntimeException("ObjectPool instantiation failed", e);
        }
        Logger.getInstance().log("Pool initialized with " + objects.size() + " objects.", Logger.LogLevel.INFO);
    }

    private T createNewObject() {
        try {
            T object = type.getDeclaredConstructor().newInstance();
            Logger.getInstance().log("Created a new object.", Logger.LogLevel.INFO);
            return object;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create a new instance of " + type.getSimpleName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends PoolObjectInterface> ObjectPool<T> getInstance(Class<T> type) {
        if (instance == null) {
            instance = new ObjectPool<T>(type);
        }
        return (ObjectPool<T>) instance;
    }

    public T get() {
        T object;
        if (!objects.isEmpty()) {
            object = objects.removeFirst();
        } else {
            object = createNewObject();
        }
        object.reset();
        Logger.getInstance().log("Object retrieved from pool. Pool size now: " + objects.size(), Logger.LogLevel.INFO);
        return object;
    }

    public void release(T object) {
        objects.add(object);
        Logger.getInstance().log("Object returned to pool. Pool size now: " + objects.size(), Logger.LogLevel.INFO);
    }
}

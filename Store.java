import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Store<T> {
    private Supplier<T> supplier;
    private Set<BiConsumer<T, Object>> bisubscribers = Collections.newSetFromMap(new WeakHashMap<>());
    private Set<Consumer<T>> subscribers = Collections.newSetFromMap(new WeakHashMap<>());

    public Store(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * subscribe now and get one 𝐓 for free!!
     * 
     * @return the subscriber, for keeping in a non-weak reference
     */
    public BiConsumer<T, Object> subscribe(BiConsumer<T, Object> subscriber) {
        bisubscribers.add(subscriber);
        subscriber.accept(supplier.get(), this);
        return subscriber;
    }

    /**
     * subscribe now and get one 𝐓 for free!!
     * 
     * @return the subscriber, for keeping in a non-weak reference
     */
    public Consumer<T> subscribe(Consumer<T> subscriber) {
        subscribers.add(subscriber);
        subscriber.accept(supplier.get());
        return subscriber;
    }

    public void unsubscribe(BiConsumer<T, Object> subscriber) {
        bisubscribers.remove(subscriber);
    }

    public void unsubscribe(Consumer<T> subscriber) {
        subscribers.remove(subscriber);
    }

    /** @param source object that started change */
    public void broadcast(Object source) {
        T value = supplier.get();
        subscribers.forEach(sub -> sub.accept(value));
        bisubscribers.forEach(sub -> sub.accept(value, source));
    }
}

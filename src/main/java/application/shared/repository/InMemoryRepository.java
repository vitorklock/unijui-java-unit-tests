package shared.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.ToIntFunction;

public class InMemoryRepository<T> implements Repository<T> {

    protected final Map<Integer, T> storage = new ConcurrentHashMap<>();
    private final ToIntFunction<T> idExtractor;

    public InMemoryRepository(ToIntFunction<T> idExtractor) {
        if (idExtractor == null) {
            throw new IllegalArgumentException("idExtractor must not be null");
        }
        this.idExtractor = idExtractor;
    }

    @Override
    public T save(T entity) {
        int id = idExtractor.applyAsInt(entity);
        storage.put(id, entity);
        return entity;
    }

    @Override
    public Optional<T> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean deleteById(int id) {
        return storage.remove(id) != null;
    }

    @Override
    public void deleteAll() {
        storage.clear();
    }

    @Override
    public long count() {
        return storage.size();
    }
}

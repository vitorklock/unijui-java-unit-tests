package shared.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    T save(T entity);

    Optional<T> findById(int id);

    List<T> findAll();

    boolean deleteById(int id);

    void deleteAll();

    long count();
}

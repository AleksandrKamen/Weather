package util.repository;

import java.util.Optional;

public interface Repository<T, K> {

    T save(T entity);

    Optional<T> findById(K id);

    void update(T entity);

    void delete(K id);
}

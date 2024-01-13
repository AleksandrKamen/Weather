package util.repository;

import java.util.Optional;

public interface Repository<T> {

    T save(T entity);
    Optional<T> findById(Long id);
    void update(T entity);
    void delete (Long id);
}

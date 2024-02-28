package util.repository;

import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import util.HibernateUtil;
import validator.exception.DataBaseException;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseRepository<T, K> implements Repository<T, K> {

    private final Class<T> clazz;

    @Override
    public T save(T entity) {
        try (var session = HibernateUtil.getSession()) {
            Transaction transaction = session.getTransaction();
            try {
                session.beginTransaction();
                session.persist(entity);
                session.getTransaction().commit();
            } catch (HibernateException hibernateException) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
        return entity;
    }

    @Override
    public Optional<T> findById(K id) {
        try (var session = HibernateUtil.getSession()) {
            session.beginTransaction();
            var mabyObject = session.get(clazz, id);
            session.getTransaction().commit();
            return Optional.ofNullable(mabyObject);
        } catch (HibernateException hibernateException) {
            throw new DataBaseException("Database Error");
        }
    }

    @Override
    public void update(T entity) {
        try (var session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
        } catch (HibernateException hibernateException) {
            throw new DataBaseException("Database Error");
        }
    }

    @Override
    public void delete(K id) {
        try (var session = HibernateUtil.getSession()) {
            session.beginTransaction();
            var entity = session.get(clazz, id);
            session.remove(entity);
            session.getTransaction().commit();
        } catch (HibernateException hibernateException) {
            throw new DataBaseException("Database Error");
        }
    }
}

package util.repository;

import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.Optional;
@RequiredArgsConstructor
public class BaseRepository <T> implements Repository<T>{

    private final Class<T> clazz;
    @Override
    public T save(T entity) {
        try (var session = HibernateUtil.getSession()) {
            Transaction transaction = session.getTransaction();
            try {
                session.beginTransaction();
                session.persist(entity);
                session.getTransaction().commit();
            }
            catch (HibernateException hibernateException) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
        return entity;
    }


    @Override
    public Optional<T> findById(Long id) {
        try (var session = HibernateUtil.getSession()) {
            session.beginTransaction();
            var mabyObject = session.get(clazz, id);
            return Optional.ofNullable(mabyObject);
        }
    }

    @Override
    public void update(T entity) {
        try (var session = HibernateUtil.getSession()) {
            session.merge(entity);
        }
    }

    @Override
    public void delete(Long id) {
        try (var session = HibernateUtil.getSession()) {
            session.remove(id);
        }
    }
}

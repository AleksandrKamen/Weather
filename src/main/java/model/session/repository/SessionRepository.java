package model.session.repository;

import model.session.entity.Session;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.repository.BaseRepository;

import java.time.LocalDateTime;

public class SessionRepository extends BaseRepository<Session, String> {
    public SessionRepository() {
        super(Session.class);
    }

    public void deleteSessionsIfTimeIsUp() {
        Transaction transaction = null;
        try (var session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            var query = session.createQuery("delete from Session where expiresAt < :now");
            query.setParameter("now", LocalDateTime.now());
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new HibernateException("failed to delete");
        }
    }
}

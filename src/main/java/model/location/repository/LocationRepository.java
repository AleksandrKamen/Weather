package model.location.repository;

import model.location.entity.Location;
import org.hibernate.HibernateException;
import util.HibernateUtil;
import util.repository.BaseRepository;
import validator.exception.DataBaseException;

import java.util.List;

public class LocationRepository extends BaseRepository<Location,Long> {
    public LocationRepository() {
        super(Location.class);
    }

    public List<Location> findLocationsByUserLogin(String login){
        try (var session = HibernateUtil.getSession()) {
            session.beginTransaction();
            var query = session.createQuery("select l from Location l JOIN FETCH l.user u where u.login = :login", Location.class);
            query.setParameter("login", login);
            var resultList = query.getResultList();
            session.getTransaction().commit();
            return resultList;
        } catch (HibernateException hibernateException){
            throw new DataBaseException("Database Error");
        }
    }

    public List<Location> findLocationsByUserLoginWithPagination(String login, Integer offset, Integer limit){
        try (var session = HibernateUtil.getSession()) {
            session.beginTransaction();
            var query = session.createQuery("select l from Location l JOIN FETCH l.user u where u.login = :login order by l.id desc", Location.class);
            query.setParameter("login", login);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            var resultList = query.getResultList();
            session.getTransaction().commit();
            return resultList;
        } catch (HibernateException hibernateException){
            throw new DataBaseException("Database Error");
        }
    }
}

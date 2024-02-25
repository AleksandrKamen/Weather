package model.location.repository;

import model.location.entity.Location;
import util.HibernateUtil;
import util.repository.BaseRepository;
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
        }
    }
}

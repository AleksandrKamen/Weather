package model.location.repository;

import model.location.entity.LocationEntity;
import util.HibernateUtil;
import util.repository.BaseRepository;
import java.util.List;

public class LocationRepository extends BaseRepository<LocationEntity,Long> {
    public LocationRepository() {
        super(LocationEntity.class);
    }

    public List<LocationEntity> findLocationsByUserLogin(String login){
        try (var session = HibernateUtil.getSession()) {
            session.beginTransaction();
            var query = session.createQuery("select l from LocationEntity l JOIN FETCH l.user u where u.login = :login", LocationEntity.class);
            query.setParameter("login", login);
            var resultList = query.getResultList();
            session.getTransaction().commit();
            return resultList;
        }
    }
}

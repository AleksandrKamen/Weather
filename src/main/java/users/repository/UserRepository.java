package users.repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;
import users.entity.UsersEntity;
import util.HibernateUtil;
import util.repository.BaseRepository;

import java.util.Optional;

public class UserRepository extends BaseRepository<UsersEntity> {
   public UserRepository(){super(UsersEntity.class);}

   public Optional<UsersEntity> findByLogin(String login){
       try (var session = HibernateUtil.getSession()) {
           session.beginTransaction();
           var query = session.createQuery("select u from UsersEntity u where u.login = :login", UsersEntity.class);
           query.setParameter("login", login);
           var singleResult = query.getSingleResult();
           session.getTransaction().commit();
           return Optional.ofNullable(singleResult);
         }
    }
}

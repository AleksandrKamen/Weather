package model.user.repository;

import model.user.entity.UserEntity;
import util.HibernateUtil;
import util.repository.BaseRepository;
import java.util.Optional;

public class UserRepository extends BaseRepository<UserEntity,Long> {
   public UserRepository(){super(UserEntity.class);}

   public Optional<UserEntity> findByLoginWithSession(String login){
       try (var session = HibernateUtil.getSession()) {
           session.beginTransaction();
           var query = session.createQuery("select u from UserEntity u LEFT JOIN FETCH u.sessions s where u.login = :login", UserEntity.class);
           query.setParameter("login", login);
           var maybeUser = query.getSingleResultOrNull();
           session.getTransaction().commit();
           return Optional.ofNullable(maybeUser);
         }
    }
    public Optional<UserEntity> findByLoginWithLocation(String login){
       try (var session = HibernateUtil.getSession()) {
           session.beginTransaction();
           var query = session.createQuery("select u from UserEntity u LEFT JOIN FETCH u.locations l where u.login = :login", UserEntity.class);
           query.setParameter("login", login);
           var maybeUser = query.getSingleResultOrNull();
           session.getTransaction().commit();
           return Optional.ofNullable(maybeUser);
         }
    }
    public Optional<UserEntity> findUserBySession(String sessionId){
        try (var session = HibernateUtil.getSession()) {
            session.beginTransaction();
            var query = session.createQuery("select u from UserEntity u LEFT JOIN FETCH u.sessions s where s.id = :sessionId", UserEntity.class);
            query.setParameter("sessionId", sessionId);
            var user = query.getSingleResultOrNull();
            session.getTransaction().commit();
            return Optional.ofNullable(user);
        }
    }
 }

package model.user.repository;

import model.user.entity.User;
import util.HibernateUtil;
import util.repository.BaseRepository;
import java.util.Optional;

public class UserRepository extends BaseRepository<User,Long> {
   public UserRepository(){super(User.class);}

   public Optional<User> findByLoginWithSession(String login){
       try (var session = HibernateUtil.getSession()) {
           session.beginTransaction();
           var query = session.createQuery("select u from User u LEFT JOIN FETCH u.sessions s where u.login = :login", User.class);
           query.setParameter("login", login);
           var maybeUser = query.getSingleResultOrNull();
           session.getTransaction().commit();
           return Optional.ofNullable(maybeUser);
         }
    }
    public Optional<User> findByLoginWithLocation(String login){
       try (var session = HibernateUtil.getSession()) {
           session.beginTransaction();
           var query = session.createQuery("select u from User u LEFT JOIN FETCH u.locations l where u.login = :login", User.class);
           query.setParameter("login", login);
           var maybeUser = query.getSingleResultOrNull();
           session.getTransaction().commit();
           return Optional.ofNullable(maybeUser);
         }
    }
    public Optional<User> findUserBySession(String sessionId){
        try (var session = HibernateUtil.getSession()) {
            session.beginTransaction();
            var query = session.createQuery("select u from User u LEFT JOIN FETCH u.sessions s where s.id = :sessionId", User.class);
            query.setParameter("sessionId", sessionId);
            var user = query.getSingleResultOrNull();
            session.getTransaction().commit();
            return Optional.ofNullable(user);
        }
    }
 }

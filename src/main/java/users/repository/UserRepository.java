package users.repository;

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
           var mabyUser = query.getSingleResultOrNull();
           session.getTransaction().commit();
           return Optional.ofNullable(mabyUser);
         }
    }
}

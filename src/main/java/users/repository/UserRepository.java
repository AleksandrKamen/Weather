package users.repository;

import users.entity.UsersEntity;
import util.repository.BaseRepository;

public class UserRepository extends BaseRepository<UsersEntity> {
    public UserRepository(){super(UsersEntity.class);}
}

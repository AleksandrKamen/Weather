package model.session.repository;

import model.session.entity.SessionEntity;
import util.repository.BaseRepository;

public class SessionsRepository extends BaseRepository<SessionEntity,String> {
    public SessionsRepository() {
        super(SessionEntity.class);
    }
}

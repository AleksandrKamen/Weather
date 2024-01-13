package session.repository;

import session.entity.SessionEntity;
import util.repository.BaseRepository;

public class SessionsRepository extends BaseRepository<SessionEntity> {
    public SessionsRepository() {
        super(SessionEntity.class);
    }
}

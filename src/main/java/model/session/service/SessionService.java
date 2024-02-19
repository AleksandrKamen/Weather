package model.session.service;

import model.session.repository.SessionRepository;

public class SessionService {
    private static final SessionRepository sessionRepository = new SessionRepository();
    public void deleteSessionsIfTimeIsUp(){
        sessionRepository.deleteSessionsIfTimeIsUp();
    }
}

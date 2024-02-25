package model.session.service;

import model.session.entity.Session;
import model.session.repository.SessionRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public class SessionService {
    private static final SessionRepository sessionRepository = new SessionRepository();
    public void deleteSessionsIfTimeIsUp(){
        sessionRepository.deleteSessionsIfTimeIsUp();
    }
    public boolean isSessionExpired(Session session){
        return session.getExpiresat().isAfter(LocalDateTime.now());
    }
    public Optional<Session> getSessionById(String id){
        return sessionRepository.findById(id);
    }
}

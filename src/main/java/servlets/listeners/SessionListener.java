package servlets.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import model.session.service.SessionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
@Slf4j
public class SessionListener implements ServletContextListener {
   private static final SessionService sessionService = new SessionService();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Deleting expired sessions");
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(sessionService::deleteSessionsIfTimeIsUp,0,24, TimeUnit.HOURS);
    }
}

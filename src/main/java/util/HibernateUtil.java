package util;

import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {
    private SessionFactory sessionFactory;

    public Session getSession() {
        return getSessionFactory().getCurrentSession();
    }

    private SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            var configuration = buildConfiguration();
            configuration.configure();
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }

    private Configuration buildConfiguration() {
        var configuration = new Configuration();
        return configuration;
    }
}

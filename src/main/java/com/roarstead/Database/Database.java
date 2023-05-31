package com.roarstead.Database;

import com.roarstead.Exception.SessionIsClosedException;
import com.roarstead.Exception.SessionIsOpenException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Database {
    private Session session;
    private boolean sessionIsOpen;
    private Configuration configuration;
    private SessionFactory sessionFactory;

    public Database() {
        configuration = new Configuration();
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
        sessionIsOpen = false;
    }

    public Session getSession() {
        return session;
    }
    public void setSession(Session session) {
        this.session = session;
    }

    public void openSession() {
        if (!sessionIsOpen) {
            setSession(sessionFactory.openSession());
            sessionIsOpen = true;
        }
        else
            throw new SessionIsOpenException();
    }

    public void closeSession() {
        if (sessionIsOpen) {
            session.close();
            sessionIsOpen = false;
        }
        else
            throw new SessionIsClosedException();
    }
}

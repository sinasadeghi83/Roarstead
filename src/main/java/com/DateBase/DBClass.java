package com.DateBase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBClass {
    private Session session;
    private boolean sessionIsOpen;
    private Configuration configuration;
    private SessionFactory sessionFactory;

    public DBClass() {
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
        setSession(sessionFactory.openSession());
        sessionIsOpen = true;
    }

    public void closeSession() {
        session.close();
        sessionIsOpen = false;
    }
}

package com.roarstead.Components.Database;

import com.roarstead.Components.Auth.Models.Auth;
import com.roarstead.Components.Auth.Models.Permission;
import com.roarstead.Components.Auth.Models.Role;
import com.roarstead.Components.Exceptions.SessionAlreadyClosedException;
import com.roarstead.Components.Exceptions.SessionAlreadyOpenedException;
import com.roarstead.Components.Exceptions.TransactionAlreadyClosedException;
import com.roarstead.Components.Exceptions.TransactionAlreadyHasBegun;
import com.roarstead.Models.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class Database {
    private Session session;
    private Transaction transaction;
    private boolean transactionHasBegun;
    private boolean sessionIsOpen;
    private Configuration configuration;
    private SessionFactory sessionFactory;
    private CriteriaBuilder criteriaBuilder;

    public Database() {
        configuration = new Configuration();
        configuration.configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .configure()
                .build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        sessionIsOpen = false;
        transactionHasBegun = false;
    }

    public Session getSession() {
        return session;
    }
    public void setSession(Session session) {
        this.session = session;
    }

    public void openSession() throws SessionAlreadyOpenedException{
        if (!sessionIsOpen) {
            setSession(sessionFactory.openSession());
            criteriaBuilder = session.getCriteriaBuilder();
            sessionIsOpen = true;
        }
        else
            throw new SessionAlreadyOpenedException();
    }

    public void openSessionIfNotOpened() {
        try{
            openSession();
        }catch (SessionAlreadyOpenedException e){
            System.out.println("Session already open, ignore.");
        }
    }

    public void closeSession() throws SessionAlreadyClosedException {
        if (sessionIsOpen) {
            session.close();
            criteriaBuilder = null;
            sessionIsOpen = false;
        }
        else
            throw new SessionAlreadyClosedException();
    }

    public void closeSessionIfNotClosed() {
        try {
            closeSession();
        }catch (SessionAlreadyClosedException e){
            System.out.println("Session already closed, ignore.");
        }
    }

    public void ready() {
        openSessionIfNotOpened();
        beginTransactionIfNotBegun();
    }

    public void done() {
        commitTransactionIfNotCommited();
    }

    public void beginTransactionIfNotBegun() {
        try {
            beginTransaction();
        }catch (TransactionAlreadyHasBegun e){
            System.out.println("Transaction already has begun, ignore.");
        }
    }

    public void beginTransaction() throws TransactionAlreadyHasBegun {
        if(transactionHasBegun){
            throw new TransactionAlreadyHasBegun();
        }

        transaction = session.beginTransaction();
        transactionHasBegun = true;
    }

    public void commitTransactionIfNotCommited(){
        try{
            commitTransaction();
        }catch (TransactionAlreadyClosedException e){
            System.out.println("Transaction already commited, ignore.");
        }
    }

    public void commitTransaction() throws TransactionAlreadyClosedException {
        if(!transactionHasBegun){
            throw new TransactionAlreadyClosedException();
        }
        transaction.commit();
        transactionHasBegun = false;
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return criteriaBuilder;
    }
}

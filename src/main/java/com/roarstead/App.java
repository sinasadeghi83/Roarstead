package com.roarstead;

import com.DateBase.DBClass;
import com.Model.User;
import org.hibernate.Transaction;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        DBClass db = new DBClass();
        db.openSession();
        User user = new User("amirhossein");
        Transaction transaction = db.getSession().getTransaction();
        transaction.begin();
        db.getSession().save(user);
        transaction.commit();
        db.closeSession();
    }
}

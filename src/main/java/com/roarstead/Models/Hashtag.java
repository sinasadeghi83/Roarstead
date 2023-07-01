package com.roarstead.Models;

import com.roarstead.App;
import com.roarstead.Components.Annotation.Exclude;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Exceptions.TransactionAlreadyHasBegun;
import jakarta.persistence.*;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, updatable = false)
    private String name;

    @Exclude
    @ManyToMany(mappedBy = "hashtags")
    Set<Roar> roars;

    public Hashtag(){}

    public Hashtag(String name) {
        this.name = name;
    }

    public static Set<Hashtag> extractHashtags(String text) {
        Set<Hashtag> hashtags = new HashSet<>();

        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String hashtagStr = matcher.group().substring(1); // Exclude the '#' symbol
            Hashtag hashtag = returnOrCreateHashtag(hashtagStr);
            hashtags.add(hashtag);
        }

        return hashtags;
    }

    public static Hashtag returnOrCreateHashtag(String hashtagStr) {
        Database db = App.getCurrentApp().getDb();

        Hashtag hashtag = db.getSession().createQuery("FROM Hashtag WHERE name=:name", Hashtag.class).setParameter("name", hashtagStr).getSingleResultOrNull();
        if(hashtag != null)
            return hashtag;

        hashtag = new Hashtag(hashtagStr);
        db.getSession().persist(hashtag);
        return hashtag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Roar> getRoars() {
        return roars;
    }

    public void setRoars(Set<Roar> roars) {
        this.roars = roars;
    }
}

package com.roarstead.Models;

import com.google.gson.annotations.SerializedName;
import com.roarstead.App;
import com.roarstead.Components.Annotation.Exclude;
import com.roarstead.Components.Auth.Models.Auth;
import com.roarstead.Components.Business.Models.Country;
import com.roarstead.Components.Exceptions.BadRequestException;
import com.roarstead.Components.Exceptions.NotAuthenticatedException;
import com.roarstead.Components.Exceptions.NotFoundException;
import jakarta.persistence.Column;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends Auth {

    private static final String INVALID_USER_MESSAGE = "Invalid User";
    @Column(name = "first_name", nullable = false)
    @SerializedName("first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @SerializedName("last_name")
    private String lastName;

    @Column(unique = true)
    private String email;     //unique and formatting
    @Column(unique = true)
    private String phone;

    @Embedded
    private Country country;

    @Temporal(TemporalType.DATE)
    @Column(name="birth_date", nullable = false)
    @SerializedName("birth_date")
    private Date birthDate;

    @Embedded
    private Profile profile;

    @Exclude
    @ManyToMany
    @JoinTable(
            name = "following",
            joinColumns = @JoinColumn(name="follower_id"),
            inverseJoinColumns = @JoinColumn(name="following_id")
    )
    Set<User> followings;

    @Exclude
    @ManyToMany(mappedBy = "followings")
    Set<User> followers;

//    @ManyToMany
//    @JoinTable(name = "roar_like", joinColumns = @JoinColumn(name = "username"), inverseJoinColumns = @JoinColumn(name = "roar-id"))
//    private Set<Roar> likedRoars;

    public User() {

    }

    public User(String username, String firstName, String lastName, String email, String phone,Country country, String password, Date birthDate) {
        super(password);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.country = country;
        this.birthDate = birthDate;
    }

    @Override
    public void enterPassword(String password) {
        setPassword(password);
    }
  
//    public void like(Roar roar) {
//        likedRoars.add(roar);
//    }
//
//    public void unLike(Roar roar) {
//        likedRoars.remove(roar);
//    }

    @Override
    public Auth identity() throws NotAuthenticatedException {
        return this;
    }

    @PrePersist
    protected void onCreate() {
        profile = new Profile();
        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        profile.setCreatedAt(currentDate);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public void addFollower(User follower){
        if(followers == null)
            followers = new HashSet<>();
        followers.add(follower);
    }

    public Set<User> getFollowings() {
        return followings;
    }

    public void setFollowings(Set<User> followings) {
        this.followings = followings;
    }

    public void addFollowing(User following) throws BadRequestException {
        if(following == null)
            followings = new HashSet<>();
        if(following == null || following.getId() == id)
            throw new BadRequestException(INVALID_USER_MESSAGE);
        followings.add(following);
    }

    //Add user with id = followingId to the following list of the logged-in user
    //Returns followed user
    public User addFollowing(int followingId) throws NotFoundException, BadRequestException {
        User following = App.getCurrentApp().getDb().getSession()
                .createQuery("FROM User u WHERE u.id=:id", User.class)
                .setParameter("id", followingId)
                .getSingleResultOrNull();
        //Throws exception if user does not exist
        if(following == null)
            throw new NotFoundException();
        addFollowing(following);
        return following;
    }

    public void removeFollowing(User following) throws BadRequestException {
        if(following == null)
            followings = new HashSet<>();
        if(following == null || following.getId() == id || !followings.contains(following))
            throw new BadRequestException(INVALID_USER_MESSAGE);
        followings.remove(following);
    }

    public User removeFollowing(int followingId) throws NotFoundException, BadRequestException {
        User following = App.getCurrentApp().getDb().getSession()
                .createQuery("FROM User u WHERE u.id=:id", User.class)
                .setParameter("id", followingId)
                .getSingleResultOrNull();
        //Throws exception if user does not exist
        if(following == null)
            throw new NotFoundException();
        removeFollowing(following);
        return following;
    }
}
package com.roarstead.Models;

import com.google.gson.annotations.SerializedName;
import com.roarstead.App;
import com.roarstead.Components.Annotation.Exclude;
import com.roarstead.Components.Auth.Models.Auth;
import com.roarstead.Components.Business.Models.Country;
import com.roarstead.Components.Exceptions.BadRequestException;
import com.roarstead.Components.Exceptions.ForbiddenException;
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
    private static final String FORBIDDEN_BLOCKED_ERR = "Sorry! You have been blocked by this user.";

    @Column(name = "first_name", nullable = false)
    @SerializedName("first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @SerializedName("last_name")
    private String lastName;

    @Exclude
    @Column(unique = true)
    private String email;     //unique and formatting

    @Exclude
    @Column(unique = true)
    private String phone;

    @Exclude
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

    @Exclude
    @OneToMany(mappedBy = "sender")
    Set<Roar> roars;

    @Exclude
    @OneToMany(mappedBy = "writer")
    Set<GRoar> groars;

    @Exclude
    @ManyToMany(mappedBy = "usersLiked")
    Set<GRoar> roarsLiked;

    @Exclude
    @OneToMany(mappedBy = "uploader")
    Set<RoarMedia> uploadedMedia;

    @Exclude
    @ManyToMany(mappedBy = "usersLiked")
    Set<GRoar> likedGroars;

    @Exclude
    @ManyToMany
    @JoinTable(
            name = "blacklist",
            joinColumns = @JoinColumn(name = "blocker_id"),
            inverseJoinColumns = @JoinColumn(name = "blocked_id")
    )
    Set<User> blacklist;

    @Exclude
    @ManyToMany(mappedBy = "blacklist")
    Set<User> blockedByList;

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

    public void addFollowing(User following) throws Exception {
        if(followings == null)
            followings = new HashSet<>();

        if(following == null || following.getId() == id)
            throw new BadRequestException(INVALID_USER_MESSAGE);

        if(blockedByList.contains(following))
            throw new ForbiddenException(FORBIDDEN_BLOCKED_ERR);

        followings.add(following);
    }

    //Add user with id = followingId to the following list of the logged-in user
    //Returns followed user
    public User addFollowing(int followingId) throws Exception {
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
        if(following == null || following.getId() == id)
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

    public User blockUser(int userId) throws Exception{
        User blockingUser = App.getCurrentApp().getDb().getSession()
                .createQuery("FROM User u WHERE u.id=:id", User.class)
                .setParameter("id", userId)
                .getSingleResultOrNull();
        //Throws exception if user does not exist
        if(blockingUser == null)
            throw new NotFoundException();
        blockUser(blockingUser);
        return blockingUser;
    }

    public void blockUser(User blockingUser) throws Exception {
        if(blacklist == null)
            blacklist = new HashSet<>();

        if(blockingUser == null || blockingUser.getId() == id)
            throw new BadRequestException(INVALID_USER_MESSAGE);

        removeFollowing(blockingUser);
        blockingUser.removeFollowing(this);
        blacklist.add(blockingUser);
    }

    public Set<Roar> getRoars() {
        return roars;
    }

    public void setRoars(Set<Roar> roars) {
        this.roars = roars;
    }

    public Set<GRoar> getGroars() {
        return groars;
    }

    public void setGroars(Set<GRoar> groars) {
        this.groars = groars;
    }

    public Set<GRoar> getRoarsLiked() {
        return roarsLiked;
    }

    public void setRoarsLiked(Set<GRoar> roarsLiked) {
        this.roarsLiked = roarsLiked;
    }

    public Set<RoarMedia> getUploadedMedia() {
        return uploadedMedia;
    }

    public void setUploadedMedia(Set<RoarMedia> uploadedMedia) {
        this.uploadedMedia = uploadedMedia;
    }

    public Set<GRoar> getLikedGroars() {
        return likedGroars;
    }

    public void setLikedGroars(Set<GRoar> likedGroars) {
        this.likedGroars = likedGroars;
    }

    public Set<User> getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(Set<User> blacklist) {
        this.blacklist = blacklist;
    }

    public Set<User> getBlockedByList() {
        return blockedByList;
    }

    public void setBlockedByList(Set<User> blockedByList) {
        this.blockedByList = blockedByList;
    }
}
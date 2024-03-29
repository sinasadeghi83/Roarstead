package com.roarstead.Controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.roarstead.App;
import com.roarstead.Components.Annotation.POST;
import com.roarstead.Components.Auth.AuthManager;
import com.roarstead.Components.Auth.Models.Role;
import com.roarstead.Components.Business.Models.Country;
import com.roarstead.Components.Controller.BaseController;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Exceptions.*;
import com.roarstead.Components.Resource.Models.Image;
import com.roarstead.Components.Resource.ResourceManager;
import com.roarstead.Components.Response.Pagination;
import com.roarstead.Components.Response.Response;
import com.roarstead.Models.Profile;
import com.roarstead.Models.ProfileForm;
import com.roarstead.Models.User;
import com.roarstead.Models.UserForm;
import jakarta.validation.ConstraintViolation;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserController extends BaseController {

    private static final String USER_ALREADY_SIGNED_UP = "There's already an account with this username or email or phone number";
    private static final String SEARCH_QUERY_KEY = "query";
    private static final String FOLLOWING_ID_KEY = "following_id";
    private static final String DOES_FOLLOW_KEY = "does_follow";
    private static final String REQUESTED_USER_ID = "requested_user_id";
    private static final String JWT_TOKEN_KEY = "token";
    private static final String USER_KEY = "user";
    private static final String BLOCKING_USER_ID = "blocking_user_id";
    private static final String BLOCKED_USER_ID = "blocked_user_id";

    @Override
    public Map<String, List<String>> accessControl() {
        Map<String, List<String>> result = new HashMap<>();

        result.putAll(Map.of(
                "actionIndex", List.of(),
                "actionSignUp", List.of("?"),
                "actionGetToken", List.of("?"),
                "actionUpdateProfileImage", List.of("@"),
                "actionUpdateProfileHeader", List.of("@"),
                "actionUpdateProfile", List.of("@"),
                "actionSearch", List.of("@"),
                "actionFollow", List.of("@"),
                "actionUnfollow", List.of("@"),
                "actionDoesFollow", List.of("@")
        ));

        result.putAll(Map.of(
                "actionGetFollowings", List.of("@"),
                "actionGetFollowers", List.of("@"),
                "actionBlock", List.of("@"),
                "actionUnblock", List.of("@")
        ));

        return result;
    }

    public Response actionIndex(JsonObject requestBody) {
        return new Response("Yay! It works!", Response.OK);
    }

    // TODO: Creating put annotation
    public Response actionUnblock() throws Exception{
        Map<String, String> params = App.getCurrentApp().getQueryParams();
        int blockedUserId = Integer.parseInt(params.get(BLOCKED_USER_ID));

        Database db = App.getCurrentApp().getDb();
        User user = (User) App.getCurrentApp().getAuthManager().identity();

        db.ready();
        User blockedUser = user.unblockUser(blockedUserId);
        db.getSession().merge(user);
        db.done();

        return new Response(blockedUser, Response.OK);
    }

    // TODO: Creating put annotation
    public Response actionBlock() throws Exception{
        Map<String, String> params = App.getCurrentApp().getQueryParams();
        int blockingUserId = Integer.parseInt(params.get(BLOCKING_USER_ID));

        Database db = App.getCurrentApp().getDb();
        User user = (User) App.getCurrentApp().getAuthManager().identity();

        db.ready();
        User blockedUser = user.blockUser(blockingUserId);
        db.getSession().merge(user);
        db.done();

        return new Response(blockedUser, Response.OK);
    }

    public Response actionGetFollowers() throws Exception{
        Database db = App.getCurrentApp().getDb();

        Pagination<User> userPagination = new Pagination<>(App.getCurrentApp().getQueryParams());
        String queryString = "SELECT f FROM User u JOIN u.followers f WHERE u.id=:userId";
        String countQueryString = "SELECT COUNT(f) FROM User u JOIN u.followers f WHERE u.id=:userId";
        Map<String, Object> queryParams = Map.of("userId", App.getCurrentApp().getAuthManager().getUserId());
        userPagination.setQuery(queryString, queryParams);
        userPagination.setCountQuery(countQueryString, queryParams);

        return new Response(userPagination.getJsonResult(), Response.OK);
    }

    public Response actionGetFollowings() throws Exception{
        Database db = App.getCurrentApp().getDb();

        Pagination<User> userPagination = new Pagination<>(App.getCurrentApp().getQueryParams());
        String queryString = "SELECT f FROM User u JOIN u.followings f WHERE u.id=:userId";
        String countQueryString = "SELECT COUNT(f) FROM User u JOIN u.followings f WHERE u.id=:userId";
        Map<String, Object> queryParams = Map.of("userId", App.getCurrentApp().getAuthManager().getUserId());
        userPagination.setQuery(queryString, queryParams);
        userPagination.setCountQuery(countQueryString, queryParams);

        return new Response(userPagination.getJsonResult(), Response.OK);
    }

    public Response actionDoesFollow() throws Exception{
        Database db = App.getCurrentApp().getDb();
        //Get request user id to check
        int requestedId = Integer.parseInt(App.getCurrentApp().getQueryParams().get(REQUESTED_USER_ID));

        long result = db.getSession()
                .createQuery("SELECT count(u.id) FROM User u JOIN u.followings f WHERE u.id=:userId AND f.id =:requestedId", Long.class)
                .setParameter("requestedId", requestedId)
                .setParameter("userId", App.getCurrentApp().getAuthManager().getUserId())
                .getSingleResult();

        return new Response(Map.of(DOES_FOLLOW_KEY, result > 0), Response.OK);
    }

    //TODO: Creating PUT annotation
    public Response actionUnfollow() throws Exception {
        Database db = App.getCurrentApp().getDb();

        //Get following user
        int followingId = Integer.parseInt(App.getCurrentApp().getQueryParams().get(FOLLOWING_ID_KEY));

        //Gets logged in user
        AuthManager authManager = App.getCurrentApp().getAuthManager();
        User user = (User) authManager.identity();

        //Remove from the followings
        db.ready();
        User following = user.removeFollowing(followingId);
        db.getSession().merge(user);
        db.done();

        //Returns unfollowed user with OK response
        return new Response(following, Response.OK);
    }

    //TODO: Creating PUT annotation
    public Response actionFollow() throws Exception {
        Database db = App.getCurrentApp().getDb();

        //Get following user
        int followingId = Integer.parseInt(App.getCurrentApp().getQueryParams().get(FOLLOWING_ID_KEY));

        //Gets logged in user
        AuthManager authManager = App.getCurrentApp().getAuthManager();
        User user = (User) authManager.identity();

        //Add to the followings
        db.ready();
        User following = user.addFollowing(followingId);
        db.getSession().merge(user);
        db.done();

        //Returns followed user with OK response
        return new Response(following, Response.OK);
    }

    public Response actionSearch() throws Exception {
        Map<String, String> params = App.getCurrentApp().getQueryParams();
        String searchQuery = params.get(SEARCH_QUERY_KEY);
        Pagination<User> pagination = new Pagination<>(params);
        Map<String, Object> queryParams = Map.of(
                "firstName", searchQuery,
                "lastName", searchQuery,
                "username", searchQuery
        );
        String queryString = "FROM User u WHERE " +
                "u.firstName LIKE CONCAT('%', :firstName, '%') OR " +
                "u.lastName LIKE CONCAT('%', :lastName, '%') OR " +
                "u.username LIKE CONCAT('%', :username, '%')";
        pagination.setQuery(queryString, queryParams);
        pagination.setCountQuery("SELECT count(u.id) " + queryString, queryParams);
        return new Response(pagination.getJsonResult(), Response.OK);
    }

    @POST
    public Response actionSignUp(JsonObject requestBody) throws Exception {
        Gson gson = App.getCurrentApp().getGson();
        UserForm userForm;
        userForm = gson.fromJson(requestBody, UserForm.class);
        if (!userForm.validate()) {
            Set<ConstraintViolation<UserForm>> violations = userForm.getViolations();
            String[] messages = new String[violations.size()];
            int i = 0;
            for (ConstraintViolation<UserForm> violation :
                    violations) {
                messages[i++] = violation.getMessage();
            }
            throw new UnprocessableEntityException(messages[0]);
        }
        Database db = App.getCurrentApp().getDb();
        long userCount = (Long) db.getSession()
                .createQuery("SELECT count(u) FROM User u WHERE u.username=:username OR u.email=:email OR u.phone=:phone")
                .setParameter("username", userForm.getUsername())
                .setParameter("email", userForm.getEmail())
                .setParameter("phone", userForm.getPhone()).getSingleResult();
        if (userCount > 0)
            throw new ConflictException(USER_ALREADY_SIGNED_UP);
        db.ready();
        User user = new User(userForm.getUsername(), userForm.getFirstName(), userForm.getLastName(), userForm.getEmail(), userForm.getPhone(), Country.getCountryByDialCode(userForm.getDialCode()), userForm.getPassword(), userForm.getBirthDate());
        user.setProfile(new Profile());
        db.getSession().persist(user);
        db.done();
        App.getCurrentApp().getAuthManager().assignRole(user, Role.findRoleByName(Role.DEFAULT_NAME));
        JsonObject userJson = (JsonObject) gson.toJsonTree(user);
        userJson.addProperty("phone", user.getPhone());
        userJson.addProperty("email", user.getEmail());
        userJson.add("country", gson.toJsonTree(user.getCountry()));
        return new Response(userJson, Response.OK);
    }

    @POST
    public Response actionGetToken(JsonObject requestBody) throws Exception {
        AuthManager authManager = App.getCurrentApp().getAuthManager();
        authManager.authenticate(requestBody.get("username").getAsString(), requestBody.get("password").getAsString());
        String token = authManager.generateJWT();
        Map<String, Object> responseBody = new HashMap<>();
        User user = (User) authManager.identity();
        JsonObject userJson = (JsonObject) gson.toJsonTree(user);
        userJson.addProperty("phone", user.getPhone());
        userJson.addProperty("email", user.getEmail());
        userJson.add("country", gson.toJsonTree(user.getCountry()));
        responseBody.put(JWT_TOKEN_KEY, token);
        responseBody.put(USER_KEY, userJson);
        return new Response(responseBody, Response.OK);
    }

    //TODO : make a PUT anotation
    @POST
    public Response actionUpdateProfileImage() throws Exception {
        //Validate file size
        Profile.validateSizeForAvatarImage(0);

        ResourceManager resourceManager = App.getCurrentApp().getResourceManager();
        Database db = App.getCurrentApp().getDb();

        //Retrieve uploaded image
        Image image;
        try {
            db.ready();
            image = resourceManager.createImageFromFileItem(0);
            db.getSession().persist(image);
            db.done();
        } catch (FileModelIsNotAnImageException e){
            resourceManager.deleteFileModel(e.getFileModel());
            throw new UnprocessableEntityException("File is not an image!");
        }catch (Exception e) {
            throw new BadRequestException();
        }

        try {
            Profile.validateForAvatarImage(image);
        }catch (Exception e){
            resourceManager.deleteImage(image);
            throw e;
        }

        AuthManager authManager = App.getCurrentApp().getAuthManager();
        User user = (User) authManager.identity();
        Profile profile = user.getProfile();

        if(profile == null){
            profile = new Profile();
        }else{
            profile.deleteProfileImage(user);
        }

        db.ready();
        profile.setProfImage(image);
        db.getSession().merge(user);
        db.done();

        return new Response(Response.OK_UPLOAD, Response.OK);
    }

    //TODO : make a PUT anotation
    @POST
    public Response actionUpdateProfileHeader() throws Exception {
        //Validate file size
        Profile.validateSizeForHeaderImage(0);

        ResourceManager resourceManager = App.getCurrentApp().getResourceManager();
        Database db = App.getCurrentApp().getDb();

        //Retrieve uploaded image
        Image image;
        try {
            db.ready();
            image = resourceManager.createImageFromFileItem(0);
            db.getSession().persist(image);
            db.done();
        } catch (FileModelIsNotAnImageException e){
            resourceManager.deleteFileModel(e.getFileModel());
            throw new UnprocessableEntityException("File is not an image!");
        } catch (Exception e) {
            throw new BadRequestException();
        }

        try {
            Profile.validateForHeaderImage(image);
        }catch (Exception e){
            resourceManager.deleteImage(image);
            throw e;
        }

        AuthManager authManager = App.getCurrentApp().getAuthManager();
        User user = (User) authManager.identity();
        Profile profile = user.getProfile();

        if(profile == null){
            profile = new Profile();
        }else{
            profile.deleteHeaderImage(user);
        }

        db.ready();
        profile.setHeaderImage(image);
        db.getSession().merge(user);
        db.done();

        return new Response(Response.OK_UPLOAD, Response.OK);
    }

    //TODO: make a PATCH annotation
    @POST
    public Response actionUpdateProfile(JsonObject jsonObject) throws Exception {
        Database db = App.getCurrentApp().getDb();

        //Get user profile
        User user = (User) App.getCurrentApp().getAuthManager().identity();
        Profile profile = user.getProfile();

        //Getting data to be updated
        ProfileForm profileForm = gson.fromJson(jsonObject, ProfileForm.class);

        //Validating data
        if (!profileForm.validate()) {
            Set<ConstraintViolation<ProfileForm>> violations = profileForm.getViolations();
            String[] messages = new String[violations.size()];
            int i = 0;
            for (ConstraintViolation<ProfileForm> violation :
                    violations) {
                messages[i++] = violation.getMessage();
            }
            throw new UnprocessableEntityException(messages[0]);
        }

        //Updating data
        if(profileForm.getBio() != null)
            profile.setBio(profileForm.getBio());

        if(profileForm.getLocation() != null)
            profile.setLocation(profileForm.getLocation());

        if(profileForm.getWebsiteLink() != null)
            profile.setWebsiteLink(new URI(profileForm.getWebsiteLink()).toURL());

        //Updating database
        db.ready();
        db.getSession().merge(user);
        db.done();

        //Returning updated profile
        return new Response(profile.toJson(), Response.OK);
    }
}

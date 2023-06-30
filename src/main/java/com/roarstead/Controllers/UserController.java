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
import org.hibernate.query.Query;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserController extends BaseController {

    private static final String USER_ALREADY_SIGNED_UP = "There's already an account with this username or email or phone number";
    private static final String SEARCH_QUERY_KEY = "query";
    private static final String FOLLOWING_ID_KEY = "following_id";

    @Override
    public Map<String, List<String>> accessControl() {
        return Map.of(
                "actionIndex", List.of(),
                "actionSignUp", List.of("?"),
                "actionGetToken", List.of("?"),
                "actionUpdateProfileImage", List.of("@"),
                "actionUpdateProfileHeader", List.of("@"),
                "actionUpdateProfile", List.of("@"),
                "actionSearch", List.of("@"),
                "actionFollow", List.of("@")
        );
    }

    public Response actionIndex(JsonObject requestBody) {
        return new Response("Yay! It works!", Response.OK);
    }

    //TODO: Creating PUT annotation
    public Response actionFollow() throws HttpException {
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

    public Response actionSearch() throws NotFoundException {
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
    public Response actionSignUp(JsonObject requestBody) throws HttpException {
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
        db.getSession().save(user);
        db.done();
        App.getCurrentApp().getAuthManager().assignRole(user, Role.findRoleByName(Role.DEFAULT_NAME));
        JsonObject userRaw = (JsonObject) gson.toJsonTree(userForm);
        userRaw.addProperty("id", user.getId());
        userRaw.addProperty("country", user.getCountry().getName());
        userRaw.add("created_at", gson.toJsonTree(user.getProfile().getCreatedAt()));
        return new Response(userRaw, Response.OK);
    }

    @POST
    public Response actionGetToken(JsonObject requestBody) throws InvalidCredentialsException {
        AuthManager authManager = App.getCurrentApp().getAuthManager();
        authManager.authenticate(requestBody.get("username").getAsString(), requestBody.get("password").getAsString());
        String token = authManager.generateJWT();
        JsonObject responseBody = App.getCurrentApp().getGson().fromJson("{\"token\":\"" + token + "\"}", JsonObject.class);
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
    public Response actionUpdateProfile(JsonObject jsonObject) throws NotAuthenticatedException, MalformedURLException, URISyntaxException, UnprocessableEntityException {
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

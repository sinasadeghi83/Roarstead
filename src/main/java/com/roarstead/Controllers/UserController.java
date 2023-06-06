package com.roarstead.Controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.roarstead.App;
import com.roarstead.Components.Annotation.POST;
import com.roarstead.Components.Auth.Models.Role;
import com.roarstead.Components.Business.Models.Country;
import com.roarstead.Components.Controller.BaseController;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Response.Response;
import com.roarstead.Models.User;
import com.roarstead.Models.UserForm;
import jakarta.validation.ConstraintViolation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserController extends BaseController {

    private static final String USER_ALREADY_SIGNED_UP = "There's already an account with this username or email or phone number";

    @Override
    public Map<String, List<String>> accessControl() {
        return Map.of(
                "actionIndex", List.of(),
                "actionSignUp", List.of("?")
        );
    }

    public Response actionIndex(JsonObject requestBody){
        return new Response("Yay! It works!", Response.OK);
    }

    @POST
    public Response actionSignUp(JsonObject requestBody){
        Gson gson = App.getCurrentApp().getGson();
        UserForm userForm;
        userForm = gson.fromJson(requestBody, UserForm.class);
        if(!userForm.validate()){
            Set<ConstraintViolation<UserForm>> violations = userForm.getViolations();
            String[] messages = new String[violations.size()];
            int i = 0;
            for (ConstraintViolation<UserForm> violation :
                    violations) {
                messages[i++] = violation.getMessage();
            }
            return new Response(messages, Response.UNPROCESSABLE_ENTITY);
        }
        Database db = App.getCurrentApp().getDb();
        long userCount = (Long) db.getSession()
                    .createQuery("SELECT count(u) FROM User u WHERE u.username=:username OR u.email=:email OR u.phone=:phone")
                    .setParameter("username", userForm.getUsername())
                    .setParameter("email", userForm.getEmail())
                    .setParameter("phone", userForm.getPhone()).getSingleResult();
        if(userCount > 0)
            return new Response(USER_ALREADY_SIGNED_UP, Response.CONFLICT);
        db.ready();
        User user = new User(userForm.getUsername(), userForm.getFirstName(), userForm.getLastName(), userForm.getEmail(), userForm.getPhone(), Country.getCountryByDialCode(userForm.getDialCode()), userForm.getPassword(), userForm.getBirthDate());
        db.getSession().save(user);
        db.done();
        App.getCurrentApp().getAuthManager().assignRole(user, Role.findRoleByName(Role.DEFAULT_NAME));
        user.setPassword(null);
        user.setRoles(null);
        return new Response(user, Response.OK);
    }
}

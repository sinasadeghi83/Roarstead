package com.roarstead.Controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.roarstead.App;
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

    public Response actionSignUp(JsonObject requestBody){
        Gson gson = App.getCurrentApp().getGson();
        UserForm userForm;
        userForm = gson.fromJson(requestBody, UserForm.class);
        if(!userForm.validate()){
            Set<ConstraintViolation<UserForm>> violations = userForm.getViolations();
            StringBuilder message = new StringBuilder();
            message.append(Response.UNPROCESSABLE_ENTITY_MSG);
            message.append('\n');
            for (ConstraintViolation<UserForm> violation :
                    violations) {
                message.append(violation.getMessage());
            }
            return new Response(message.toString(), Response.UNPROCESSABLE_ENTITY);
        }

        Database db = App.getCurrentApp().getDb();
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

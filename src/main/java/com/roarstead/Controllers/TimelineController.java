package com.roarstead.Controllers;

import com.roarstead.App;
import com.roarstead.Components.Controller.BaseController;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Exceptions.NotAuthenticatedException;
import com.roarstead.Components.Response.Response;
import com.roarstead.Models.Roar;
import com.roarstead.Models.User;

import java.util.List;
import java.util.Map;

public class TimelineController extends BaseController {
    @Override
    public Map<String, List<String>> accessControl() {
        return Map.of(
            "actionFollowing", List.of("@")
        );
    }

    public Response actionFollowing() throws Exception {
        User user = (User) App.getCurrentApp().getAuthManager().identity();
        Database db = App.getCurrentApp().getDb();

        List<Roar> roars = db.getSession()
                .createQuery("FROM Roar r WHERE r.sender IN (:followings)", Roar.class)
                .setParameterList("followings", user.getFollowings())
                .getResultList();

        return new Response(roars, Response.OK);
    }
}

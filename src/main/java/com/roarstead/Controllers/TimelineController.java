package com.roarstead.Controllers;

import com.roarstead.App;
import com.roarstead.Components.Controller.BaseController;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Exceptions.NotAuthenticatedException;
import com.roarstead.Components.Response.Response;
import com.roarstead.Models.GRoar;
import com.roarstead.Models.Roar;
import com.roarstead.Models.User;

import java.util.List;
import java.util.Map;

public class TimelineController extends BaseController {
    private static final int FAV_STAR_LIMIT = 10;

    @Override
    public Map<String, List<String>> accessControl() {
        return Map.of(
            "actionFollowing", List.of("@"),
                "actionForYou", List.of("@")
        );
    }

    public Response actionForYou() throws Exception {
        User user = (User) App.getCurrentApp().getAuthManager().identity();
        Database db = App.getCurrentApp().getDb();

        List<Integer> favStars = db.getSession()
                .createQuery("SELECT r.id FROM GRoar r WHERE (SELECT COUNT(ul) FROM r.usersLiked ul WHERE ul.id = r.id) >= :favLimit", Integer.class)
                .setParameter("favLimit", FAV_STAR_LIMIT)
                .getResultList();

        List<Roar> roars = db.getSession()
                .createQuery("FROM Roar r WHERE r.sender IN (:followings) OR r.id IN (:favStars) ORDER BY r.createdAt DESC", Roar.class)
                .setParameterList("followings", user.getFollowings())
                .setParameterList("favStars", favStars)
                .getResultList();

        return new Response(roars, Response.OK);
    }

    public Response actionFollowing() throws Exception {
        User user = (User) App.getCurrentApp().getAuthManager().identity();
        Database db = App.getCurrentApp().getDb();

        List<Roar> roars = db.getSession()
                .createQuery("FROM Roar r WHERE r.sender IN (:followings) ORDER BY r.createdAt DESC", Roar.class)
                .setParameterList("followings", user.getFollowings())
                .getResultList();

        return new Response(roars, Response.OK);
    }
}

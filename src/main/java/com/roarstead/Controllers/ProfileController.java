package com.roarstead.Controllers;

import com.roarstead.App;
import com.roarstead.Components.Controller.BaseController;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Exceptions.ForbiddenException;
import com.roarstead.Components.Exceptions.NotFoundException;
import com.roarstead.Components.Resource.Models.Image;
import com.roarstead.Components.Response.Response;
import com.roarstead.Models.User;

import java.util.List;
import java.util.Map;

public class ProfileController extends BaseController {
    private static final String USER_ID_KEY = "user_id";

    @Override
    public Map<String, List<String>> accessControl() {
        return Map.of(
                "actionAvatar", List.of("@"),
                "actionImageHeader", List.of("@")
        );
    }

    public Response actionImageHeader() throws Exception{
        Map<String, String> params = App.getCurrentApp().getQueryParams();
        int userId = Integer.parseInt(params.get(USER_ID_KEY));

        Database db = App.getCurrentApp().getDb();
        User user = (User) App.getCurrentApp().getAuthManager().identity();

        User wantedUser = db.getSession()
                .createQuery("FROM User WHERE id=:userId", User.class)
                .setParameter("userId", userId)
                .getSingleResultOrNull();
        if(user == null)
            throw new NotFoundException();

        if(user.getBlockedByList().contains(wantedUser)){
            throw new ForbiddenException(User.FORBIDDEN_BLOCKED_ERR);
        }

        Image imageHeader = wantedUser.getProfile().getHeaderImage();
        String extension = imageHeader != null ? imageHeader.getExtension() : "png";
        return new Response(App.getCurrentApp().getResourceManager().convertImageToBase64(imageHeader), Response.OK, "image/" + extension);
    }

    public Response actionAvatar() throws Exception{
        Map<String, String> params = App.getCurrentApp().getQueryParams();
        int userId = Integer.parseInt(params.get(USER_ID_KEY));

        Database db = App.getCurrentApp().getDb();
        User user = (User) App.getCurrentApp().getAuthManager().identity();

        User wantedUser = db.getSession()
                .createQuery("FROM User WHERE id=:userId", User.class)
                .setParameter("userId", userId)
                .getSingleResultOrNull();
        if(user == null)
            throw new NotFoundException();

        if(user.getBlockedByList().contains(wantedUser)){
            throw new ForbiddenException(User.FORBIDDEN_BLOCKED_ERR);
        }

        Image avatar = wantedUser.getProfile().getProfImage();
        String extension = avatar != null ? avatar.getExtension() : "png";
        return new Response(App.getCurrentApp().getResourceManager().convertImageToBase64(avatar), Response.OK, "image/" + extension);
    }
}

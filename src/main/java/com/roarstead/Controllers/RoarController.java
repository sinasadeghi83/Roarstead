package com.roarstead.Controllers;

import com.google.gson.JsonObject;
import com.roarstead.App;
import com.roarstead.Components.Annotation.POST;
import com.roarstead.Components.Controller.BaseController;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Exceptions.BadRequestException;
import com.roarstead.Components.Exceptions.FileModelIsNotAnImageException;
import com.roarstead.Components.Exceptions.NotFoundException;
import com.roarstead.Components.Exceptions.UnprocessableEntityException;
import com.roarstead.Components.Resource.Models.Image;
import com.roarstead.Components.Resource.ResourceManager;
import com.roarstead.Components.Response.Response;
import com.roarstead.Models.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoarController extends BaseController {
    private static final String MEDIA_TYPE_KEY = "media_type";
    private static final String ALT_TEXT_KEY = "alt_text";
    private static final String GROAR_ID_KEY = "groar_id";
    private static final String REROARED_ID_KEY = "reroared_id";
    private static final String REROARING_SELF_ERR = "You can't Reroar yourself!";
    private static final String QUOTED_ROAR_KEY = "quoted_roar_id";
    private static final String REPLY_TO_KEY = "reply_to";

    @Override
    public Map<String, List<String>> accessControl() {
        return Map.of(
                "actionRoar", List.of("@"),
                "actionRoarImage", List.of("@"),
                "actionQuoteRoar", List.of("@")
        );
    }

    @POST
    public Response actionQuoteRoar(JsonObject requestBody) throws Exception{
        int quotedId = requestBody.get(QUOTED_ROAR_KEY).getAsInt();
        Database db = App.getCurrentApp().getDb();

        GRoar quotedRoar = db.getSession()
                .createQuery("FROM GRoar WHERE id=:id", GRoar.class)
                .setParameter("id", quotedId)
                .getSingleResultOrNull();
        if(quotedRoar == null)
            throw new NotFoundException();

        User user = (User) App.getCurrentApp().getAuthManager().identity();
        GRoarForm roarForm = gson.fromJson(requestBody, GRoarForm.class);

        GRoar replyTo = db.getSession()
                .createQuery("FROM GRoar WHERE id=:id", GRoar.class)
                .setParameter("id", roarForm.getReplyTo())
                .getSingleResultOrNull();

        db.ready();
        QRoar qroar = new QRoar(user, roarForm.getText(), replyTo, quotedRoar);
        db.getSession().persist(qroar);
        db.done();

        return new Response(qroar, Response.OK);
    }

    @POST
    public Response actionReroar(JsonObject requestBody) throws Exception{
        int reroaredId = requestBody.get(REROARED_ID_KEY).getAsInt();

        Database db = App.getCurrentApp().getDb();
        GRoar reroared = db.getSession()
                .createQuery("FROM GRoar WHERE id=:id", GRoar.class)
                .setParameter("id", reroaredId)
                .getSingleResultOrNull();
        if(reroared == null)
            throw new NotFoundException();

        User user = (User) App.getCurrentApp().getAuthManager().identity();
        if(reroared.getSender().getId() == user.getId())
            throw new BadRequestException(REROARING_SELF_ERR);

        db.ready();
        Reroar reroar = new Reroar(user, reroared);
        db.getSession().persist(reroar);
        db.done();

        return new Response(reroar, Response.OK);
    }

    @POST
    public Response actionRoarImage() throws Exception{
        //Get parameters
        Map<String, String> params = App.getCurrentApp().getQueryParams();
        RoarMedia.MediaType mediaType = RoarMedia.MediaType.IMAGE;
        String altText = null;
        int groarId = 0;
        int replyToId = 0;
        if(params != null) {
            altText = params.get(ALT_TEXT_KEY);
            groarId = Integer.parseInt(params.get(GROAR_ID_KEY) == null ? "0" : params.get(GROAR_ID_KEY));
            replyToId = Integer.parseInt(params.get(REPLY_TO_KEY) == null ? "0" : params.get(REPLY_TO_KEY));
        }

        //Retrieve image
        ResourceManager resourceManager = App.getCurrentApp().getResourceManager();
        Database db = App.getCurrentApp().getDb();
        Image image = resourceManager.retrieveUploadedImage();

        //Validate image
        try {
            RoarMedia.validateImage(image);
        }catch (Exception e){
            resourceManager.deleteImage(image);
            throw e;
        }

        User user = (User) App.getCurrentApp().getAuthManager().identity();

        GRoar replyTo = db.getSession()
                .createQuery("FROM GRoar WHERE id=:id", GRoar.class)
                .setParameter("id", replyToId)
                .getSingleResultOrNull();

        GRoar roar = App.getCurrentApp().getDb().getSession()
                .createQuery("FROM GRoar WHERE id=:id", GRoar.class)
                .setParameter("id", groarId)
                .getSingleResultOrNull();
        if(roar == null) {
            db.ready();
            roar = new GRoar(user, null, replyTo);
            db.getSession().persist(roar);
            db.done();
        }

        db.ready();
        RoarMedia roarMedia = new RoarMedia(mediaType, altText, user, roar);
        roarMedia.loadMediaWithImage(image);
        db.getSession().persist(roarMedia);
        db.done();

        return new Response(roarMedia, Response.OK);
    }

    @POST
    public Response actionRoar(JsonObject requestBody) throws Exception{
        Database db = App.getCurrentApp().getDb();

        User user = (User) App.getCurrentApp().getAuthManager().identity();
        GRoarForm roarForm = gson.fromJson(requestBody, GRoarForm.class);

        GRoar replyTo = db.getSession()
                .createQuery("FROM GRoar WHERE id=:id", GRoar.class)
                        .setParameter("id", roarForm.getReplyTo())
                                .getSingleResultOrNull();

        db.ready();
        GRoar gRoar = new GRoar(user, roarForm.getText(), replyTo);
        db.getSession().persist(gRoar);
        db.done();

        return new Response(gRoar, Response.OK);
    }
}

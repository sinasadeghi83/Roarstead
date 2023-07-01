package com.roarstead.Controllers;

import com.google.gson.JsonObject;
import com.roarstead.App;
import com.roarstead.Components.Annotation.POST;
import com.roarstead.Components.Controller.BaseController;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Response.Response;
import com.roarstead.Models.GRoar;
import com.roarstead.Models.GRoarForm;
import com.roarstead.Models.RoarMedia;
import com.roarstead.Models.User;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoarController extends BaseController {
    @Override
    public Map<String, List<String>> accessControl() {
        return Map.of(
            "actionRoar", List.of("@")
        );
    }

    @POST
    public Response actionRoar(JsonObject requestBody) throws Exception{
        Database db = App.getCurrentApp().getDb();

        User user = (User) App.getCurrentApp().getAuthManager().identity();
        GRoarForm roarForm = gson.fromJson(requestBody, GRoarForm.class);

        db.ready();
        GRoar gRoar = new GRoar(user, roarForm.getText());
        db.getSession().persist(gRoar);
        db.done();

        return new Response(gRoar, Response.OK);

//        List<RoarMedia> roarMedias = db.getSession()
//                .createQuery("FROM RoarMedia WHERE id IN :roarMediaIds")
//                .setParameterList("roarMediaIds", roarForm.getRoarMediaIds())
//                .getResultList();
    }
}

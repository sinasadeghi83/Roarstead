package com.roarstead.Controllers;

import com.roarstead.App;
import com.roarstead.Components.Controller.BaseController;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Exceptions.NotFoundException;
import com.roarstead.Components.Resource.Models.Image;
import com.roarstead.Components.Resource.ResourceManager;
import com.roarstead.Components.Response.Response;
import com.roarstead.Models.GRoar;
import com.roarstead.Models.RoarMedia;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MediaController extends BaseController {
    private static final String ROAR_ID_KEY = "roar_id";

    @Override
    public Map<String, List<String>> accessControl() {
        return Map.of(
            "actionRoar", List.of("@")
        );
    }

    public Response actionRoar() throws Exception{
        Map<String, String> params = App.getCurrentApp().getQueryParams();
        int roarId = Integer.parseInt(params.get(ROAR_ID_KEY));

        Database db = App.getCurrentApp().getDb();
        GRoar roar = db.getSession()
                .createQuery("FROM GRoar WHERE id=:id", GRoar.class)
                .setParameter("id", roarId)
                .getSingleResultOrNull();
        if(roar == null)
            throw new NotFoundException();

        Set<RoarMedia> roarMediaSet = roar.getMediaSet();
        Set<Map<String, String>> base64Set = new HashSet<>();
        ResourceManager resourceManager = App.getCurrentApp().getResourceManager();
        for (RoarMedia roarMedia :
                roarMediaSet) {
            String base64 = resourceManager.convertMediaToBase64(roarMedia.getMedia());
            String contentType = resourceManager.getMediaMIMEType(roarMedia.getMedia());
            base64Set.add(Map.of(
                    "content_type", contentType,
                    "media", base64
            ));
        }

        return new Response(base64Set);
    }
}

package com.benine.backend.http.jetty;

import com.benine.backend.Preset;
import com.benine.backend.PresetController;
import com.benine.backend.ServerController;
import org.eclipse.jetty.server.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PresetsHandler extends CameraRequestHandler {

  /**
   * Constructor for a new CameraInfoHandler, handling the /camera/ request.
   */
  public PresetsHandler() {
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {

    String cameraInfo = getCameraController().getCamerasJSON();
    String route = getRoute(request);

    request.getParameter("tag");

//      switch (route) {
//        case "createpreset":
//          presetCreationHandler.handle(s, request, req, res);
//          break;
//      }


    String presetInfo = getPresetsInfo(request);

    respond(request, res, presetInfo);
    request.setHandled(true);

  }

  private String getPresetsInfo(Request request) {
    String tag = request.getParameter("tag");

    ArrayList<Preset> presets;
    PresetController controller = ServerController.getInstance().getPresetController();
    if (tag == null) {
      presets = controller.getPresets();
    } else {
      presets = controller.getPresetsByTag(tag);
    }

    JSONArray json = new JSONArray();
    for (Preset preset : presets) {
      json.add(preset.toJSON());
    }

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("presets", json);
    return jsonObject.toString();
  }

  /**
   * Returns the route of the url, so we can select the next handler.
   * @param request   The current request.
   * @return          Returns the route.
   */
  public String getRoute(Request request) {
    String path = request.getPathInfo();

    return path.replaceFirst(".*/", "");
  }
}
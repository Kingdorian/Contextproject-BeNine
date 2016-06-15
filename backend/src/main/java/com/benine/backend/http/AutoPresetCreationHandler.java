package com.benine.backend.http;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import com.benine.backend.video.StreamNotAvailableException;


public class AutoPresetCreationHandler extends AutoPresetHandler  {


  /**
   * Constructs a request handler.
   * @param httpserver to interact with the rest of the system.
   */
  public AutoPresetCreationHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse) throws IOException, ServletException {
    PresetPyramidCreator creator = getPyramidPresetCreator(request);
    String camID = request.getParameter("camera");
    Camera cam = getCameraController().getCameraById(Integer.parseInt(camID));
    if (!(cam instanceof IPCamera )) {
      System.out.println(" not a ipcam");
      respondFailure(request, httpServletResponse);
      request.setHandled(true);
      return;
    }
    IPCamera ipcam = (IPCamera) cam;
   
    try {
      Collection<Integer> presetIDs = creator.createPresets(ipcam, creator.generateSubViews());
      JSONObject jsonObject = new JSONObject();
      JSONArray idsJson = new JSONArray();
      presetIDs.forEach(id -> idsJson.add(id));
      jsonObject.put("presetIDs", idsJson);
      respond(request, httpServletResponse, jsonObject.toJSONString());
      
    } catch (CameraConnectionException | InterruptedException
            | TimeoutException | StreamNotAvailableException | SQLException e ) {
      getLogger().log("Exception occured while trying to auto create presets", e);
      respondFailure(request, httpServletResponse);
    }  catch (CameraBusyException e) {
      getLogger().log("Trying to auto create presets on busy camera with id: "
              + camID, e);
      respondFailure(request, httpServletResponse);
    } finally {
      request.setHandled(true);
    }


  }
  
}

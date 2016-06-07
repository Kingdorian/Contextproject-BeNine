package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.Preset;
import org.eclipse.jetty.server.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RecallPresetHandler extends RequestHandler {

  /**
   * Constructor for a new RecallPresetHandler, handling the /presets/recallpreset request.
   * @param httpserver this handler belongs to.
   */
  public RecallPresetHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    try {
      int presetID = Integer.parseInt(request.getParameter("presetid"));

      Preset preset = getPresetController().getPresetById(presetID);
      
      Camera camera = getCameraController().getCameraById(preset.getCameraId());

      preset.excecutePreset(camera);
      respondSuccess(request, res);

    } catch (CameraConnectionException e) {
      getLogger().log("Error connectiong to camera", e);
    } catch (CameraBusyException e) {
      getLogger().log("Camera is busy", e);
      respondFailure(request, res);
    } catch (NumberFormatException e) {
      getLogger().log(e.getMessage(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    }

    request.setHandled(true);
  }

}

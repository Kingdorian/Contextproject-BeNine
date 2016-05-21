package com.benine.backend.http.jetty;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ZoomingCamera;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 21-05-16.
 */
public class CameraZoomHandler extends CameraRequestHandler {

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    int camID = getCameraId(request);

    ZoomingCamera zoomingCam = (ZoomingCamera) getCameraController().getCameraById(camID);
    String zoomto = request.getParameter("zoomType");
    String zoom = request.getParameter("zoom");

    try {
      zoom(zoomingCam, zoomto, zoom);
    } catch (MalformedURIException e) {
      getLogger().log("Malformed URI: " + request.getRequestURI(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to camera: " + zoomingCam.getId(), LogEvent.Type.WARNING);
      respondFailure(request, res);
    }

  }

  private void zoom(ZoomingCamera zoomingCam, String zoomto, String zoom) throws MalformedURIException, CameraConnectionException {
    if (zoom != null && zoomto.equals("relative")) {
      zoomingCam.zoom(Integer.parseInt(zoom));
    } else if (zoom != null && zoomto.equals("absolute")) {
      zoomingCam.zoomTo(Integer.parseInt(zoom));
    } else {
      throw new MalformedURIException("Invalid value for zoom or zoomType invalid");
    }
  }
}
package com.benine.backend.http;

import com.benine.backend.*;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.jar.Attributes;


public class RecallPresetHandler extends RequestHandler {

  /**
   * Create a new handler for recalling presets.
   * @param logger to log to.
   */
  public RecallPresetHandler(Logger logger) {
    super(logger);
  }
  
  /**
  * Handles a request of making a new preset. 
  * @param exchange the exchange containing data about the request.
  * @throws IOException when an error occurs with responding to the request.
  */
  public void handle(HttpExchange exchange) throws IOException {
    Attributes parsedURI;
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());

      int cameraID = Integer.parseInt(parsedURI.getValue("currentcamera"));
      int presetID = Integer.parseInt(parsedURI.getValue("presetid"));
      PresetController presetController = ServerController.getInstance().getPresetController();
      Preset preset = presetController.getPresetById(presetID);
      IPCamera ipcamera = (IPCamera)getCameraController().getCameraById(cameraID);
      
      moveCamera(ipcamera,preset);
      responseSuccess(exchange);
    } catch (MalformedURIException e) {
      responseFailure(exchange);
      getLogger().log("Wrong URI", LogEvent.Type.CRITICAL);
    } catch (CameraConnectionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  /**
   * Method that moves the camera to the correct position.
   * @param ipcamera the camera to be moved
   * @param preset the preset used with the values for moving the camera.
   * @throws CameraConnectionException exception thrown when camera cannot connect.
   */
  public void moveCamera(IPCamera ipcamera, Preset preset) throws CameraConnectionException {
    Position position = preset.getPosition();
    ipcamera.moveTo(position, preset.getPanspeed(), preset.getTiltspeed());
    ipcamera.zoomTo(preset.getZoom());
    ipcamera.moveFocus(preset.getFocus());
    ipcamera.setAutoFocusOn(preset.isAutofocus());
    ipcamera.setIrisPosition(preset.getIris());
    ipcamera.setAutoIrisOn(preset.isAutoiris());
  }
}

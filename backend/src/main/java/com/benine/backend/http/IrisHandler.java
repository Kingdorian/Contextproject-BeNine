package com.benine.backend.http;

import com.benine.backend.Main;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.IrisCamera;
import com.sun.net.httpserver.HttpExchange;
import sun.util.resources.cldr.mr.CalendarData_mr_IN;

import java.io.IOException;
import java.util.jar.Attributes;

/**
 * Created by dorian on 4-5-16.
 */
public class IrisHandler extends RequestHandler {

  /**
   * Creates a new IrisHandler.
   */
  public IrisHandler(CameraController controller) {
    super(controller);
  }
  /**
   * Handles a request
   * @param exchange the exchange containing data about the request.
   * @throws IOException when an error occurs with responding to the request.
   */
  public void handle(HttpExchange exchange) throws IOException {
    //TODO add logging stuff
    // Extract camera id from function and amount to zoom in
    Attributes parsedURI;
    String response;
    try {
      parsedURI = parseURI(exchange.getRequestURI().getQuery());
    } catch (MalformedURIException exception) {
      // TODO log exception
      response = "{\"succes\":\"false\"}";
      return;
    }
    Camera cam =  getCameraController().getCameraById(Integer.parseInt(parsedURI.getValue("id")));
    IrisCamera irisCam = (IrisCamera)cam;
    String autoOn = parsedURI.getValue("autoIrisOn");
    String setPos = parsedURI.getValue("position");
    try {
      if (autoOn != null) {
        boolean autoOnBool = Boolean.parseBoolean(autoOn);
        irisCam.setAutoIrisOn(autoOnBool);
      }
      if (setPos != null) {
        irisCam.setIrisPos(Integer.parseInt(setPos));
      }
      response = "{\"succes\":\"true\"}";
    } catch (Exception e) {
      //TODO Log exception
      response = "{\"succes\":\"false\"}";
    }
    respond(exchange, response);
    return;

  }
}
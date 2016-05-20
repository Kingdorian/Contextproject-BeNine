package com.benine.backend.http.jetty;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraController;
import com.benine.backend.database.Database;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 20-05-16.
 */
public abstract class RequestHandler extends AbstractHandler {

  private Logger logger;

  public RequestHandler() {
    this.logger = ServerController.getInstance().getLogger();
  }

  /**
   * Returns cameracontroller
   * @return cameracontroller interacting with.
   */
  protected CameraController getCameraController() {
    return ServerController.getInstance().getCameraController();
  }

  /**
   * Returns the database
   * @return database to retrieve information from.
   */
  protected Database getDatabase() {
    return ServerController.getInstance().getDatabase();
  }

  /**
   * Responds to a request with status 200.
   * @param response the httpservletresponse to respond to.
   * @param body a string with the response.
   */
  public void respond(Request request, HttpServletResponse response, String body) {
    try {
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentLength(body.length());

      PrintWriter out = response.getWriter();
      out.write(body);
      out.close();

    } catch (IOException e) {
      getLogger().log("Error occured while writing the response to a request at URI"
              + request.getRequestURI(), LogEvent.Type.WARNING);
    }
  }

  /**
   * Returns the logger.
   * @return A Logger object.
   */
  public Logger getLogger() {
    return this.logger;
  }
}
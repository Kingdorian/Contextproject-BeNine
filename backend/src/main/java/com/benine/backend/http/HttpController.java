package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.FocussingCamera;
import com.benine.backend.camera.IrisCamera;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.ZoomingCamera;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by dorian on 10-5-16.
 */
public class HttpController {

  private HttpServer server;
  private Logger logger;
  private ServerController serverController;

  /**
   * Constructor, creates a new HttpController object.
   * @param address an internetsocet address indicating the ports for the server to listen to.
   * @param logger the logger to use to log to.
   * @param port number to connect to.
   */
  public HttpController(String address, int port, Logger logger) {
    this(createServer(address, port, logger), logger);
  }

  /**
   * Constructor, creates a new HttpController object.
   * @param httpserver a server object.
   * @param logger the logger to use to log to.
   */
  public HttpController(HttpServer httpserver, Logger logger) {
    this.logger = logger;
    this.server = httpserver;
    serverController = ServerController.getInstance();
    createHandlers();
    server.start();
    logger.log("Server running at: " + server.getAddress(), LogEvent.Type.INFO);
  }

  /**
   * Creates a server object.
   * @param address Socket address
   * @param logger  Logger
   * @param port port number to connect to
   * @return  An HttpServer.
     */
  private static HttpServer createServer(String address, int port, Logger logger) {
    try {
      InetSocketAddress socket = new InetSocketAddress(address, port);
      return HttpServer.create(socket, 20);
    } catch (IOException e) {
      logger.log("Unable to create server", LogEvent.Type.CRITICAL);
      e.printStackTrace();
      return null;
    }
  }
  
  /**
   * Creates handlers for all cams in the camera controller.
   */
  private void createHandlers() {
    server.createContext("/static", new FileHandler(logger));
    server.createContext("/camera/", new CameraInfoHandler(logger));
    for (Camera cam : serverController.getCameraController().getCameras()) {
      createHandlers(cam);
    }
  }
  
  /**
   * Creates the handlers for a certain camera.
   * @param cam camera to create handlers for.
   */
  private void createHandlers(Camera cam) {
    int camId = cam.getId();
    if (cam instanceof FocussingCamera) {
      server.createContext("/camera/" + camId
              + "/focus", new FocussingHandler(logger));
    }
    if (cam instanceof IrisCamera) {
      server.createContext("/camera/" + camId + "/iris",
                                              new IrisHandler(logger));
    }
    if (cam instanceof MovingCamera) {
      server.createContext("/camera/" + camId + "/move", 
                                              new MovingHandler(logger));
    }
    if (cam instanceof ZoomingCamera) {
      server.createContext("/camera/" + camId + "/zoom",
                                              new ZoomingHandler(logger));
    }
    server.createContext("/camera/" + camId + "/preset", 
                                              new PresetHandler(logger));
    server.createContext("/camera/" + camId + "/createpreset", 
                                              new PresetCreationHandler(logger));
    server.createContext("/camera/" + camId + "/recallpreset",
                                               new RecallPresetHandler(logger));

    logger.log("Succesufully setup endpoints", LogEvent.Type.INFO);
  }

  /**
   * Destroys http handler.
   */
  public void destroy() {
    server.stop(0);
  }
}

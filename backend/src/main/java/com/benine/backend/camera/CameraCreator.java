package com.benine.backend.camera;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.CameraFactory.InvalidCameraTypeException;
import com.benine.backend.camera.ipcameracontrol.IPCameraFactory;

import java.util.HashMap;
import java.util.Map;

public class CameraCreator {

  static CameraCreator cameraCreator;
  
  /**
   * Map of string types to right factory.
   */
  static final Map<String, CameraFactory> CAMERA_TYPES;
  
  static {
    CAMERA_TYPES = new HashMap<String, CameraFactory>();
    CAMERA_TYPES.put("simplecamera", new SimpleCameraFactory());
    CAMERA_TYPES.put("ipcamera", new IPCameraFactory());
  }

  /**
   * Private constructor so there can only be one instance of the camera creator.
   */
  private CameraCreator() {

  }
  
  /**
   * Returns the unique instance of the camera creator.
   * @return CameraCreator
   */
  public static synchronized CameraCreator getInstance() {
    if (cameraCreator == null) {
      cameraCreator = new CameraCreator();
    }
    return cameraCreator;
  }

  /**
  * Creates a camera object as specified in camSpec.
  * @param camSpec specification of the camera 0: type, 1: additional info.
  * @return Camera object.
  * @throws InvalidCameraTypeException when specified camera type can not be created.
  */
  public Camera createCamera(String[] camSpec) throws InvalidCameraTypeException {
    CameraFactory factory = CAMERA_TYPES.get(camSpec[0]);
    if (factory == null) {
      CameraController.logger.log("The following camera type is not specified: " + camSpec[0],
          LogEvent.Type.CRITICAL);
      throw new InvalidCameraTypeException("Camera type is not regonized");
    }
    String[] spec = new String[camSpec.length - 1];
    System.arraycopy(camSpec, 1, spec, 0, camSpec.length - 1);
    CameraController.logger.log("New Camera object is created.",
        LogEvent.Type.INFO);
    return factory.createCamera(spec);
  }
}


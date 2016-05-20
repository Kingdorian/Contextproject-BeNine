package com.benine.backend.camera;

import com.benine.backend.video.StreamType;


/**
 * Interface for communication with remote camera's.
 */
public interface Camera {

  /**
   * Method to create a json object describinding the camera.
   *
   * @return Json object in the form of a string.
   * @throws CameraConnectionException When the information can not be retrieved.
   */
  String toJSON() throws CameraConnectionException;

  /**
   * Get the ID of this camera.
   *
   * @return ID of this camra.
   */
  int getId();

  /**
   * Set the ID of this camera.
   * @param id to set.
   */
  void setId(int id);

  /**
   * Get the Stream Type of this camera.
   *
   * @return StreamType ENUM of this camera.
   */
  StreamType getStreamType();

  /**
   * Requests the MAC address of the camera.
   * @return MAC address of the camera.
   * @throws CameraConnectionException when mac address can not be retrieved.
   */
  String getMacAddress() throws CameraConnectionException;

}


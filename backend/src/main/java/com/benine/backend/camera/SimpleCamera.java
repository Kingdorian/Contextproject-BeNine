package com.benine.backend.camera;

import org.json.simple.JSONObject;

/**
 * Created by dorian on 5-5-16.
 */
public class SimpleCamera implements Camera {

  private int id = -1;
  private String streamLink;

  /**
   * Creates a JSON representation of this object.
   * @return A JSON string.
   * @throws CameraConnectionException thrown when the connection with the camera can't be used.
   */
  @Override
  public String toJSON() throws CameraConnectionException {
    JSONObject object = new JSONObject();
    object.put("id", getId());
    object.put("streamlink", getStreamLink());
    return object.toString();
  }

  /**
   * Sets id.
   * @param id the new id.
   */
  @Override
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Getter for id.
   * @return the id of this camera.
   */
  @Override
  public int getId() {
    return id;
  }

  /**
   * Sets the stream link for this camera.
   * @return the new stream link.
   */
  public String getStreamLink() {
    return streamLink;
  }

  /**
   * Sets streamlink
   * @param streamLink an url string pointing to a mjpeg stream.
   */
  public void setStreamLink(String streamLink) {
    this.streamLink = streamLink;
  }

}

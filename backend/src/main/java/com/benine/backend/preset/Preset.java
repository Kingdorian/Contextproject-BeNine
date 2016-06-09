package com.benine.backend.preset;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * an abstract preset class containing the basics for a preset.
 */
public abstract class Preset {

  private String image;
  private int presetid = -1;
  protected Set<String> tags = new HashSet<String>();
  private int cameraId;
  private String name;

  /**
   * Constructs a preset.
   *
   * @param cameraId The id of the camera associated with this preset.
   * @param name     The name of te preset
   */
  public Preset(int cameraId, String name) {
    this.cameraId = cameraId;
    this.name = name;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setId(int id) {
    this.presetid = id;
  }

  public int getId() {
    return presetid;
  }

  public void setCameraId(int id) {
    this.cameraId = id;
  }

  public int getCameraId() {
    return cameraId;
  }

  public Set<String> getTags() {
    return tags;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  /**
   * Adds a new tag to this object.
   *
   * @param tag the tag to add.
   */
  public void addTag(String tag) {
    tags.add(tag);
  }


  /**
   * Adds a list of keywords to this class.
   *
   * @param tags a list of keywords
   */
  public void addTags(Set<String> tags) {
    tags.remove("");
    this.tags.addAll(tags);
  }

  /**
   * Removes a keyword from this preset.
   *
   * @param tag the keyword to remove
   */
  public void removeTag(String tag) {
    tags.remove(tag);
  }

  /**
   * Remove all the tags from this preset.
   */
  public void removeTags() {
    this.tags.removeAll(getTags());
  }

  /**
   * Returns a JSON representation of this object.
   *
   * @return JSON representation of this object.
   */
  public abstract JSONObject toJSON();


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + presetid;
    result = prime * result + cameraId;
    result = prime * result + tags.hashCode();
    return result;
  }

  /**
   * Checking if two presets are equal.
   *
   * @param o the object to be checked with.
   * @return true if two presets are equal, false otherwise.
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Preset preset = (Preset) o;
    if (presetid != preset.presetid || cameraId != preset.cameraId
        || !tags.equals(preset.getTags()) || !name.equals(preset.name)) {
      return false;
    }
    return true;
  }

  /**
   * Recall this preset by moving the camera to the right position.
   *
   * @param camera used to move the camera.
   * @throws CameraConnectionException when camera can't be moved
   */
  public abstract void excecutePreset(Camera camera) throws CameraConnectionException;
}

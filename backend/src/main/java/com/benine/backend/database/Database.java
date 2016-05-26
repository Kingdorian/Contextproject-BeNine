package com.benine.backend.database;

import com.benine.backend.Preset;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface for communication with the database.
 */
public interface Database {

  /**
   * Checks if database is connected.
   *
   * @return true if database is connected
   * @throws SQLException No right connection found
   */
  boolean isConnected() throws SQLException;

  /**
   * Add a preset to the database.
   * @param preset             The preset to be added
   */
  void addPreset(Preset preset);

  /**
   * Delete a preset from the database.
   * @param presetID ID of the camera
   */
  void deletePreset(int presetID);

  /**
   * Update a preset to the database.
   *
   * @param preset The preset to be updated
   */
  void updatePreset(Preset preset);

  /**
   * Returns all the presets.
   *
   * @return all the presets
   */
  ArrayList<Preset> getAllPresets();

  /**
   * Returns all the presets of the camera.
   *
   * @param cameraId ID of the camera
   * @return the presets of the given camera
   */
  ArrayList<Preset> getAllPresetsCamera(int cameraId);

  /**
   * Tries to connect to database server.
   *
   * @return True if the connection is succeeded.
   */
  boolean connectToDatabaseServer();

  /**
   * Check if the database is present in the server.
   *
   * @return True if the database is present, false otherwise
   */
  boolean checkDatabase();

  /**
   * Creates new database, overrides the old one if there is one.
   */
  void resetDatabase();

  /**
   * Closes the connection to the server.
   */
  void closeConnection();

  /**
   * Adds a camera to the database.
   *
   * @param id The ID of the camera
   * @param ip The IP of the camera
   */
  void addCamera(int id, String ip);

  /**
   * Checks if cameras are correct in database.
   */
  void checkCameras();

  /**
   * deletes a camera from the database.
   * @param cameraID the camera to be deleted
   */
  void deleteCamera(int cameraID);

  /**
   * Makes sure the right database is used.
   */
  void useDatabase();

  /**
   * Setter for the connection.
   * @param connection The new connection
   */
  void setConnection(Connection connection);
}
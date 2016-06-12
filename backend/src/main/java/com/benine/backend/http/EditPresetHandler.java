package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.Preset;
import com.benine.backend.video.MJPEGFrameResizer;
import com.benine.backend.video.MJPEGStreamReader;
import com.benine.backend.video.StreamController;
import com.benine.backend.video.StreamNotAvailableException;
import com.benine.backend.video.StreamReader;
import com.benine.backend.video.VideoFrame;

import org.eclipse.jetty.server.Request;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class that handles editing presets.
 */
public class EditPresetHandler extends RequestHandler {
  
  /**
   * Constructor for a new editPresetHandler that handles editing a preset.
   * @param httpserver this handler is for.
   */
  public EditPresetHandler(HTTPServer httpserver) {
    super(httpserver);
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {
    
    try {
      final String overwriteTag = request.getParameter("overwritetag");
      final String overwritePosition = request.getParameter("overwriteposition");
      int presetID = Integer.parseInt(request.getParameter("presetid"));
      String tags = request.getParameter("tags");
      String name = request.getParameter("name");
      
      Preset preset = getPresetController().getPresetById(presetID);
      Set<String> tagList = new HashSet<>();
      if (name != null) {
        updateName(preset, name);
      }
      if (tags != null) {
        tagList = new HashSet<>(Arrays.asList(tags.split("\\s*,\\s*"))); 
      }
      if (overwriteTag.equals("true")) {
        updateTag(preset, tagList);
      }
      if (overwritePosition.equals("true")) {
        updatePosition(preset);
      }
      respondSuccess(request, res);
    } catch (MalformedURIException | SQLException | StreamNotAvailableException e) {
      getLogger().log(e.getMessage(), e);
      respondFailure(request,res);
    } catch (CameraConnectionException e) {
      getLogger().log("Cannot connect to camera.", e);
      respondFailure(request,res);
    }  catch (CameraBusyException e) {
      getLogger().log("Camera is busy.", LogEvent.Type.WARNING);
      respondFailure(request, res);
    } finally {
      request.setHandled(true);
    }
  }
  
  /**
   * Updating the tag only.
   * @param preset the preset to be changed
   * @param tagList the tag to be added
   * @throws SQLException when preset can not be updated.
   */
  private void updateTag(Preset preset, Set<String> tagList) throws SQLException {
    preset.removeTags();
    preset.addTags(tagList);
    getPresetController().updatePreset(preset);
  }
  
  /**
   * Editing an already existing preset by removing the old preset and creating a new 
   * preset with the same preset and camera id. It also creates a new image that belongs to the 
   * preset and updates the database.
   * @param preset                        The preset to be updated.
   * @throws IOException                  If the image cannot be created.
   * @throws StreamNotAvailableException  If the camera does not have a stream.
   * @throws SQLException                 If the preset cannot be written to the database.
   * @throws CameraConnectionException    If the camera cannot be reached.
   * @throws MalformedURIException        If there is an error in the request.
   * @throws CameraBusyException          If camera is busy
   */
  private void updatePosition(Preset preset) throws 
  IOException, StreamNotAvailableException, SQLException, CameraConnectionException, 
  MalformedURIException, CameraBusyException {
    IPCamera ipcam = (IPCamera) getCameraController().getCameraById(preset.getCameraId());   
    Preset newPreset = ipcam.createPreset(preset.getTags(), preset.getName());
    newPreset.setId(preset.getId());
    StreamReader streamReader = getStreamController().getStreamReader(ipcam.getId());
    newPreset.createImage(streamReader);
    getPresetController().updatePreset(newPreset);
  }

  /**
   * Updating the name only.
   * @param preset the preset to be changed
   * @param name the new name
   * @throws SQLException when preset can not be updated
   */
  private void updateName(Preset preset, String name) throws SQLException {
    preset.setName(name);
    getPresetController().updatePreset(preset);
  } 
}

package com.benine.backend.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.benine.backend.Preset;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.video.MJPEGStreamReader;
import com.benine.backend.video.Stream;
import com.benine.backend.video.StreamNotAvailableException;
import com.google.common.collect.ImmutableSet;


public class EditPresetHandlerTest extends RequestHandlerTest {

  private IPCamera ipcamera;
  private Preset preset;
  private Stream stream;
  private MJPEGStreamReader streamReader;
  private List<String> tags;
  
  @Override
   public RequestHandler supplyHandler() {
     return new EditPresetHandler();
   }
 
   
  @Before
  public void initialize() throws IOException {
    super.initialize();
    ipcamera = mock(IPCamera.class);
    when(cameracontroller.getCameraById(1)).thenReturn(ipcamera);
    stream = mock(Stream.class);
    when(stream.getInputStream()).thenReturn(new BufferedInputStream(new FileInputStream("resources" + File.separator + "test" + File.separator + "testmjpeg.mjpg")));

    streamReader = new MJPEGStreamReader(stream);
    tags = Arrays.asList("violin", "piano");
    

    try {
      when(streamController.getStreamReader(1)).thenReturn(streamReader);
      when(ipcamera.getFocusPosition()).thenReturn(33);
      when(ipcamera.getIrisPosition()).thenReturn(50);
      when(ipcamera.getPosition()).thenReturn(new Position(0, 0));
      when(ipcamera.getZoomPosition()).thenReturn(100);
      when(ipcamera.isAutoFocusOn()).thenReturn(true);
      when(ipcamera.isAutoIrisOn()).thenReturn(true);
      when(ipcamera.getId()).thenReturn(1);
      
      preset = new Preset(new Position(0,0), 100, 33,50,true,15,1,true, 0, tags);
      when(presetController.getPresetById(1)).thenReturn(preset);

    } catch (CameraConnectionException e) {
      e.printStackTrace();
    } catch (StreamNotAvailableException e) {
      e.printStackTrace();
    }
  }
 
  @Test
  public void testUpdateTag() throws IOException, StreamNotAvailableException, SQLException, CameraConnectionException, MalformedURIException {
    EditPresetHandler handler = new EditPresetHandler();
    List<String> tags1 = Arrays.asList("drums", "guitar");
    Set<String> tags2 = ImmutableSet.copyOf(tags1);
    when(handler.updateTag(preset, tags1)).thenReturn(tags2);
    
  }
   
  
  
   
   
   
 }















package com.benine.backend.camera.ipcameracontrol;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.InvalidCameraTypeException;
import com.benine.backend.camera.Position;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class to test the IP Camera class.
 * The mock server is used to simulate the camera.
 */
public class IpcameraTest {

  private IPCamera camera;
  
  @Before
  public final void setUp() throws InvalidCameraTypeException {
    camera = Mockito.spy(new IPCamera("test"));
  }
  
  public void setCameraBehaviour(String cmd, String response) throws IpcameraConnectionException {
    Mockito.doReturn(response).when(camera).sendCommand("aw_ptz?cmd=%23" + cmd + "&res=1");
  }
  
  @Test
  public final void testGetMACAddress() throws CameraConnectionException, IOException {
    String ipcameraInfo = IOUtils.toString(new FileInputStream("resources" + File.separator + "test" + File.separator + "ipcameraInfoTest.txt"));
    Mockito.doReturn(ipcameraInfo).when(camera).sendCommand("getinfo?FILE=1");   
    String actual = camera.getMacAddress();
    assertEquals("8C-C1-21-F0-46-C9", actual);
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testGetMACAddressFails() throws CameraConnectionException {
    Mockito.doReturn("").when(camera).sendCommand("getinfo?FILE=1");      
    camera.getMacAddress();
  }


  @Test
  public final void testMoveToHomePosition() throws CameraConnectionException {
    setCameraBehaviour("APS80008000111", "aPS80008000111");
    
    Position pos = new Position(0, 180);
    camera.moveTo(pos, 17, 1);
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23APS80008000111&res=1");
  }
  
  @Test
  public final void testMoveToWithSpeed1() throws CameraConnectionException {
    setCameraBehaviour("APS80008000011", "aPS80008000011");
    
    Position pos = new Position(0, 180);
    camera.moveTo(pos, 1, 1);
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23APS80008000011&res=1");
  }
  
  @Test
  public final void testMoveWithSpecifiedSpeed() throws CameraConnectionException {
    setCameraBehaviour("PTS0199", "pTS0199");
    camera.move(01, 99);
    Mockito.verify(camera).sendCommand("aw_ptz?cmd=%23PTS0199&res=1");
  }
  
  @Test
  public final void testGetPosition() throws CameraConnectionException {
    setCameraBehaviour("APC", "aPC80008000");
    Position res = camera.getPosition();
    
    assertEquals(0, res.getPan(), 0.000001);
    assertEquals(180, res.getTilt(), 0.000001);
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testGetPositionException() throws CameraConnectionException {
    setCameraBehaviour("APC", "aPP80008000");
    camera.getPosition();
  }
   
  @Test
  public final void testGetStreamLink() {
    String res = camera.getStreamLink();
    assertEquals(res, "http://test" + "/cgi-bin/mjpeg");
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testNonExcistingIpAdres() throws CameraConnectionException {
    IPCamera camera = new IPCamera("1.300.3.4");
    camera.move(180, 50);
  }

  @Test
  public final void testGetSetId() {
    IPCamera camera = new IPCamera("1.300.3.4");
    camera.setId(4);
    Assert.assertEquals(4, camera.getId());
  }

  @Test
  public final void testUninitializedId(){
    IPCamera camera = new IPCamera("1.300.3.4");
    Assert.assertEquals(-1, camera.getId());
  }
  
  @Test
  public final void testNotEqualsIPAddress() {
    IPCamera camera1 = new IPCamera("12");
    IPCamera camera2 = new IPCamera("13");
    assertNotEquals(camera1, camera2);
  }
  
  @Test
  public final void testEquals() {
    IPCamera camera1 = new IPCamera("12");
    IPCamera camera2 = new IPCamera("12");
    assertEquals(camera1, camera2);
  }
  
  @Test
  public final void testNotEqualsID() {
    IPCamera camera1 = new IPCamera("12");
    IPCamera camera2 = new IPCamera("12");
    camera2.setId(5);
    assertNotEquals(camera1, camera2);
  }
  
  @Test
  public final void testHashCodeNotEqual() {
    IPCamera camera1 = new IPCamera("12");
    IPCamera camera2 = new IPCamera("12");
    camera2.setId(5);
    assertNotEquals(camera1.hashCode(), camera2.hashCode());
  }
  
  @Test
  public final void testHashCodeEqual() {
    IPCamera camera1 = new IPCamera("12");
    IPCamera camera2 = new IPCamera("12");
    assertEquals(camera1.hashCode(), camera2.hashCode());
  }
}

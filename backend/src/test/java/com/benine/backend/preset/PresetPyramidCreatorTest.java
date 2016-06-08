package com.benine.backend.preset;

import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ZoomPosition;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.benine.backend.preset.autopresetcreation.Coordinate;
import com.benine.backend.preset.autopresetcreation.SubView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class PresetPyramidCreatorTest extends AutoPresetCreatorTest {

  IPCamera cam = mock(IPCamera.class);

  @Before
  public void initialize() throws CameraConnectionException {
    when(cam.getPosition()).thenReturn(new Position(0, 180));
    when(cam.getZoom()).thenReturn(0);
  }

  @Test
  public void testCreatePositionsSingleCenter()
          throws CameraConnectionException {
    ArrayList<SubView> subViews = new ArrayList<>();
    subViews.add(new SubView(new Coordinate(10, 90), new Coordinate(90, 10)));
    Collection<ZoomPosition> actualPositons =  new PresetPyramidCreator(1, 1, 1, 0).generatePositions(cam, subViews);
    Collection<ZoomPosition> expectedPositions = new ArrayList<>();
    expectedPositions.add(new ZoomPosition(cam.getPosition(), cam.getZoom()));

    Assert.assertEquals(expectedPositions, actualPositons);
  }


  @Test
  public void testCreatePositionsSingleOffCenter()
          throws CameraConnectionException {
    ArrayList<SubView> subViews = new ArrayList<>();
    subViews.add(new SubView(new Coordinate(0, 100), new Coordinate(50, 50)));
    Collection<ZoomPosition> actualPositons =  new PresetPyramidCreator(1, 1, 1, 0).generatePositions(cam, subViews);
    Collection<ZoomPosition> expectedPositions = new ArrayList<>();
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan() - (IPCamera.HORIZONTAL_FOV_MAX/4),
            cam.getPosition().getTilt() + (IPCamera.VERTICAL_FOV_MAX/4), cam.getZoom()));

    Assert.assertEquals(expectedPositions, actualPositons);
  }

  @Test
  public void testCreatePositionsMultiple()
          throws CameraConnectionException {
    ArrayList<SubView> subViews = new ArrayList<>();
    Collection<ZoomPosition> actualPositons =  new PresetPyramidCreator(2, 1, 1, 0).generatePositions(cam, new ArrayList<>());
    Collection<ZoomPosition> expectedPositions = new ArrayList<>();
    expectedPositions.add(new ZoomPosition(0.0,
            cam.getPosition().getTilt()-(IPCamera.VERTICAL_FOV_MAX/4),
            cam.getZoom()));
    expectedPositions.add(new ZoomPosition(0.0,
            cam.getPosition().getTilt()+(IPCamera.VERTICAL_FOV_MAX/4),
            cam.getZoom()));
    Assert.assertEquals(expectedPositions, actualPositons);
  }

  @Test
  public void testCreatePositions1x2x1()
          throws CameraConnectionException {
    Collection<ZoomPosition> actualPositons =  new PresetPyramidCreator(1, 2, 1, 0).generatePositions(cam, new ArrayList<>());
    Collection<ZoomPosition> expectedPositions = new ArrayList<>();
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()-(IPCamera.HORIZONTAL_FOV_MAX/4),
            180.0,
            cam.getZoom()));
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()+(IPCamera.HORIZONTAL_FOV_MAX/4),
            180.0,
            cam.getZoom()));
    Assert.assertEquals(expectedPositions, actualPositons);
  }

  @Test
  public void testCreatePositions2x2x1()
          throws CameraConnectionException {
    Collection<ZoomPosition> actualPositons =  new PresetPyramidCreator(2, 2, 1, 0).generatePositions(cam, new ArrayList<>());
    Collection<ZoomPosition> expectedPositions = new ArrayList<>();
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()-(IPCamera.HORIZONTAL_FOV_MAX/4),
            cam.getPosition().getTilt()-(IPCamera.VERTICAL_FOV_MAX/4),
            cam.getZoom()));
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()-(IPCamera.HORIZONTAL_FOV_MAX/4),
            cam.getPosition().getTilt()+(IPCamera.VERTICAL_FOV_MAX/4),
            cam.getZoom()));
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()+(IPCamera.HORIZONTAL_FOV_MAX/4),
            cam.getPosition().getTilt()+(IPCamera.VERTICAL_FOV_MAX/4),
            cam.getZoom()));
    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()+(IPCamera.HORIZONTAL_FOV_MAX/4),
            cam.getPosition().getTilt()-(IPCamera.VERTICAL_FOV_MAX/4),
            cam.getZoom()));
    Assert.assertTrue(expectedPositions.containsAll(actualPositons));
    Assert.assertTrue(actualPositons.containsAll(expectedPositions));
  }

  @Test
  public void testCreatePositions3x3x1()
          throws CameraConnectionException {
    ArrayList<ZoomPosition> actualPositons =  new ArrayList<>(new PresetPyramidCreator(3, 3, 1, 0).generatePositions(cam, new ArrayList<>()));
    ArrayList<ZoomPosition> expectedPositions = new ArrayList<>();

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()-(IPCamera.HORIZONTAL_FOV_MAX*(1.0/3)),
            cam.getPosition().getTilt()-(IPCamera.VERTICAL_FOV_MAX*(1.0/3)),
            cam.getZoom()));

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()+(IPCamera.HORIZONTAL_FOV_MAX*(1.0/3)),
            cam.getPosition().getTilt()-(IPCamera.VERTICAL_FOV_MAX*(1.0/3)),
            cam.getZoom()));

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()-(IPCamera.HORIZONTAL_FOV_MAX*(1.0/3)),
            cam.getPosition().getTilt()+(IPCamera.VERTICAL_FOV_MAX*(1.0/3)),
            cam.getZoom()));

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()+(IPCamera.HORIZONTAL_FOV_MAX*(1.0/3)),
            cam.getPosition().getTilt()+(IPCamera.VERTICAL_FOV_MAX*(1.0/3)),
            cam.getZoom()));

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()+(IPCamera.HORIZONTAL_FOV_MAX*(1.0/3)),
            cam.getPosition().getTilt(),
            cam.getZoom()));

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan()-(IPCamera.HORIZONTAL_FOV_MAX*(1.0/3)),
            cam.getPosition().getTilt(),
            cam.getZoom()));

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan(),
            cam.getPosition().getTilt()-(IPCamera.VERTICAL_FOV_MAX*(1.0/3)),
            cam.getZoom()));

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan(),
            cam.getPosition().getTilt()+(IPCamera.VERTICAL_FOV_MAX*(1.0/3)),
            cam.getZoom()));

    expectedPositions.add(new ZoomPosition(cam.getPosition().getPan(),
            cam.getPosition().getTilt(),
            cam.getZoom()));

    Assert.assertTrue(expectedPositions.containsAll(actualPositons));
    Assert.assertTrue(actualPositons.containsAll(expectedPositions));
  }

  @Test(expected = AssertionError.class)
  public final void testAssertRows() {
    new PresetPyramidCreator(-1, 2, 3, 0.5);
  }

  @Test(expected = AssertionError.class)
  public final void testAssertColumns() {
    new PresetPyramidCreator(1, -2, 3, 0.5);
  }

  @Test(expected = AssertionError.class)
  public final void testAssertLevels() {
    new PresetPyramidCreator(1, 2, -5, 0.5);
  }

  @Override
  public void testCreate() {

  }

  @Override
  public AutoPresetCreator getCreator() {
    return new PresetPyramidCreator(2, 2, 2, 0);
  }

}
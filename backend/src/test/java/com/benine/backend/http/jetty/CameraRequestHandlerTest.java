package com.benine.backend.http.jetty;

import com.benine.backend.http.CameraRequestHandler;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.when;

/**
 * Created on 22-05-16.
 */
public abstract class CameraRequestHandlerTest extends RequestHandlerTest {

  @Override
  public abstract CameraRequestHandler supplyHandler();

  public void setPath(String path) {
    when(requestMock.getPathInfo()).thenReturn(path);
  }

  public void setParameters(MultiMap<String> parameters) {
    requestMock.setParameters(parameters);

    for (String s : parameters.keySet()) {
      when(requestMock.getParameter(s)).thenReturn(parameters.getString(s));
    }

    when(requestMock.getParameters()).thenReturn(parameters);

  }

  @Test
  public void testGetCameraID() {
    setPath("/camera/42/test");
    int camid = ((CameraRequestHandler) getHandler()).getCameraId(requestMock);

    Assert.assertEquals(42, camid, 0);
  }

  @Test
  public void testGetCameraIDError() {
    setPath("/camera/fortytwo/test");
    int camid = ((CameraRequestHandler) getHandler()).getCameraId(requestMock);

    Assert.assertEquals(-1, camid, 0);
  }

  @Test
  public void testGetRoute() {
    setPath("/camera/42/test");
    String route = ((CameraRequestHandler) getHandler()).getRoute(requestMock);

    Assert.assertEquals("test", route);
  }

}

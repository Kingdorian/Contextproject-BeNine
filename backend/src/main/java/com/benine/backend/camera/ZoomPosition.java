package com.benine.backend.camera.ipcameracontrol;//TODO add Javadoc comment

import com.benine.backend.camera.Position;

/**
 *
 */
public class ZoomPosition extends Position {

  private int zoom;

  /**
   * Constructor of a position object used for zoomingcamera positions.
   *
   * @param pan  position in pan direction.
   * @param tilt position in the tilt direction.
   * @param zoom zoom position
   */
  public ZoomPosition(double pan, double tilt, int zoom) {
    super(pan, tilt);
    this.zoom = zoom;
  }

  /**
   * Constructor of a position object used for zoomingcamera positions.
   * @param position the positon
   * @param zoom the zoom level
   */
  public ZoomPosition(Position position, int zoom) {
    super(position.getPan(), position.getTilt());
    this.zoom = zoom;
  }

  public void setZoom(int zoom) {
    this.zoom = zoom;
  }

  public int getZoom() {
    return zoom;
  }
}
package com.benine.backend;

import com.benine.backend.video.MJPEGStreamReader;

public class Main {

  /**
   * Main method of the program.
   *
   * @param args command line arguments.
   */
  public static void main(String[] args) {
    ServerController server = ServerController.getInstance();

    server.start();

    MJPEGStreamReader stream1 = new MJPEGStreamReader("http://131.180.123.51/zm/cgi-bin/nph-zms?mode=jpeg&monitor=2&scale=100&buffer=100");
    Thread t1 = new Thread(stream1);
    t1.start();

    MJPEGStreamReader stream2 = new MJPEGStreamReader("http://tuincam.bt.tudelft.nl/mjpg/video.mjpg");
    new Thread(stream2).start();

    try {
      while (true) {
        Thread.sleep(100);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

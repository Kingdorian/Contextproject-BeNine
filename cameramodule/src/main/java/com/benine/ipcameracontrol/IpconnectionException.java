package com.benine.ipcameracontrol;

/**
 * Exception class for problems with the IP connection.
 * @author Bryan
 *
 */
public class IpconnectionException extends Exception {
  
  /**
   * Create Ip connection exception.
   * @param message to give extra information with exception.
   */
  public IpconnectionException(String message) {
    super(message);
  }

  /**
   * Serial version ID.
   */
  private static final long serialVersionUID = -6165565835957650746L;

}

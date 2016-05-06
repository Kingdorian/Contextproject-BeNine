package com.benine.backend;

import java.io.IOException;
import java.util.Date;

/**
 * Created by dorian on 2-5-16.
 */
public class Logger {

  private LogWriter writer;

  private boolean consoleLoggingEnabled = true;
  private boolean fileLoggingEnabled  = true;

  /**
   * Creates a new logger object and logs to given output.
   * @param writer the logwriter
   */
  public Logger(LogWriter writer) {
    this.writer = writer;
  }
  
  /**
   * Creates a new Logger object with standard logwriter.
   * @throws IOException if the default log file is for some reason tot accesible
   */
  public Logger() throws IOException {
    this.writer = new LogWriter(Main.getConfig().getValue("standardloglocation"));
  }

  /**
   * Logs item.
   * @param time the time to register the logevent at.
   * @param message the description of the logevent
   * @param level An int specifying the level of the logevent.
   */
  public void log(String time, String message, int level) {
    log(new LogEvent(time, message, LogEvent.Type.values()[level]));
  }

  /**
   * Logs item.
   * @param time the time to register the logevent at.
   * @param message the description of the logevent
   * @param type The type of the logevent.
   */
  public void log(String time, String message, LogEvent.Type type) {
    log(new LogEvent(time, message, type));
  }
  
  /**
   * Logs item at current time
   * @param message the description of the logevent
   * @param type The type of the logevent.
   */
  public void log(String message, LogEvent.Type type) {
    log(new LogEvent(new Date().toString(), message, type));
  }
  
  /**
   * Logs logevent.
   * @param event event to log.
   */
  public void log(LogEvent event) {
    if (consoleLoggingEnabled) {
      System.out.println(event.toString());
    }
    if (fileLoggingEnabled) {
      writer.write(event);
    }
  }

  /**
   * Returns if there is currently being logged to the console.
   * @return true if there is being logged to the console
   */
  public boolean consoleLoggingEnabled() {
    return consoleLoggingEnabled;
  }

  /**
   * Returns if there is currently being logged to the file.
   * @return true if there is being logged to the file
   */
  public boolean fileLoggingEnabled() {
    return fileLoggingEnabled;
  }

  /**
   * Disables logging to console.
   */
  public void disableConsoleLogging() {
    consoleLoggingEnabled = false;
  }
  
  /**
   * Disables logging to file.
   */
  public void disableFileLogging() {
    fileLoggingEnabled = false;
  }
  
  /**
   * Disables logging to console.
   */
  public void enableConsoleLogging() {
    consoleLoggingEnabled = true;
  }
  
  /**
   * Disables logging to file.
   */
  public void enableFileLogging() {
    fileLoggingEnabled = true;
  }
}
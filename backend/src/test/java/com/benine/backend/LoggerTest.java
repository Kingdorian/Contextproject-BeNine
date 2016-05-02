package com.benine.backend;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import java.io.PrintStream;

/**
 * Created by dorian on 2-5-16.
 */
public class LoggerTest {

  private final ByteArrayOutputStream out = new ByteArrayOutputStream();
  private final LogWriter logwriter = mock(LogWriter.class);

  private int counter = 0;

  @Before
  public void setUp() {
    // Catch the console output in out
    System.setOut(new PrintStream(out));
    doNothing().when(logwriter).write(anyString(), anyString(), any());
    doNothing().when(logwriter).write(anyString(), any());
    doNothing().when(logwriter).write(any());
  }

  @Test
  public void testLoggerToConsole() {
    Logger logger = new Logger(logwriter);
    logger.log("this moment", "Hello", LogEvent.Type.CRITICAL);
    Assert.assertEquals("[CRITICAL|this moment]Hello", out.toString());
  }

  @Test
  public void testLoggerToFile() {
    Logger logger = new Logger(logwriter);
    logger.log("this moment", "Hello", LogEvent.Type.CRITICAL);
    // Check if one of the write methods is called on the logwriter mock
    doAnswer(count).when(logwriter).write(any(), any(), any());
    doAnswer(count).when(logwriter).write(any(), any());
    doAnswer(count).when(logwriter).write(any());
    Assert.assertEquals(1, counter);
    counter=0;
  }



  private Answer count = invocation -> {
    counter++;
    return null;
  };
}

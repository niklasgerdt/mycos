package mycos.systemtests;

import static org.junit.Assert.*;

import java.util.*;
import java.util.concurrent.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import zmq.ZError;
import mycos.*;

@SuppressWarnings("unused")
public class SocketCreationTest {

  public void afterTest(Socket... sockets) {
    Arrays.asList(sockets).forEach(s -> s.release());
  }

  @Test(expected = NullPointerException.class)
  public void createClientRequiresValidAddress() {
    Client client = SocketBuilder.buildSocket().asClientOf(null);
    fail();
  }

  @Test
  public void createsClientToGivenAddress() {
    Client client = SocketBuilder.buildSocket().asClientOf("localhost:8000");
    assertNotNull(client);
    afterTest(client);
  }

  @Test(expected = NullPointerException.class)
  public void createServerRequiresValidAddress() {
    Server server = SocketBuilder.buildSocket().asServerAt(null);
    fail();
  }

  @Test
  public void createsServerToGivenAddress() {
    Server server = SocketBuilder.buildSocket().asServerAt("localhost:8000");
    assertNotNull(server);
    afterTest(server);
  }
}

package mycos.systemtests;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Optional;
import mycos.*;
import org.junit.Test;

// System level test
public class ClientServerTests {
  private static final String REQUEST = "request";
  private static final String REPLY = "reply";

  void release(Socket... sockets) {
    Arrays.asList(sockets).stream().forEach(s -> s.release());
  }

  @Test
  public void simpleRequestReply() {
    final Client client = SocketBuilder.buildSocket().asClientOf("localhost:8000");
    final Server server = SocketBuilder.buildSocket().asServerAt("localhost:8000");
    Wait<String> waitReply = client.ask(REQUEST);
    Optional<String> what = server.hang();
    server.reply(REPLY);
    Optional<String> reply = waitReply.get();
    release(client, server);
    assertEquals(REQUEST, what.get());
    assertEquals(REPLY, reply.get());
  }

  @Test
  public void simpleRequestReplyWithoutActualServerInPlace() {
    final Client client = SocketBuilder.buildSocket().asClientOf("localhost:8000");
    Wait<String> waitReply = client.ask(REQUEST);
    final Server server = SocketBuilder.buildSocket().asServerAt("localhost:8000");
    Optional<String> what = server.hang();
    server.reply(REPLY);
    Optional<String> reply = waitReply.get();
    release(client, server);
    assertEquals(REQUEST, what.get());
    assertEquals(REPLY, reply.get());
  }

  @Test
  public void canCreateMultipleClientsToSameServer() {
    Client c1 = SocketBuilder.buildSocket().asClientOf("localhost:8000");
    Client c2 = SocketBuilder.buildSocket().asClientOf("localhost:8000");
    Client c3 = SocketBuilder.buildSocket().asClientOf("localhost:8000");
    Server s = SocketBuilder.buildSocket().asServerAt("localhost:8000");
    Wait<String> w1 = c1.ask("C1");
    Wait<String> w2 = c2.ask("C2");
    Wait<String> w3 = c3.ask("C3");
    s.reply(s.hang().get());
    s.reply(s.hang().get());
    s.reply(s.hang().get());
    assertEquals("C1", w1.get().get());
    assertEquals("C2", w2.get().get());
    assertEquals("C3", w3.get().get());
  }
}

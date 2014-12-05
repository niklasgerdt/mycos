package mycos;

import static org.junit.Assert.*;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.zeromq.ZMQ;

// NetworkContextStateManager is the core class of this application and should be tested exhaustive.
public class NetworkContextStateManagerConcurrencyTest {
  private static final int INVOCATIONS = 10_000;
  @Mock
  private ZeroMqContextWrapper ctxmock;
  @Mock
  private ZmqSock sockmock;
  @InjectMocks
  private NetworkContextStateManager manager;

  @Before
  public void setupMocks() {
    MockitoAnnotations.initMocks(this);
    manager = new NetworkContextStateManager(ctxmock);
  }

  @Test
  public void parallelSocketCreationTriggersOnlyOneContextCreation() {
    Mockito.when(ctxmock.socket(ZMQ.REQ)).thenReturn(sockmock);
    IntStream.rangeClosed(1, INVOCATIONS).parallel()
        .forEach(i -> manager.createSocket(SocketType.CLIENT, ""));
    Mockito.verify(ctxmock, Mockito.times(1)).init();
  }

  @Test
  public void sequalSocketCreationAndReleasingTriggersEqualAmountOfContextCreationsAndDestroys() {
    Mockito.when(ctxmock.socket(ZMQ.REQ)).thenReturn(sockmock);
    IntStream.rangeClosed(1, INVOCATIONS).forEach(i -> {
      manager.createSocket(SocketType.CLIENT, "");
      manager.destroySocket(sockmock);
    });
    Mockito.verify(ctxmock, Mockito.times(INVOCATIONS)).init();
    Mockito.verify(ctxmock, Mockito.times(INVOCATIONS)).close();
  }

  @Test
  public void alwaysSetupsContextBeforeSocketCreation() {
    Mockito.when(ctxmock.socket(ZMQ.REQ)).thenReturn(sockmock);
    IntStream.rangeClosed(1, INVOCATIONS).parallel()
        .forEach(i -> manager.createSocket(SocketType.CLIENT, ""));
    InOrder inOrder = Mockito.inOrder(ctxmock);
    inOrder.verify(ctxmock, Mockito.times(1)).init();
    inOrder.verify(ctxmock, Mockito.times(INVOCATIONS)).socket(ZMQ.REQ);
  }

  @Test
  public void alwaysDestroysContextAfterAllSocketsAreReleased() {
    Mockito.when(ctxmock.socket(ZMQ.REQ)).thenReturn(sockmock);
    IntStream.rangeClosed(1, INVOCATIONS).parallel()
        .forEach(i -> manager.createSocket(SocketType.CLIENT, ""));
    IntStream.rangeClosed(1, INVOCATIONS).parallel().forEach(i -> manager.destroySocket(sockmock));
    InOrder inOrder = Mockito.inOrder(ctxmock, sockmock);
    inOrder.verify(sockmock, Mockito.times(INVOCATIONS)).close();
    inOrder.verify(ctxmock, Mockito.times(1)).close();
  }

  // comparing performance of different implementations of NetworkContextStateManager
  @Test
  public void printsCreationTimeForTenThousandParallelSocketCreation() {
    final long TAG_0_0_1 = 211534080; // synchronized methods
    final long t1 = System.nanoTime();
    Mockito.when(ctxmock.socket(ZMQ.REQ)).thenReturn(sockmock);
    IntStream.rangeClosed(1, INVOCATIONS).parallel()
        .forEach(i -> manager.createSocket(SocketType.CLIENT, ""));
    final long t2 = System.nanoTime();
    final long time = t2 - t1;
    System.out.println("It takes " + time + " nanos to create ten thousand sockets");
    System.out.println(1.0 * time / TAG_0_0_1 + " of TAG_0.0.1");
    assertTrue(time < TAG_0_0_1);
  }
}

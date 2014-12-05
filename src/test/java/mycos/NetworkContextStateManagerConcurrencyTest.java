package mycos;

import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.zeromq.ZMQ;

// NetworkContextStateManager is the core class of this application and should be tested exhaustive.
public class NetworkContextStateManagerConcurrencyTest {
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
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> manager.createSocket(SocketType.CLIENT, ""));
        Mockito.verify(ctxmock, Mockito.times(1)).init();
    }

    @Test
    public void sequalSocketCreationAndReleasingTriggersEqualAmountOfContextCreationsAndDestroys() {
        Mockito.when(ctxmock.socket(ZMQ.REQ)).thenReturn(sockmock);
        IntStream.rangeClosed(1, 1000).forEach(i ->
        {
            manager.createSocket(SocketType.CLIENT, "");
            manager.destroySocket(sockmock);
        });
        Mockito.verify(ctxmock, Mockito.times(1000)).init();
        Mockito.verify(ctxmock, Mockito.times(1000)).close();
    }

    @Test
    public void alwaysSetupsContextBeforeSocketCreation() {
        Mockito.when(ctxmock.socket(ZMQ.REQ)).thenReturn(sockmock);
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> manager.createSocket(SocketType.CLIENT, ""));
        InOrder inOrder = Mockito.inOrder(ctxmock);
        inOrder.verify(ctxmock, Mockito.times(1)).init();
        inOrder.verify(ctxmock, Mockito.times(1000)).socket(ZMQ.REQ);
    }

    @Test
    public void alwaysDestroysContextAfterAllSocketsAreReleased() {
        Mockito.when(ctxmock.socket(ZMQ.REQ)).thenReturn(sockmock);
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> manager.createSocket(SocketType.CLIENT, ""));
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> manager.destroySocket(sockmock));
        InOrder inOrder = Mockito.inOrder(ctxmock, sockmock);
        inOrder.verify(sockmock, Mockito.times(1000)).close();
        inOrder.verify(ctxmock, Mockito.times(1)).close();
    }

    // nothing to assert here, but useful for comparing different implementations of NetworkContextStateManager
    @Test
    public void printsCreationTimeForTenThousandParallelSocketCreation() {
        final long t1 = System.nanoTime();
        Mockito.when(ctxmock.socket(ZMQ.REQ)).thenReturn(sockmock);
        IntStream.rangeClosed(1, 10_000).parallel().forEach(i -> manager.createSocket(SocketType.CLIENT, ""));
        final long t2 = System.nanoTime();
        System.out.println("It takes " + (t2 - t1) + " nanos to create ten thousand sockets");
    }
}

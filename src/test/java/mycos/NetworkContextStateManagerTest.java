package mycos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.zeromq.ZMQ;

// NetworkContextStateManager is the core class of this application and should be tested exhaustive
public class NetworkContextStateManagerTest {
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
  public void firstSocketCreationFiresContextUp() {
    Mockito.when(ctxmock.socket(ZMQ.REQ)).thenReturn(sockmock);
    manager.createSocket(SocketType.CLIENT, "");
    Mockito.verify(ctxmock, Mockito.times(1)).init();
  }

  @Test
  public void ifContextCreationFailsDoesNotTryToInitSocket() {
    try {
      Mockito.doThrow(new NetworkException("")).when(ctxmock).init();
      manager.createSocket(SocketType.CLIENT, "");
    } catch (NetworkException e) {
      Mockito.verify(ctxmock, Mockito.times(0)).socket(ZMQ.REQ);
    }
  }

  @Test
  public void ifFirstSocketCreationFailsTrysToDestroyContext() {
    try {
      Mockito.doThrow(new NetworkException("")).when(ctxmock).socket(ZMQ.REQ);
      manager.createSocket(SocketType.CLIENT, "");
    } catch (NetworkException e) {
      Mockito.verify(ctxmock, Mockito.times(1)).init();
      Mockito.verify(ctxmock, Mockito.times(1)).close();
    }
  }

  @Test
  public void secondSocketCreationSkipsContextCreation() {
    Mockito.when(ctxmock.socket(ZMQ.REQ)).thenReturn(sockmock);
    manager.createSocket(SocketType.CLIENT, "");
    manager.createSocket(SocketType.CLIENT, "");
    Mockito.verify(ctxmock, Mockito.times(1)).init();
  }

  @Test
  public void socketReleasingHasNoEffectOnContextWhenLiveSocketsBehind() {
    Mockito.when(ctxmock.socket(ZMQ.REQ)).thenReturn(sockmock);
    manager.createSocket(SocketType.CLIENT, "");
    manager.createSocket(SocketType.CLIENT, "");
    manager.destroySocket(sockmock);
    Mockito.verify(ctxmock, Mockito.times(0)).close();
  }

  @Test
  public void releasingLastSocketDestroysContext() {
    Mockito.when(ctxmock.socket(ZMQ.REQ)).thenReturn(sockmock);
    manager.createSocket(SocketType.CLIENT, "");
    manager.destroySocket(sockmock);
    Mockito.verify(ctxmock, Mockito.times(1)).close();
  }

  @Test
  public void releasingLastSocketDestroysContextEvenIfReleasingFails() {
    try {
      Mockito.when(ctxmock.socket(ZMQ.REQ)).thenReturn(sockmock);
      Mockito.doThrow(new NetworkException("")).when(sockmock).close();
      manager.createSocket(SocketType.CLIENT, "");
      manager.destroySocket(sockmock);
    } catch (NetworkException e) {
      Mockito.verify(ctxmock, Mockito.times(1)).close();
    }
  }
}

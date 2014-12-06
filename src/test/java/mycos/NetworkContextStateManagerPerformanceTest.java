/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Niklas Gerdt
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package mycos;

import static org.junit.Assert.assertTrue;

import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.zeromq.ZMQ;

public class NetworkContextStateManagerPerformanceTest {
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
  public void creationTimeForTenThousandParallelSocketCreation() {
    final long TAG_0_0_1 = 211534080; // synchronized methods
    final long TAG_0_0_2 = 281885220; // synchronized blocks
    final long t1 = System.nanoTime();
    Mockito.when(ctxmock.socket(ZMQ.REQ)).thenReturn(sockmock);
    IntStream.rangeClosed(1, INVOCATIONS).parallel()
        .forEach(i -> manager.createSocket(SocketType.CLIENT, ""));
    final long t2 = System.nanoTime();
    final long time = t2 - t1;
    System.out.println("It takes " + time + " nanos to create ten thousand sockets");
    System.out.println(1.0 * time / TAG_0_0_1 + " of TAG_0.0.1");
    System.out.println(1.0 * time / TAG_0_0_2 + " of TAG_0.0.2");
    assertTrue(time < TAG_0_0_1);
    assertTrue(time < TAG_0_0_2);
  }

  @Test
  public void destructionTimeForTenThousandParallelSocketCreation() {
    final long TAG_0_0_2 = 26651815; // synchronized blocks
    Mockito.when(ctxmock.socket(ZMQ.REQ)).thenReturn(sockmock);
    IntStream.rangeClosed(1, INVOCATIONS).parallel()
        .forEach(i -> manager.createSocket(SocketType.CLIENT, ""));
    final long t1 = System.nanoTime();
    IntStream.rangeClosed(1, INVOCATIONS).parallel().forEach(i -> sockmock.close());
    final long t2 = System.nanoTime();
    final long time = t2 - t1;
    System.out.println("It takes " + time + " nanos to destroy ten thousand sockets");
    System.out.println(1.0 * time / TAG_0_0_2 + " of TAG_0.0.2");
    assertTrue(time < TAG_0_0_2);
  }
}

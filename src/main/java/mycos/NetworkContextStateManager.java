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

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import zmq.ZError;

// TODO all exception types need to be confirmed. This means digging in to zeromq source code.
final class NetworkContextStateManager {
  private static final int MAX_SOCKETS = 1024;
  private static final int INITIAL_PERMITS = 1;
  private final Semaphore semaphore = new Semaphore(INITIAL_PERMITS);
  private final ZeroMqContextWrapper zmqctx;
  private final AtomicInteger socketCounter = new AtomicInteger(0);
  private boolean contextup = false;

  NetworkContextStateManager(final ZeroMqContextWrapper contextWrapper) {
    zmqctx = contextWrapper;
  }

  ZmqSock createSocket(final SocketType type, final String address) {
    try {
      semaphore.acquire();
      if (contextDownAndNoSockets()) {
        initContext();
        semaphore.release(MAX_SOCKETS);
        return createSocket(type, address);
      }
      final ZmqSock socket = initSocket(type, address);
      socketCounter.incrementAndGet();
      return socket;
    } catch (NetworkException e) {
      if (contextUpAndNoSockets())
        destroyContext();
      throw e;
    } catch (InterruptedException e) {
      throw new UnknownException("Socket creating thread interrupted by client", e);
    } finally {
      semaphore.release();
    }
  }

  void destroySocket(final ZmqSock socket) {
    try {
      socket.close();
    } catch (ZError.CtxTerminatedException | ZError.IOException e) {
      throw new NetworkException("can't destroy socket", e);
    } finally {
      socketCounter.decrementAndGet();
      drainPermitsAndDestroyContextIfNecessary();
    }
  }

  private void initContext() {
    if (contextDownAndNoSockets())
      try {
        zmqctx.init();
        contextup = true;
      } catch (ZMQException | ZError.CtxTerminatedException | ZError.InstantiationException
          | ZError.IOException e) {
        throw new NetworkException("can't init networking context!", e);
      }
  }

  private ZmqSock initSocket(final SocketType type, String address) {
    try {
      return initSocketByType(type, address);
    } catch (ZMQException | ZError.CtxTerminatedException | ZError.InstantiationException
        | ZError.IOException e) {
      throw new NetworkException("can't init requested socket type: " + type, e);
    }
  }

  private ZmqSock initSocketByType(final SocketType type, String address) {
    switch (type) {
      case CLIENT:
        ZmqSock c = zmqctx.socket(ZMQ.REQ);
        c.connect(address);
        return c;
      case SERVER:
        ZmqSock s = zmqctx.socket(ZMQ.REP);
        s.bind(address);
        return s;
      default:
        throw new NetworkException("can't init requested socket type: " + type);
    }
  }

  private void drainPermitsAndDestroyContextIfNecessary() {
    if (contextUpAndNoSockets()) {
      drainSemaphoreForContextDestroying();
      destroyContext();
      if (contextup)
        semaphore.release(MAX_SOCKETS);
      else
        semaphore.release(INITIAL_PERMITS);
    }
  }

  private void drainSemaphoreForContextDestroying() {
    try {
      semaphore.acquire();
      drainSemaphore();
    } catch (InterruptedException e) {
      throw new UnknownException("Socket destroying thread interrupted by client", e);
    }
  }

  private void drainSemaphore() {
    int snapped = semaphore.drainPermits();
    while (snapped - MAX_SOCKETS - INITIAL_PERMITS > 0)
      snapped += semaphore.drainPermits();
  }
  

  private synchronized void destroyContext() {
    if (contextUpAndNoSockets())
      try {
        zmqctx.close();
        contextup = false;
      } catch (ZMQException | ZError.CtxTerminatedException | ZError.InstantiationException
          | ZError.IOException e) {
        throw new NetworkException("can't destroy networking context!", e);
      }
  }
  
  private boolean contextUpAndNoSockets() {
    return socketCounter.get() == 0 && contextup;
  }

  private boolean contextDownAndNoSockets() {
    return socketCounter.get() == 0 && !contextup;
  }
}

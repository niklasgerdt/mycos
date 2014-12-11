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

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.zeromq.ZMQException;
import com.google.gson.JsonParseException;

final class ClientSocket implements Client {
  private final ExecutorService exec;
  private final NetworkContextStateManager contextStateManager;
  private final GsonWrapper gson;
  private final ZmqSock zmqsocket;
  private volatile boolean released = false;

  ClientSocket(NetworkContextStateManager networkContextStateManager, ZmqSock zmqsocket,
      GsonWrapper gson) {
    this.gson = gson;
    exec = Executors.newSingleThreadExecutor();
    contextStateManager = networkContextStateManager;
    this.zmqsocket = zmqsocket;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <C, S> Wait<S> ask(C object) {
    validateState();
    Future<Optional<S>> future = exec.submit(() -> sendAndReceive(object));
    return new ReplyWaiter<S>(future);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <C, S> Optional<S> askAndWait(C object) {
    validateState();
    return sendAndReceive(object);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void release() {
    released = true;
    contextStateManager.destroySocket(zmqsocket);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean released() {
    return released;
  }

  private <C, S> Optional<S> sendAndReceive(C object) {
    try {
      String json = gson.toJson(object);
      zmqsocket.send(json);
      String reply = zmqsocket.recvStr();
      return gson.fromJson(reply);
    } catch (ZMQException e) {
      throw new NetworkException("Network communication failed", e);
    } catch (JsonParseException e) {
      throw new ParseException("Object parsing failed", e);
    }
  }
}

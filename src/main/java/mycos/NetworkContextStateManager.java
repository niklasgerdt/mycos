/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Niklas Gerdt
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package mycos;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;
import zmq.ZError;

// TODO all exception types need to be confirmed. This means digging in to zeromq source code.
final class NetworkContextStateManager {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final ZeroMqContextWrapper zmqctx;
    private int socketCounter = 0;
    private boolean contextup = false;

    NetworkContextStateManager(final ZeroMqContextWrapper contextWrapper) {
        zmqctx = contextWrapper;
    }

    synchronized ZmqSock createSocket(final SocketType type, final String address) {
        try {
            if (contextDownAndNoSockets())
                initContext();
            final ZmqSock socket = initSocket(type, address);
            socketCounter++;
            return socket;
        } catch (NetworkException e) {
            if (contextUpAndNoSockets())
                destroyContext();
            throw e;
        }
    }

    synchronized void destroySocket(final ZmqSock socket) {
        try {
            socket.close();
        } catch (ZError.CtxTerminatedException | ZError.IOException e) {
            throw new NetworkException("can't destroy socket", e);
        } finally {
            socketCounter--;
            if (contextUpAndNoSockets())
                destroyContext();
        }
    }

    private boolean contextUpAndNoSockets() {
        return socketCounter == 0 && contextup;
    }

    private boolean contextDownAndNoSockets() {
        return socketCounter == 0 && !contextup;
    }

    private void initContext() {
        try {
            zmqctx.init();
            contextup = true;
        } catch (ZMQException | ZError.CtxTerminatedException | ZError.InstantiationException | ZError.IOException e) {
            throw new NetworkException("can't init networking context!", e);
        }
    }

    private void destroyContext() {
        try {
            zmqctx.close();
            contextup = false;
        } catch (ZMQException | ZError.CtxTerminatedException | ZError.InstantiationException | ZError.IOException e) {
            throw new NetworkException("can't destroy networking context!", e);
        }
    }

    private ZmqSock initSocket(final SocketType type, String address) {
        try {
            return initSocketByType(type, address);
        } catch (ZMQException | ZError.CtxTerminatedException | ZError.InstantiationException | ZError.IOException e) {
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
}

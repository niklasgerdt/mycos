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

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQException;
import zmq.ZError;

// TODO all exception types need to be confirmed. This means digging in to zeromq source code.
final class NetworkContextStateManager {
    private final ZeroMqContextWrapper zmqctx;
    private int socketCounter = 0;
    private boolean contextup = false;

    NetworkContextStateManager(final ZeroMqContextWrapper contextWrapper) {
	zmqctx = contextWrapper;
    }

    synchronized Socket createSocket(final SocketType type, final String address) {
	Socket socket = null;
	try {
	    if (contextDownAndNoSockets())
		initContext();
	    socket = initSocket(type, address);
	} catch (NetworkException e) {
	    if (contextUpAndNowSockets())
		destroyContext();
	    throw e;
	}
	socketCounter++;
	return socket;
    }

    synchronized void destroySocket(final Socket socket) {
	try {
	    socket.close();
	} catch (ZError.CtxTerminatedException | ZError.IOException e) {
	    throw new NetworkException("can't destroy socket", e);
	} finally {
	    socketCounter--;
	    if (contextUpAndNowSockets())
		destroyContext();
	}
    }

    private boolean contextUpAndNowSockets() {
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
	} catch (ZMQException | ZError.CtxTerminatedException | ZError.InstantiationException | ZError.IOException e) {
	    throw new NetworkException("can't destroy networking context!", e);
	}
    }

    private Socket initSocket(final SocketType type, String address) {
	try {
	    return initSocketByType(type, address);
	} catch (ZMQException | ZError.CtxTerminatedException | ZError.InstantiationException | ZError.IOException e) {
	    throw new NetworkException("can't init requested socket type: " + type, e);
	}
    }

    private Socket initSocketByType(final SocketType type, String address) {
	Socket s = null;
	switch (type) {
	case CLIENT:
	    s = zmqctx.socket(ZMQ.REQ);
	    s.connect(address);
	    return s;
	case SERVER:
	    s = zmqctx.socket(ZMQ.REP);
	    s.bind(address);
	    return s;
	default:
	    throw new NetworkException("can't init requested socket type: " + type);
	}
    }
}

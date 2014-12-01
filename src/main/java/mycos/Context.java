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

import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;
import zmq.ZError;

final class Context {
    private static final String CONTEXT = "mycos.workers";
    private static final int WORKERS = 1;
    private static final Context INSTANCE = new Context();
    private final ZMQ.Context zmqctx;
    // TODO explore alternatives from Lea's Java Concurrency
    private final NavigableSet<Socket> sockets = new ConcurrentSkipListSet<>();
    private final Object socketsLock = new Object();

    private Context() {
	String workersStr = System.getProperty(CONTEXT, String.valueOf(WORKERS));
	final int workers = Integer.parseInt(workersStr);
	zmqctx = ZMQ.context(workers);
    }

    static Context instance() {
	return INSTANCE;
    }

    Socket clientSocket(final String address) {
	try {
	    final ZMQ.Socket zmqsocket = zmqctx.socket(ZMQ.REQ);
	    zmqsocket.connect(address);
	    final Socket socket = new Socket(zmqsocket);
	    addToSockets(socket);
	    return socket;
	} catch (ZMQException | ZError.CtxTerminatedException | ZError.InstantiationException | ZError.IOException e) {
	    throw new MycosNetworkException("Can't create valid client socket", e);
	}
    }

    Socket serverSocket(final String address) {
	try {
	    final ZMQ.Socket zmqsocket = zmqctx.socket(ZMQ.REP);
	    zmqsocket.bind(address);
	    final Socket socket = new Socket(zmqsocket);
	    addToSockets(socket);
	    return socket;
	} catch (ZMQException | ZError.CtxTerminatedException | ZError.InstantiationException | ZError.IOException e) {
	    throw new MycosNetworkException("Can't create valid server socket", e);
	}
    }

    void release(Socket s) {
	synchronized (socketsLock) {
	    tryToRelease(s);
	}
    }

    void release() {
	synchronized (socketsLock) {
	    tryToReleaseAllSocketsAndCloseContext();
	}
    }

    // Trying to close all zmq sockets and the zmq context even if some fails
    private void tryToReleaseAllSocketsAndCloseContext() {
	try {
	    sockets.forEach(s -> tryToRelease(s));
	} catch (MycosNetworkException e) {
	    if (sockets.isEmpty())
		throw e;
	    else {
		tryToReleaseAllSocketsAndCloseContext();
	    }
	} finally {
	    zmqctx.close();
	}
    }

    private void tryToRelease(Socket s) {
	try {
	    s.release();
	} catch (MycosNetworkException e) {
	    sockets.remove(s);
	    throw e;
	}
    }

    private void addToSockets(final Socket socket) {
	synchronized (socketsLock) {
	    sockets.add(socket);
	}
    }
}

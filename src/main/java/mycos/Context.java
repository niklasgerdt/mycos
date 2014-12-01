/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Niklas Gerdt
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package mycos;

import java.util.*;
import java.util.concurrent.*;

import org.zeromq.*;

final class Context {
    private static final String CONTEXT = "mycos.workers";
    private static final int WORKERS = 1;
    private static final Context INSTANCE = new Context();
    private final ZMQ.Context zmqctx;
    // TODO explore alternatives from Lea's Java Concurrency
    private final NavigableSet<Socket> sockets = new ConcurrentSkipListSet<>();

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
	    final org.zeromq.ZMQ.Socket zmqsocket = zmqctx.socket(ZMQ.REQ);
	    zmqsocket.connect(address);
	    final Socket socket = new Socket(zmqsocket);
	    sockets.add(socket);
	    return socket;
	} catch (RuntimeException e) {
	    // ZMQ throws various RunTimeExceptions
	    throw new MycosNetworkException("Can't create valid client socket", e);
	}
    }

    Socket serverSocket(final String address) {
	final org.zeromq.ZMQ.Socket zmqsocket = zmqctx.socket(ZMQ.REP);
	zmqsocket.bind(address);
	final Socket socket = new Socket(zmqsocket);
	sockets.add(socket);
	return socket;
    }

    // TODO do we need concurrency control?
    void release(Socket s) {
	s.release();
	sockets.remove(s);
    }

    void release() {
	sockets.forEach(s -> s.release());
	sockets.clear();
    }
}

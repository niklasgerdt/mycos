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

import java.util.*;
import org.zeromq.*;
import zmq.ZError;

class Socket {
    private final ZMQ.Socket zmqsocket;

    Socket(ZMQ.Socket socket) {
	this.zmqsocket = socket;
    }

    void send(String data) {
	try {
	    zmqsocket.send(data);
	} catch (ZMQException e) {
	    throw new MycosNetworkException("Sending failed", e);
	}
    }

    Optional<String> receive() {
	try {
	    String rec = zmqsocket.recvStr();
	    return returnOptionalReply(rec);
	} catch (ZMQException e) {
	    throw new MycosNetworkException("Receiving failed", e);
	}
    }

    void release() {
	try {
	    zmqsocket.close();
	} catch (ZMQException | ZError.CtxTerminatedException | ZError.InstantiationException | ZError.IOException e) {
	    throw new MycosNetworkException("Socket releasing failed", e);
	}
    }

    private Optional<String> returnOptionalReply(String rec) {
	if (Objects.isNull(rec))
	    return Optional.empty();
	else
	    return Optional.of(rec);
    }
}

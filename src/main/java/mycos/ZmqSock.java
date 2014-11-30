package mycos;

import java.util.*;

import org.zeromq.*;

class ZmqSock implements Sock {
    private final ZMQ.Socket socket;

    ZmqSock(ZMQ.Socket socket) {
	this.socket = socket;
    }

    @Override
    public void send(String data) {
	try {
	    socket.send(data);
	} catch (ZMQException e) {
	    throw new SocketException("Sending failed", e);
	}
    }

    @Override
    public Optional<String> receive() {
	try {
	    String rec = socket.recvStr();
	    if (Objects.isNull(rec))
		return Optional.empty();
	    else
		return Optional.of(rec);
	} catch (ZMQException e) {
	    throw new SocketException("Receiving failed", e);
	}
    }
}

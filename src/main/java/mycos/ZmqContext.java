package mycos;

import java.util.*;

import org.zeromq.*;
import org.zeromq.ZMQ.Socket;

final class ZmqContext implements Context {
    private final ZMQ.Context ctx;

    ZmqContext(int workers) {
	ctx = ZMQ.context(workers);
    }

    public Optional<Sock> clientSocket(final String address) {
	try {
	    Socket socket = ctx.socket(ZMQ.REQ);
	    socket.connect(address);
	    return Optional.of(new ZmqSock(socket));
	} catch (RuntimeException e) {
	    // TODO add log
	    return Optional.empty();
	}
    }

    public Optional<Sock> serverSocket(final String address) {
	try {
	    Socket socket = ctx.socket(ZMQ.REP);
	    socket.bind(address);
	    return Optional.of(new ZmqSock(socket));
	} catch (RuntimeException e) {
	    // TODO add log
	    return Optional.empty();
	}
    }
}

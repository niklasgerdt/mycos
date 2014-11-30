package mycos;

import java.util.*;

/**
 * SocketBuilder for constructing clients and servers.
 */
public final class SocketBuilder {
    private final Context context = Mycos.context();

    SocketBuilder() {
    }

    // TODO not yet implemented
    /**
     * Sockets block forever by default. Sockets can have timeout values though.
     */
    public SocketBuilder withTimeOut(final int timeout) throws UnsupportedOperationException {
	throw new UnsupportedOperationException();
    }

    public Client asClientOf(final String serverAddress) {
	Objects.requireNonNull(serverAddress);
	final Optional<Sock> s = context.clientSocket(serverAddress);
	final Client c = new ClientImpl(s.get());
	return c;
    }

    public Server asServerAt(final String serverAddress) {
	Objects.requireNonNull(serverAddress);
	final Optional<Sock> s = context.serverSocket(serverAddress);
	final Server c = new ServerImpl(s.get());
	return c;
    }

}

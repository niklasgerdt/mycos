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

import java.util.Objects;

/**
 * SocketBuilder for constructing clients and servers.
 */
public final class SocketBuilder {
    private final Context context = Context.instance();

    /**
     * Creates a SocketBuilder instance that can be used to build Socket with various configuration settings.
     * SocketBuilder follows the builder pattern, and it is typically used by first invoking various configuration
     * methods to set desired options, and finally calling {@link #asClientOf} or {@link #asServer}.
     */
    SocketBuilder() {
    }

    /**
     * Configures socket timeout to be used on the socket. Sockets block forever by default.
     * 
     * @throws UnsupportedOperationException
     *             not yet implemented
     */
    public SocketBuilder withTimeOut(final int timeout) throws UnsupportedOperationException {
	throw new UnsupportedOperationException();
    }

    /**
     * Creates socket as {@link Client} instance that is connected to the given server address. Sockets are unconnected
     * by design, so server does not have to be in place when calling this method. Subsequent calls to this method with
     * same serverAddress will fail unless the created socket is released by {@link ReleasableSocket} method
     * {@code release} or {@link Mycos} method {@code release}. On the other hand multiple different Clients can be
     * built with similar configuration, if the given server address is different.
     * 
     * @param serverAddress
     *            Address of the server where to send objects.
     * @return Client that can send objects to attached server.
     * @throws MycosNetworkException
     *             Socket creation can fail for various reasons. {@link MycosNetworkException} wraps actual causes.
     * @throws NullPointerException
     *             if serverAddress is null
     */
    public Client asClientOf(final String serverAddress) {
	Objects.requireNonNull(serverAddress);
	final Socket socket = context.clientSocket(serverAddress);
	final Client client = new ClientSocket(socket);
	return client;
    }

    /**
     * Creates socket as {@link Server} that is connected to given server address. Sockets are unconnected by design.
     * Subsequent calls to this method with same server address will fail unless the created socket is released by
     * {@link ReleasableSocket} method {@code release} or {@link Mycos} method {@code release}. On the other hand
     * multiple different Clients can be built with similar configuration, if the given server address is different.
     * 
     * @param serverAddress
     *            Address of the server where {@link Client} can send objects.
     * @return Server that is binded to the given address.
     * @throws MycosNetworkException
     *             Socket creation can fail for various reasons. {@link MycosNetworkException} wraps actual causes.
     * @throws NullPointerException
     *             if serverAddress is null
     */
    public Server asServerAt(final String serverAddress) {
	Objects.requireNonNull(serverAddress);
	final Socket socket = context.serverSocket(serverAddress);
	final Server server = new ServerSocket(socket);
	return server;
    }
}

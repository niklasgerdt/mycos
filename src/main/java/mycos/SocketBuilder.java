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
    private final SocketFactory socketFactory;
    private final CommunicationMedium communicationMedium;

    /**
     * Creates a SocketBuilder instance that can be used to build Socket with various configuration settings.
     * SocketBuilder follows the immutable builder pattern, and it is typically used by first invoking various
     * configuration methods to set desired options, and finally calling the actual builder method ({@link #asClientOf}
     * , {@link #asServer}).
     */
    public static SocketBuilder buildSocket() {
	return new SocketBuilder(GraphBuilder.socketFactory(), CommunicationMedium.TCP);
    }

    private SocketBuilder(SocketFactory socketFactory, CommunicationMedium communicationMedium) {
	this.socketFactory = socketFactory;
	this.communicationMedium = communicationMedium;
    }

    /**
     * Configures Socket to use TCP as communication medium. TCP is used by default.
     *
     * @return a reference to new {@code SocketBuilder} object to fulfil the immutable "Builder" pattern
     */
    public SocketBuilder asRemote() {
	return new SocketBuilder(socketFactory, CommunicationMedium.TCP);
    }

    /**
     * Configures Socket to use ICP as communication medium. TCP is used by default.
     *
     * @return a reference to new {@code SocketBuilder} object to fulfil the immutable "Builder" pattern
     */
    public SocketBuilder asLocal() {
	return new SocketBuilder(socketFactory, CommunicationMedium.IPC);
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
     * same serverAddress will fail unless the created socket is released. On the other hand multiple Clients can be
     * built with same builder by using different server address.
     * 
     * @param serverAddress
     *            Address of the server where to send objects. For remote client the address should be in the following
     *            format: host:port (for example "127.0.0.1:5000"). For local clients any String will do.
     * @return Client that can send objects to attached server.
     * @throws NetworkException
     *             Socket creation can fail for various reasons. {@link NetworkException} wraps actual causes.
     * @throws NullPointerException
     *             if serverAddress is null
     */
    public Client asClientOf(final String serverAddress) {
	Objects.requireNonNull(serverAddress);
	// TODO validate address
	return socketFactory.clientSocket(communicationMedium.prefix() + serverAddress);
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
     * @throws NetworkException
     *             Socket creation can fail for various reasons. {@link NetworkException} wraps actual causes.
     * @throws NullPointerException
     *             if serverAddress is null
     */
    public Server asServerAt(final String serverAddress) {
	Objects.requireNonNull(serverAddress);
	// TODO validate address
	return socketFactory.serverSocket(communicationMedium.prefix() + serverAddress);
    }

    /**
     * @throws UnsupportedOperationException
     *             not yet implemented
     */
    public Publisher asPublisherAt(final String address) {
	throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException
     *             not yet implemented
     */
    public Publisher asPublisherAtWithRouterAt(final String address, final String routerAddress) {
	throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException
     *             not yet implemented
     */
    public Subscriber asSubscriberOf(final String address) {
	throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException
     *             not yet implemented
     */
    public Publisher asSubscriberOfWithFilter(final String address, final String filter) {
	throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException
     *             not yet implemented
     */
    public Router asRouterAt(final String address) {
	throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException
     *             not yet implemented
     */
    public Router asForkedRouterAt(final String address) {
	throw new UnsupportedOperationException();
    }
}

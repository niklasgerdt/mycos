package mycos;

import java.util.*;

interface Sock {

    /**
     * Method for sending data over the wire.
     * 
     * @param data
     *            the message to send
     * @throws SocketException
     *             if there was a networking related issues.
     */
    void send(String data);

    /**
     * Method for receiving data over the wire.
     * 
     * @return The received data as Optional<String> or empty, if no data before
     *         timeout.
     * @throws SocketException
     *             if there was a networking related issues.
     */
    public Optional<String> receive();

}

package mycos;

import java.util.*;

public interface Client {

    /**
     * @throws SocketException
     *             if there was a networking related issues.
     */
    <C, S> Later<S> ask(C object);

    /**
     * @throws SocketException
     *             if there was a networking related issues.
     */
    <C, S> Optional<S> askAndWait(C clientObject);

}

package mycos;

import java.util.*;

public interface Context {

    Optional<Sock> clientSocket(final String address);

    Optional<Sock> serverSocket(final String address);

}

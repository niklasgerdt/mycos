package mycos.lookandfeel;

import static org.junit.Assert.*;
import java.util.*;
import java.util.concurrent.*;
import org.junit.Test;
import mycos.*;

@SuppressWarnings("unused")
public class SocketCreation {

    @Test(expected = NullPointerException.class)
    public void createClientRequiresValidAddress() {
	final Client client = SocketBuilder.buildSocket().asClientOf(null);
	fail();
    }

    @Test
    public void createsClientToGivenAddress() {
	final Client client = SocketBuilder.buildSocket().asClientOf("localhost:8000");
	assertNotNull(client);
    }

    @Test(expected = NullPointerException.class)
    public void createServerRequiresValidAddress() {
	final Server S = SocketBuilder.buildSocket().asServerAt(null);
	fail();
    }

    @Test
    public void createsServerToGivenAddress() {
	final Server s = SocketBuilder.buildSocket().asServerAt("localhost:8000");
	assertNotNull(s);
    }
}

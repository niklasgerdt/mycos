package mycos.lookandfeel;

import java.util.*;
import java.util.concurrent.*;
import mycos.*;

@SuppressWarnings("unused")
public class SocketCreation {
    {
	TestObject to = new TestObject();

	// Creating new client socket
	// final Client client = Mycos.buildSocket().asClientOf("tcp://localhost:6060");

	// Creating new server socket
	// final Server server = Mycos.buildSocket().asServerAt("tcp://localhost:6060");

	// Creating new client socket with timeout
	// final Client clientWitTo = Mycos.buildSocket().withTimeOut(0).asClientOf("tcp://localhost:6060");

	// Sending asynchronous request
	// Optional<Future<Optional<String>>> opt = client.<TestObject, String>
	// request(to);
	// Working while waiting reply
	// Future<Optional<String>> fut = opt.get();
	// Getting the optional reply
	// Optional<String> strOpt = null;
	// try {
	// strOpt = fut.get();
	// } catch (InterruptedException e) {
	// } catch (ExecutionException e) {
	// }
	// Getting the actual reply
	// String str = strOpt.get();

	// Maybe<String> maybe = client.request3(to);
	// Later<String> later = maybe.get();
	// Optional<String> optional = later.get();
	// String string = optional.get();

	// Sending synchronous request
	// Optional<String> rep = client.sendAndWait(to);
    }
}

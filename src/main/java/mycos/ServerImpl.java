package mycos;

import java.util.concurrent.*;

import com.google.gson.*;

final class ServerImpl implements Server {
    // TODO how many executors and how
    private final ExecutorService exec;
    private final Gson gson = new Gson();
    private final Sock sock;

    ServerImpl(Sock socket) {
	sock = socket;
	// TODO common executor among sockets
	exec = Executors.newCachedThreadPool();
    }

}

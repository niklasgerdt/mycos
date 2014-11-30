package mycos;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

import com.google.gson.*;
import com.google.gson.reflect.*;

final class ClientImpl implements Client {
    private final ExecutorService exec;
    private final Gson gson = new Gson();
    private final Sock sock;

    ClientImpl(Sock s) {
	sock = s;
	exec = Executors.newSingleThreadExecutor();
    }

    @Override
    public <C, S> Later<S> ask(C object) {
	Future<Optional<S>> future = exec.submit(() -> {
	    String json = gson.toJson(object);
	    sock.send(json);
	    Optional<String> reply = sock.receive();
	    if (reply.isPresent()) {
		S s = jsonToObject(reply.get());
		return Optional.of(s);
	    } else {
		return null;
	    }
	});
	return new Later<S>(future);

    }

    @Override
    public <C, S> Optional<S> askAndWait(C object) {
	String json = gson.toJson(object); // RE
	sock.send(json);
	Optional<String> reply = sock.receive();
	if (reply.isPresent()) {
	    S s = jsonToObject(reply.get());
	    return Optional.of(s);
	} else {
	    return null;
	}
    }

    private <T> T jsonToObject(String reply) {
	Type type = new TypeToken<T>() {
	}.getType();
	T t = gson.fromJson(reply, type);
	return t;
    }
}

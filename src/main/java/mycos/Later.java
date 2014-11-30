package mycos;

import java.util.*;
import java.util.concurrent.*;

/**
 * Later wraps Java's Future and converts possible exceptions to Mycos
 * exceptions. It also hides away thread cancelling and offers only limited view
 * of Java Future. For example thread cancelling is omitted, because issues
 * related to timeouts should be handled when creating sockets.
 * 
 * @param <T>
 *            Type parameter for {@link #get()}.
 */
public class Later<T> {
    private final Future<Optional<T>> future;

    Later(final Future<Optional<T>> future) {
	this.future = future;
    }

    public boolean done() {
	return future.isDone();
    }

    public Optional<T> get() {
	try {
	    return future.get();
	} catch (InterruptedException | ExecutionException e) {
	    throw new RuntimeException("have to change this type!, only latter is possible!", e);
	}
    }
}

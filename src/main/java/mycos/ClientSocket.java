/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Niklas Gerdt
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package mycos;

import java.util.*;
import java.util.concurrent.*;

final class ClientSocket implements Client, ReleasableSocket {
    private final Context context = Context.instance();
    private final ExecutorService exec = Executors.newSingleThreadExecutor();
    private final GsonWrapper gson = new GsonWrapper();
    private final Socket socket;

    ClientSocket(Socket s) {
	socket = s;
    }

    /**
     * 
     * This method does not throw, but resulting Wait can throw these exceptions.
     * 
     * @throws MycosParseException
     *             if failed to parse object
     * @throws MycosNetworkException
     *             if network exception occurred
     */
    @Override
    public <C, S> Wait<S> ask(C object) {
	Future<Optional<S>> future = exec.submit(() -> {
	    return sendAndReceive(object);
	});
	return new ReplyWaiter<S>(future);

    }

    /**
     * @throws MycosParseException
     *             if failed to parse object
     * @throws MycosNetworkException
     *             if network exception occurred
     */
    @Override
    public <C, S> Optional<S> askAndWait(C object) {
	return sendAndReceive(object);
    }

    @Override
    public void release() {
	try {
	    context.release(socket);
	} catch (RuntimeException e) {
	    throw new MycosNetworkException("Socket releasing failed", e);
	}
    }

    private <C, S> Optional<S> sendAndReceive(C object) {
	String json = gson.toJson(object);
	socket.send(json);
	Optional<String> reply = socket.receive();
	if (reply.isPresent())
	    return Optional.of(gson.fromJson(reply.get()));
	else
	    return Optional.empty();
    }
}

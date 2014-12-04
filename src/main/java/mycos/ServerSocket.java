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

import java.util.Optional;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQException;
import com.google.gson.JsonParseException;

final class ServerSocket implements Server {
    private final NetworkContextStateManager contextStateManager;
    private final GsonWrapper gson;
    private final ZMQ.Socket zmqsocket;

    ServerSocket(NetworkContextStateManager networkContextStateManager, Socket zmqsocket, GsonWrapper gson) {
        this.gson = gson;
        this.contextStateManager = networkContextStateManager;
        this.zmqsocket = zmqsocket;
    }

    @Override
    public <V> Optional<V> hang() {
        try {
            final String reply = zmqsocket.recvStr();
            return gson.fromJson(reply);
        } catch (ZMQException e) {
            throw new NetworkException("Network communication failed", e);
        } catch (JsonParseException e) {
            throw new ParseException("Object parsing failed", e);
        }
    }

    @Override
    public <V> void reply(V object) {
        String json = gson.toJson(object);
        zmqsocket.send(json);
    }

    @Override
    public void release() {
        contextStateManager.destroySocket(zmqsocket);
    }
}

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
package mycos.systemtests;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Optional;
import mycos.*;
import org.junit.Test;

public class ClientServerTest {
    private static final String REQUEST = "request";
    private int calls = 0;

    void release(Socket... sockets) {
        Arrays.asList(sockets).stream().forEach(s -> s.release());
    }

    @Test
    public void simpleRequestReplyWithoutActualServerInPlace() throws InterruptedException {
        final Client client = SocketBuilder.buildSocket().asClientOf("localhost:8000");
        @SuppressWarnings("unused")
        Wait<String> waitReply = client.ask(REQUEST, String.class);
        Thread.sleep(1000);
        Server s = SocketBuilder.buildSocket().asServerAt("localhost:8000");
        s.onRequest(String.class, (Optional<String> v) -> v.get(), () -> calls++ < 1);
        release(client, s);
    }

    @Test
    public void canCreateMultipleClientsToSameServer() {
        Client c1 = SocketBuilder.buildSocket().asClientOf("localhost:8000");
        Client c2 = SocketBuilder.buildSocket().asClientOf("localhost:8000");
        Client c3 = SocketBuilder.buildSocket().asClientOf("localhost:8000");
        Server s = SocketBuilder.buildSocket().asServerAt("localhost:8000");
        Wait<String> w1 = c1.ask("C1", String.class);
        Wait<String> w2 = c2.ask("C2", String.class);
        Wait<String> w3 = c3.ask("C3", String.class);
        s.onRequest(String.class, (Optional<String> v) -> v.get(), () -> calls++ < 3);
        assertEquals("C1", w1.get().get());
        assertEquals("C2", w2.get().get());
        assertEquals("C3", w3.get().get());
        release(c1, c2, c3, s);
    }
}

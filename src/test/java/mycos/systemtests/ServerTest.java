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
import java.util.Optional;
import mycos.Client;
import mycos.Serve;
import mycos.Server;
import mycos.SocketBuilder;
import mycos.TestObject;
import mycos.Until;
import mycos.Wait;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerTest {
    private static final String RESPOND = "RESPOND";
    private static final String REQUEST = "REQUEST";
    private static final Optional<String> REQ = Optional.of(REQUEST);
    private Client client;
    private Server server;
    private int testCalls;
    private Until ones = () -> testCalls++ < 1;

    @Before
    public void before() {
        client = SocketBuilder.buildSocket().asClientOf("localhost:8000");
        server = SocketBuilder.buildSocket().asServerAt("localhost:8000");
        testCalls = 0;
    }

    @After
    public void releaseSockets() {
        client.release();
        server.release();
    }

    @Test
    public void serveFunctionTest() {
        Serve<String, String> baseServe = (Optional<String> v) ->
        {
            assertEquals(REQUEST, v.get());
            return RESPOND;
        };
        assertEquals(RESPOND, baseServe.serve(REQ));
    }

    @Test
    public void respondsImplicitly() {
        Wait<String> w = client.ask(REQUEST, String.class);
        Serve<String, String> serve = (Optional<String> v) ->
        {
            return RESPOND;
        };
        server.onRequest(String.class, serve, ones);
        String reply = w.get().get();
        assertEquals(RESPOND, reply);
    }

    @Test
    public void customObjects() {
        Wait<TestObject> w = client.ask(new TestObject(REQUEST), TestObject.class);
        Serve<TestObject, TestObject> serve = (Optional<TestObject> v) ->
        {
            assertEquals(REQUEST, v.get().getData());
            return new TestObject(RESPOND);
        };
        server.onRequest(TestObject.class, serve, ones);
        assertEquals(RESPOND, w.get().get().getData());
    }

    @Test
    public void servesMoreThanOnce() {
        Wait<TestObject> w1 = client.ask(new TestObject(REQUEST), TestObject.class);
        Wait<TestObject> w2 = client.ask(new TestObject(REQUEST), TestObject.class);
        Serve<TestObject, TestObject> serve = (Optional<TestObject> v) ->
        {
            assertEquals(REQUEST, v.get().getData());
            return new TestObject(RESPOND);
        };
        server.onRequest(TestObject.class, serve, () -> testCalls++ < 2);
        assertEquals(RESPOND, w1.get().get().getData());
        assertEquals(RESPOND, w2.get().get().getData());
    }
}

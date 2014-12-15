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

import java.util.*;

/**
 * Client is special kind of socket. It can send requests to server and either block and wait for the result or send the
 * request and retrieve {@link Wait} that wraps the result. With {@link Wait} the client can do some work while the
 * processing of the result is in progress.
 */
public interface Client extends Socket {

    /**
     * This method forks request that waits for reply. Method returns {@link Wait}. User can can use it to retrieve the
     * status of the request or the actual result.
     * 
     * @return Object that represents asynchronous reply result.
     * @throws IllegalStateException
     *             if the socket is already released
     */
    <C, S> Wait<S> ask(C object, Class<S> receiveAs);

    /**
     * This method makes the request and waits for the reply.
     * 
     * @return Reply wrapped in {@link Optional}
     * @throws ParseException
     *             if failed to parse object
     * @throws NetworkException
     *             if network exception occurred
     * @throws IllegalStateException
     *             if the socket is already released
     */
    <C, S> Optional<S> askAndWait(C clientObject, Class<S> receiveAs);

}

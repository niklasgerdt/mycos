/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Niklas Gerdt
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package mycos;

/**
 * Mycos provides simple networking for distributed Java based applications.
 * <p>
 * Mycos lets you create CLients and Servers that can communicate by sending
 * plain old java objects. Clients can ask replies from the server
 * asynchronously or synchronously.
 * <p>
 * Mycos is built on top of ZeroMq (See <a
 * href="http://zeromq.org/">http://zeromq.org/</a>) networking libraries.
 * Objects are serialised by Gson (See <a
 * href="http://code.google.com/p/google-gson/">http
 * ://code.google.com/p/google-gson/</a>).
 */
public class Mycos {
    private static final Context context = Context.instance();

    /**
     * Static factory method for creating new {@link SocketBuilder}.
     * 
     * @return new {@link SocketBuilder} instance for creating {@link Server} or
     *         {@link Client}
     */
    public static SocketBuilder buildSocket() {
	return new SocketBuilder();
    }

    /**
     * Causes Mycos to explicitly set up the networking environment and read
     * optional configurations. It is advised to call this method before actual
     * socket creation, since then the first socket creation implicitly sets up
     * the environment causing possibly random length brake.
     */
    public static void setup() {
	// forces context to load
    }

    /**
     * Causes Mycos to tear down the networking environment and release acquired
     * resources.
     */
    public static void release() {
	try {
	    context.release();
	} catch (RuntimeException e) {
	    throw new MycosNetworkException("Failed to release sockets", e);
	}
    }
}

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

import java.util.*;
import java.util.concurrent.*;

/**
 * This class provides a base implementation of {@link Wait}, with methods to
 * see if the computation is complete and retrieve the result of the
 * computation. The result can only be retrieved when the computation has
 * completed; the {@code get} methods will block if the computation has not yet
 * completed.
 *
 * @param <V>
 *            The result type returned by {@code get} methods wrapped in
 *            {@link Optional}
 */
public class ReplyWaiter<V> implements Wait<V> {
    private final Future<Optional<V>> future;

    ReplyWaiter(final Future<Optional<V>> future) {
	this.future = future;
    }

    /**
     * @throws NetworkException
     *             {@inheritDoc}
     */
    public Optional<V> get() {
	try {
	    return future.get();
	} catch (InterruptedException | ExecutionException e) {
	    throw new NetworkException("This should never happen, since we are not throwing checked exceptions", e);
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDone() {
	return future.isDone();
    }
}

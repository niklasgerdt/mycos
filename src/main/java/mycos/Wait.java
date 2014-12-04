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
import java.util.concurrent.*;

/**
 * A {@code Wait} represents the result of an asynchronous computation. Method {@code isDone} is provided to check if
 * the computation is complete. Method {@code get} is provided to wait for its completion, and to retrieve the result of
 * the computation. The result can only be retrieved using method {@code get} when the computation has completed,
 * blocking if necessary until it is ready.
 * <p>
 * {@code Wait} acts like {@link Future}, but offers limited operations. Cancellation and timeouts are omitted. Timeouts
 * should be handled on socket level and no double mechanism is provided for timeouts and cancellations.
 * 
 * @see Future
 * @param <V>
 *            The result type wrapped in {@link Optional} returned by this Wait's {@code get} method
 * @throws NetworkException
 *             This exception is raised if there is a serious issue that occurs during networking
 * @throws ParseException
 *             This exception is raised if there is a serious issue that occurs during object parsing
 * @throws UnknownException
 *             This exception is raised if there is a serious issue which source can't be resolved
 */
public interface Wait<V> {

    /**
     * Returns {@code true} if this task completed. Completion may be due to normal termination or an exception.
     *
     * @return {@code true} if this task completed
     */
    boolean isDone();

    /**
     * Waits if necessary for the computation to complete, and then retrieves its result. {@code Optional.empty} is
     * returned, if there is no result.
     *
     * @return the computed result as {@code Optional<V>} or {@code Optional.empty}, if no result
     * @throws NetworkException
     *             if the current thread experienced exception
     */
    Optional<V> get();
}

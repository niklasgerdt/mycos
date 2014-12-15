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

/**
 * This exception is raised if there is a unexpected exception and the library can't categorise it. This class is only
 * thin wrapper around more specific exception.
 * <p>
 * Receiving {@code UnknownException} indicates a bug in the library. Maybe report an issue, please! *
 * <p>
 * This exception is a {@link RuntimeException} because clients can not be expected to know how to recover from this.
 */
public class UnknownException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    UnknownException(String msg) {
        super(msg);
    }

    UnknownException(String msg, Throwable cause) {
        super(msg, cause);
    }

    UnknownException(Throwable cause) {
        super(cause);
    }
}

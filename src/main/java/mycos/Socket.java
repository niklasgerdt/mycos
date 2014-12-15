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
 * Socket interface..
 */
public interface Socket {

    /**
     * Calling release releases the socket from networking resources. This socket can not be used after release and
     * calls to this socket will fail after the socket is released.
     */
    void release();

    /**
     * Method to query the status of the socket.
     * 
     * @return true if the socket is released (invalid). Otherwise false.
     */
    boolean released();

    /**
     * Method to query the status of the socket.
     * 
     * @return true if the socket is unreleased (valid). Otherwise false.
     */
    default boolean valid() {
        return !this.released();
    }

    /**
     * Validates the state of the socket.
     * 
     * @throws IllegalStateException
     *             if the socket is already released
     */
    default void validateState() {
        if (this.released())
            throw new IllegalStateException("Socket is released and in invalid state!");
    }

    /**
     * Validates the state of the socket.
     * 
     * @param socket
     *            to validate
     * @throws IllegalStateException
     *             if the socket is already released
     */
    static void validateState(Socket socket) {
        if (socket.released())
            throw new IllegalStateException("Socket is released and in invalid state!");
    }

}

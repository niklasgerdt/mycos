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
 * This exception is raised if there is a serious issue that occurs during object parsing.
 * <p>
 * This class is only thin wrapper around more specific exception. The main usages for this class is for Gson parsing
 * exceptions.
 * <p>
 * This exception is a {@link RuntimeException} because often clients do not know how to recover from a parsing error.
 * So it is often the case that you want to blow up if there is a parsing error.
 */
public class ParseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    ParseException(String msg) {
        super(msg);
    }

    ParseException(String msg, Throwable cause) {
        super(msg, cause);
    }

    ParseException(Throwable cause) {
        super(cause);
    }

}

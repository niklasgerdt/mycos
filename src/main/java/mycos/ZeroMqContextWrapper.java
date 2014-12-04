package mycos;

import org.zeromq.ZMQ;

public class ZeroMqContextWrapper {
    private static final int IO_THREADS = 1;
    private ZMQ.Context ctx;

    ZeroMqContextWrapper() {
    }

    void init() {
	ctx = ZMQ.context(IO_THREADS);
    }

    ZMQ.Socket socket(final int type) {
	return ctx.socket(type);
    }

    void close() {
	ctx.close();
    }
}

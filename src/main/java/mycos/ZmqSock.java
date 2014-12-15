package mycos;

import org.zeromq.ZMQ;

// Not final because of Mockito and unit tests
class ZmqSock {
    private final ZMQ.Socket zmqsocket;

    ZmqSock(ZMQ.Socket socket) {
        zmqsocket = socket;
    }

    void connect(final String address) {
        zmqsocket.connect(address);
    }

    void bind(final String address) {
        zmqsocket.bind(address);
    }

    void close() {
        zmqsocket.close();
    }

    String recvStr() {
        return zmqsocket.recvStr();
    }

    void send(String data) {
        zmqsocket.send(data);
    }
}

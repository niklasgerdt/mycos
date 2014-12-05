package mycos;

import org.zeromq.ZMQ;

class ZmqSock {
  private final ZMQ.Socket zmqsock;

  ZmqSock(ZMQ.Socket socket) {
    zmqsock = socket;
  }

  void connect(final String address) {
    zmqsock.connect(address);
  }

  void bind(final String address) {
    zmqsock.bind(address);
  }

  void close() {
    zmqsock.close();
  }

  public String recvStr() {
    return zmqsock.recvStr();
  }

  public void send(String data) {
    zmqsock.send(data);
  }
}

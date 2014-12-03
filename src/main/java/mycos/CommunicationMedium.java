package mycos;

enum CommunicationMedium {
    TCP("tcp://"), IPC("ipc://");

    private String prefix;

    private CommunicationMedium(String prefix) {
	this.prefix = prefix;
    }

    String prefix() {
	return prefix;
    }
}

package mycos;

/**
 * This exception is raised if there is a serious issue that occurs during
 * networking. One of the main usages for this class is for the ZeroMQ and/or
 * networking infrastructure.
 *
 * <p>
 * This exception is a {@link RuntimeException} because often clients do not
 * know how to recover from a network error. So it is often the case that you
 * want to blow up if there is a network error.
 * </p>
 */
public class SocketException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SocketException(String msg) {
	super(msg);
    }

    public SocketException(String msg, Throwable cause) {
	super(msg, cause);
    }

    public SocketException(Throwable cause) {
	super(cause);
    }
}

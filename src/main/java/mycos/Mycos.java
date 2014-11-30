package mycos;

public class Mycos {
    private static final Context context;

    static {
	MycosConfigurator conf = new MycosConfigurator();
	context = conf.getContext();
    }

    static Context context() {
	return context;
    }

    public static SocketBuilder buildSocket() {
	return new SocketBuilder();
    }

    /**
     * Causes Mycos to explicitly set up the networking environment and read
     * optional configurations. It is advised to call this method before actual
     * socket creation, since then the first socket creation implicitly sets up
     * the environment causing possibly long brake.
     */
    public static void up() {
	// forces static initialiser
    }

    /**
     * Causes Mycos to tear down the networking environment and release acquired
     * resources.
     */
    public static void down() {

    }
}

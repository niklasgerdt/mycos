package mycos;

final class MycosConfigurator {
    private static final String CONTEXT = "mycos.workers";
    private static final int WORKERS = 1;
    private final Context context;

    MycosConfigurator() {
	String workersStr = System.getProperty(CONTEXT, String.valueOf(WORKERS));
	final int workers = Integer.parseInt(workersStr);
	context = createContext(workers);
    }

    Context getContext() {
	return context;
    }

    private Context createContext(final int workers) {
	return new ZmqContext(workers);
    }
}
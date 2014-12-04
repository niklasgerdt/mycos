package mycos;

import static org.junit.Assert.*;
import mycos.NetworkContextStateManager;
import mycos.ZeroMqContextWrapper;
import org.junit.Test;

// NetworkContextStateManager is the core class of this application and should be tested exhaustive. This test is
// stateful against common unit testing recommendations. It also uses actual zeromq context where as
// NetworkContextStateManagerTest uses mocks.
public class NetworkContextStateManagerSystemTest {
    private ZeroMqContextWrapper contextWrapper = new ZeroMqContextWrapper();
    private NetworkContextStateManager manager = new NetworkContextStateManager(contextWrapper);

    @Test
    public void canCreateMultipleClientsToSameServer() {
    }
}

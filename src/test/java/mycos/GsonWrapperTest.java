package mycos;

import static org.junit.Assert.*;
import java.util.Optional;
import org.junit.Test;
import com.google.gson.Gson;

public class GsonWrapperTest {
    private static final String TESTSTRING = "TEST";
    private static final String TESTJSON = "\"TEST\"";
    private static final String ASJSON = "{\"data\":\"test\"}";
    private static final TestObject ASOBJ = new TestObject("test");
    private final GsonWrapper gw = new GsonWrapper(new Gson());

    @Test
    public void mapsStringToJson() {
        assertEquals(TESTJSON, gw.toJson(TESTSTRING));
    }

    @Test
    public void mapsCustomObjectToJson() {
        assertEquals(ASJSON, gw.toJson(ASOBJ));
    }

    @Test
    public void mapsFromJsonToString() {
        Optional<String> actual = gw.fromJson(TESTJSON, String.class);
        assertEquals(TESTSTRING, actual.get());
    }

    @Test
    public void mapsFromString() {
        Optional<TestObject> actual = gw.fromJson(ASJSON, TestObject.class);
        assertEquals(ASOBJ.getData(), actual.get().getData());
    }
}

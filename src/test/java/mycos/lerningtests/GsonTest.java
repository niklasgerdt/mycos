package mycos.lerningtests;

import static org.junit.Assert.assertEquals;
import mycos.TestObject;
import org.junit.Test;
import com.google.gson.Gson;

public class GsonTest {
    private static final String ASJSON = "{\"data\":\"test\"}";
    private static final TestObject ASOBJ = new TestObject("test");
    private final Gson gson = new Gson();

    @Test
    public void toJson() {
        String json = gson.toJson(ASOBJ);
        assertEquals(ASJSON, json);
    }

    @Test
    public void fromJson() {
        TestObject to = gson.fromJson(ASJSON, TestObject.class);
        assertEquals(ASOBJ.getClass(), to.getClass());
    }

    @Test
    public void fieldTest() {
        TestObject to = gson.fromJson(ASJSON, TestObject.class);
        assertEquals("test", to.getData());
    }

    @Test
    public void genericTest() {
        // Java's type erasure limits
        TestObject to = testAsGeneric(ASJSON, TestObject.class);
        assertEquals("test", to.getData());
    }

    public <T> T testAsGeneric(String json, Class<T> as) {
        T t = gson.fromJson(json, as);
        return t;
    }
}

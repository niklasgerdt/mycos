package mycos.lerningtests;

import static org.junit.Assert.*;
import mycos.systemtests.TestObject;

import org.junit.Test;

import com.google.gson.Gson;

public class GsonTest {
	private static final String ASJSON = "{\"i\":1,\"s\":\"test\",\"data\":\"test\"}";
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
		TestObject to = gson.fromJson(ASJSON, TestObject.class);
		assertEquals("test", to.getData());
	}
	
	<T> T generic(T t){
		return t;
	}
}

package mycos.systemtests;

public class TestObject {

	@SuppressWarnings("unused")
	private int i = 1;
	@SuppressWarnings("unused")
	private String s = "test";
	private String data;

	public TestObject() {
	}

	public TestObject(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}
}

package mycos.lerningtests;

import static org.junit.Assert.*;

import org.junit.*;

public class Properties {

    @Test
    public void set() {
	System.setProperty("my", "property");
	assertEquals("property", System.getProperty("my"));
    }

    @Test
    public void notSet() {
	assertEquals(null, System.getProperty("my"));
    }
}

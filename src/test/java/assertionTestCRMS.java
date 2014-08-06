package test.java;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.zmqcrms.rrserverCRMS;

public class assertionTestCRMS {

	private rrserverCRMS tester;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		tester = new rrserverCRMS();
	}

	@Test
	public void test(){
		String crmsIdtest = "c11-00220";
		String fakecrmsIdtest = "S11-002200";
		//testing convertString
		assertEquals("equal", "c11-00220", tester.convertString(crmsIdtest));
		assertEquals("equal", "c11-002200", tester.convertString(fakecrmsIdtest));
	}
	
	@Test
	public void testFakePersonId() throws InterruptedException, IOException{
		String fakecrmsIdtest = "S11-002220";
		String fakecrmsIdconverted = "c11-002220";
		//testing if person does not exist
		String noStudyResult = "Study: "+fakecrmsIdconverted+" related studies not found!";
		tester.reset();
		assertEquals("equal", noStudyResult, tester.start(fakecrmsIdtest));
	}
	
	@Test
	public void testPersonId() throws InterruptedException, IOException{
		String crmsIdtest = "S11-00220";
		String result = tester.start(crmsIdtest);
		tester.reset();
		assertEquals("equal", result, tester.start(crmsIdtest));
	}

}

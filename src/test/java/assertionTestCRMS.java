package test.java;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import zeromqTest.zmqCRMS.rrserverCRMS;

public class assertionTestCRMS {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test(){
		rrserverCRMS tester = new rrserverCRMS();
		String crmsIdtest = "c11-00220";
		String fakecrmsIdtest = "S11-002200";
		//testing convertString
		assertEquals("equal", "c11-00220", tester.convertString(crmsIdtest));
		assertEquals("equal", "c11-002200", tester.convertString(fakecrmsIdtest));
	}
	
	@Test
	public void testFakePersonId() throws InterruptedException, IOException{
		rrserverCRMS tester = new rrserverCRMS();
		String fakecrmsIdtest = "S11-002220";
		String fakecrmsIdconverted = "c11-002220";
		//testing if person does not exist
		String noStudyResult = "Study: "+fakecrmsIdconverted+" related studies not found!";
		assertEquals("equal", noStudyResult, tester.start(fakecrmsIdtest));
	}
	
	@Test
	public void testPersonId() throws InterruptedException, IOException{
		rrserverCRMS tester = new rrserverCRMS();
		String crmsIdtest = "S11-00220";
		String result = tester.start(crmsIdtest);
		tester.reset();
		assertEquals("equal", result, tester.start(crmsIdtest));
	}

}

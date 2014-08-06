package test.java;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.zmqirb.rrserverIRB;

public class assertionTestIRB {

	private rrserverIRB tester;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		tester = new rrserverIRB();
	}

	@Test
	public void test(){
		String irbIdtest = "S1444";
		String fakeirbIdtest = "S1444111";
		//testing convertString
		assertEquals("equal", "i1444", tester.convertString(irbIdtest));
		assertEquals("equal", "i1444111", tester.convertString(fakeirbIdtest));
	}
	
	@Test
	public void testFakePersonId() throws InterruptedException, IOException{
		String fakeirbIdtest = "S144412";
		String fakeirbIdconverted = "i144412";
		//testing if person does not exist
		String noStudyResult = "Study: "+fakeirbIdconverted+" related studies not found!";
		tester.reset();
		assertEquals("equal", noStudyResult, tester.start(fakeirbIdtest));
	}
	
	@Test
	public void testPersonId() throws InterruptedException, IOException{
		String irbIdtest = "S1444";
		String result = tester.start("i1444");
		tester.reset();
		assertEquals("equal", result, tester.start(irbIdtest));
	}

}

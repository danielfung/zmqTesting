package test.java;

import static org.junit.Assert.*;

import java.io.IOException;

import main.java.zmqauth.rrserverAuth;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;



public class assertionTestAuth {

	private rrserverAuth tester;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
			tester = new rrserverAuth();
	}
	
	@Test
	public void test(){
		String personIdtest = "com.webridge.account.Person[OID[04703945D4509F42837E8561B0556F18]]";
		String fakepersonIdtest = "com.webridge.account.Person[OID[04703945D4509F42837E8561B0556F181]]";
		//testing convertString
		assertEquals("equal", "com.webridge.account.Person:04703945D4509F42837E8561B0556F18", tester.convertString(personIdtest));
		assertEquals("equal", "com.webridge.account.Person:04703945D4509F42837E8561B0556F181", tester.convertString(fakepersonIdtest));
	}
	
	@Test
	public void testFakePersonId() throws InterruptedException, IOException{
		String fakepersonIdtest = "com.webridge.account.Person[OID[04703945D4509F42837E8561B0556F181]]";
		String fakepersonIdconverted = "com.webridge.account.Person:04703945D4509F42837E8561B0556F181";
		//testing if person does not exist
		String noStudyResult = "Person "+fakepersonIdconverted+" has no studies or does not exist";
		tester.reset();
		assertEquals("equal", noStudyResult, tester.start(fakepersonIdtest));
	}
	
	@Test
	public void testPersonId() throws InterruptedException, IOException{
		String personIdtest = "com.webridge.account.Person[OID[04703945D4509F42837E8561B0556F18]]";	
		//testing if person does exist
		tester.reset();
		String result = tester.start(personIdtest);
		assertEquals("equal", result, tester.start(personIdtest));
	}

}

package pt.upa.transporter.ws;

import org.junit.*;

import static org.junit.Assert.*;

public class RequestJobTest {

	private static TransporterPort tp = null;


	@BeforeClass
    public static void oneTimeSetUp() { 	
    	tp = new TransporterPort();
    	tp.setAll("http://localhost:9090", "UpaTranporter2", "http://localhost:8081/transporter-ws/endpoint");
    }

    @AfterClass
    public static void oneTimeTearDown() {
    	tp = null;
    }

    @Before
    public void setUp() {

    }
    @After
    public void tearDown() {

    }

    @Test
    public void goodTest() {
    	try {	
			assertEquals(JobStateView.PROPOSED,tp.requestJob("Coimbra", "Porto", 20).getJobState());
		} catch (BadLocationFault_Exception | BadPriceFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
    
    @Test
    public void goodTestNull() {
    	try {	
			assertEquals(null,tp.requestJob("Coimbra", "Porto", 220));
		} catch (BadLocationFault_Exception | BadPriceFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
    
    @Test
    public void goodTestNullLocation() {
    	try {	
			assertEquals(null,tp.requestJob("Faro", "Porto", 20));
		} catch (BadLocationFault_Exception | BadPriceFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
    
    
    @Test(expected = BadPriceFault_Exception.class)
    public void testWrongPrice() throws BadLocationFault_Exception, BadPriceFault_Exception {
        tp.requestJob("Coimbra", "Porto", -20);
    }
    
    @Test(expected = BadLocationFault_Exception.class)
    public void testWrongOrigin() throws BadLocationFault_Exception, BadPriceFault_Exception {
        tp.requestJob("Where u at?!", "Porto", 20);
    }
    
    @Test(expected = BadLocationFault_Exception.class)
    public void testWrongDestination() throws BadLocationFault_Exception, BadPriceFault_Exception {
        tp.requestJob("Coimbra", "WHERE U AT?!", 20);
    }
    
    

}
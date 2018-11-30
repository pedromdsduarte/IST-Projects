package pt.upa.transporter.ws;

import org.junit.*;

import static org.junit.Assert.*;


public class DecideJobTest {

	private static TransporterPort tp = null;
	
	private static String TRANSPORTER = "UpaTranporter2";
	private static String ORIGIN = "Lisboa";
	private static String DESTINATION = "Porto";
	private static JobStateView PROPOSED_STATE = JobStateView.PROPOSED; 
	private static Integer PRICE = 42;
	private static String ID = "abfc35a3-e92a-4e0d-a2b1-c875152b9f19";
	private static JobView good_job;
	
	@BeforeClass
    public static void oneTimeSetUp() { 	
    	tp = new TransporterPort();	
    }
    

    @AfterClass
    public static void oneTimeTearDown() {
    	tp = null;
    	
    }




    // initialization and clean-up for each test

    @Before
    public void setUp() {
    	good_job = tp.createJobView(TRANSPORTER,ORIGIN,DESTINATION,PROPOSED_STATE,PRICE);
    	good_job.setJobIdentifier(ID);
    	tp.insertJob(good_job);
    }
    @After
    public void tearDown() {

    }


    @Test
    public void goodTestAccept() {
    	try {	
    		assertEquals(JobStateView.ACCEPTED,tp.decideJob(ID,true).getJobState());
		} catch (BadJobFault_Exception e) {
			e.printStackTrace();
		}	
    }
    
    @Test
    public void goodTestReject() {
    	try {	
    		assertEquals(JobStateView.REJECTED,tp.decideJob(ID,false).getJobState());
		} catch (BadJobFault_Exception e) {
			e.printStackTrace();
		}	
    }
    
    @Test(expected = BadJobFault_Exception.class)
    public void badTestFalse() throws BadJobFault_Exception {
		tp.decideJob("this job doesn't exist", false);	
    }
    
    @Test(expected = BadJobFault_Exception.class)
    public void badTestTrue() throws BadJobFault_Exception {
		tp.decideJob("this job doesn't exist", true);	
    }


}
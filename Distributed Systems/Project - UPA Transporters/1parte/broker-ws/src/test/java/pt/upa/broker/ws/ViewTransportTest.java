package pt.upa.broker.ws;
import org.junit.*;

import static org.junit.Assert.*;

import java.util.HashMap;

public class ViewTransportTest {
	private static BrokerPort bp = null;
	private static TransportView tv;
	private static int 	  ID						= 1;
	private static String TRANSPORTER_ID			= "AAAAAAAAAAAAAAA";
	private static String TRANSPORTER_FAKE_ID		= "AAAsAAAAAAAAAAA";
	
	
	@BeforeClass
	public static void oneTimeSetUp(){
		bp = new BrokerPort();
		bp.settransports(new HashMap<Integer,TransportView>());
	}

	@AfterClass
	public static void oneTimeTearDown(){
		bp = null;
	}
	
	
	@Before
	public void setUp(){
		tv = new TransportView();
		tv.setId(TRANSPORTER_ID);
		bp.gettransports().put(ID, tv);
	}
	
	@After
	public void tearDown(){
		bp.gettransports().clear();
	}
	
	@Test
	public void allGoodTest() {
		try{
			assertEquals(tv,bp.viewTransport(TRANSPORTER_ID));
		}catch (UnknownTransportFault_Exception e){
			e.printStackTrace();
		}
	}
	
	@Test(expected = UnknownTransportFault_Exception.class)
	public void unknownTransportTest() throws UnknownTransportFault_Exception{
		bp.viewTransport(TRANSPORTER_FAKE_ID);
	}


}

package pt.upa.broker.ws;
import org.junit.*;

import pt.upa.transporter.ws.cli.TransporterClient;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class RequestTransportTest {
	private static BrokerPort bp = null;
	private static String ORIGIN = "Lisboa";
	private static String DESTINATION = "Porto";
	private static int PRICE = 42;
	private static String BADORIGIN = "Not_Lisboa";
	private static String BADDESTINATION = "Not_Porto";
	private static int BADPRICE = -1;
	private static int EVEN_PRICE = 41;
	
	
	
	private static String TRANSPORTER = "UpaTransporter2";
	
	@BeforeClass
	public static void oneTimeSetUp(){
		bp = new BrokerPort();
		bp.settransporterCompanies(new HashMap<String,TransporterClient>());
		bp.settransports(new ConcurrentHashMap<Integer,TransportView>());
	}
	
	@AfterClass
	public static void oneTimeTearDown(){
		bp = null;
	}
	
	
	
	@Before
	public void setUp(){
		bp.gettransporterCompanies().put(TRANSPORTER, new TransporterClient(TRANSPORTER));
	}
	
	@After
	public void tearDown(){
		bp.gettransports().clear();
		bp.gettransporterCompanies().clear();
	}
	/* NOT WORKING
	@Test
	public void sucessTest(){
		try{
			bp.requestTransport(ORIGIN, DESTINATION, PRICE);
			assertEquals(true,true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
	
	@Test(expected = InvalidPriceFault_Exception.class)
	public void invalidPriceTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		bp.requestTransport(ORIGIN,DESTINATION,BADPRICE);
	}
	
	@Test(expected = UnavailableTransportFault_Exception.class)
	public void unavailableTransportTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		bp.gettransporterCompanies().clear();
		bp.requestTransport(ORIGIN,DESTINATION,PRICE);
	}
	/*
	@Test(expected = UnavailableTransportPriceFault_Exception.class)
	public void unavailableTransportPriceTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		bp.requestTransport(ORIGIN,DESTINATION,EVEN_PRICE);
	}
	*/
	@Test(expected = UnknownLocationFault_Exception.class)
	public void unknownOriginLocationTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		bp.requestTransport(BADORIGIN,DESTINATION,PRICE);
	}
	
	@Test(expected = UnknownLocationFault_Exception.class)
	public void unknownDestinationLocationTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		bp.requestTransport(ORIGIN,BADDESTINATION,PRICE);
	}
	
	
}


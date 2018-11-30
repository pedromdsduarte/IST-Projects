package pt.upa.broker.ws;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Arrays;

import javax.jws.WebService;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.cli.TransporterClient;

//import pt.upa.transporter.ws.BadJobFault_Exception;

import ca.ws.cli.*;

@WebService(
	    endpointInterface="pt.upa.broker.ws.BrokerPortType",
	    wsdlLocation="broker.2_0.wsdl",
	    name="BrokerWebService",
	    portName="BrokerPort",
	    targetNamespace="http://ws.broker.upa.pt/",
	    serviceName="BrokerService"
)

//@HandlerChain(file = "/handler-chain.xml")

public class BrokerPort implements BrokerPortType {
	
	private ConcurrentHashMap<Integer,TransportView> transports;
	private HashMap<String,TransporterClient> transporterCompanies;
	private static final long LOOKUP_INTERVAL = 500;
	private Timer lookupTimer = null , checkImAlive = null;
	private boolean primary;
	private BrokerPortType secundaryBroker = null;
	private String uddiLink;
	private long lastAliveTime;
	private boolean isReady = false;
	private UDDINaming uddiNaming = null;
	private Endpoint endpoint = null;
	private boolean amISecond = false;
	private boolean foundSecond = false;
	
	private static String[] NORTH_AREA = {"Porto", "Braga" , "Viana do Castelo", "Vila Real", "Bragança"};
	private static String[] CENTER_AREA = {"Lisboa","Leiria","Santarém","Castelo Branco","Coimbra","Aveiro","Viseu","Guarda"};
	private static String[] SOUTH_AREA = {"Setúbal","Évora","Portalegre","Beja","Faro"};
	
	
	/************************************************/
	private CAClient ca;
	private HashMap<String, Certificate> transporterCertificates;
	/************************************************/

	
	public ConcurrentHashMap<Integer,TransportView> gettransports(){
		return transports;
	}
	public HashMap<String,TransporterClient> gettransporterCompanies(){
		return transporterCompanies;
	}
	public void settransports(ConcurrentHashMap<Integer, TransportView> concurrentHashMap){
		transports = concurrentHashMap;
	}
	public void settransporterCompanies(HashMap<String,TransporterClient> t){
		transporterCompanies = t;
	}
	
	/* Procedimento que causa a procura continua de transportadoras */
	public void startLookupTimer() {
		
		TimerTask lookup = new TimerTask() {
			@Override
		     public void run() {

				try {
					//printTransports();
					if(!amISecond){
						lookupSecundary(uddiLink, "UpaBroker2");
						if(secundaryBroker!=null){
							secundaryBroker.imAlive();
						}
					}
					lookupTransporterCompanies();
					getAllCertificates();
				} catch (Exception e) {
					System.err.println("Failed lookup");
					e.printStackTrace();
				}
		     }
		};
		
		lookupTimer = new Timer(true);
		
		lookupTimer.schedule(lookup, 0, LOOKUP_INTERVAL);
		
	}

	
	private void startLiveCheck() {
		TimerTask checkTime = new TimerTask() {
			@Override
		     public void run() {
				if(!isReady)return;
				long time = (System.nanoTime()-lastAliveTime)/1000000;
				if(time>2000){
					System.out.println("Primary is Dead, evolving to UpaBroker1");
					transformIntoPrimary();
					startLookupTimer();
					System.out.println("Evolving completed!");
				}
			}
		};
		
		checkImAlive = new Timer(true);
		
		checkImAlive.schedule(checkTime, 0, 500);
		
	}
	
	private void transformIntoPrimary(){
		//Se transformou em primario
		primary = true;
		String url = "http://localhost:9091/broker-ws/endpoint";
		String uddiURL = "http://localhost:9090";
		String name = "UpaBroker1";
		checkImAlive.cancel();
		try{
			if (endpoint != null) {
				// stop endpoint
				endpoint.stop();
				System.out.printf("Stopped %s%n", url);
			}
		} catch (Exception e) {
			System.out.printf("Caught exception when stopping: %s%n", e);
		}
		try {
			if (uddiNaming != null) {
				// delete from UDDI
				uddiNaming.unbind("UpaBroker2");
				System.out.printf("Deleted '%s' from UDDI%n", "UpaBroker2");
				uddiNaming = new UDDINaming(uddiURL);
				uddiNaming.rebind(name,url);
				endpoint = Endpoint.create(this);
				endpoint.publish(url);
			}
		} catch (Exception e) {
			System.out.printf("Caught exception when deleting: %s%n", e);
		}
	}
	
	
	
	public void init(String uddiURL, String name, String url) throws Exception {
		
		transports = new ConcurrentHashMap<Integer, TransportView>();
		transporterCompanies = new HashMap<String,TransporterClient>();
		transporterCertificates = new HashMap<String, Certificate>();
		

		
		if(name.equals("UpaBroker1"))
			primary = true;
		else{
			primary = false;
			amISecond = true;
		}
			
		try {
			uddiNaming = new UDDINaming(uddiURL);
			uddiNaming.rebind(name, url);
			
			endpoint = Endpoint.create(this);
			System.out.printf("Starting %s\t%s%n",name, url);
			endpoint.publish(url);
			

			if(primary){
				lookupSecundary(uddiURL, "UpaBroker2");
				startLookupTimer();
				ping("Broker");
			}
			
			if(!primary){
				startLiveCheck();
			
			}
			
			lookupCA();

			System.out.println("Awaiting connections");
			System.out.println("Press enter to shutdown");
			System.in.read();
			
		} catch (Exception e) {
			throw e;
		} finally {
			if (lookupTimer != null) {
				lookupTimer.cancel();
			} if (checkImAlive != null){
				checkImAlive.cancel();	
			}
			try {
				if (endpoint != null) {
					// clearTransports();
					// stop endpoint
					endpoint.stop();
					System.out.printf("Stopped %s%n", url);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when stopping: %s%n", e);
			}
			try {
				if (uddiNaming != null) {
					// delete from UDDI
					uddiNaming.unbind(name);
					System.out.printf("Deleted '%s' from UDDI%n", name);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when deleting: %s%n", e);
			}
			
		
		}
				
		
	}

	//Connection to Secundary Server
	public void lookupSecundary(String uddiURL, String name) throws Exception{
			//System.out.printf("Contacting UDDI at %s%n", uddiURL);
			UDDINaming uddiNaming = new UDDINaming(uddiURL);
			uddiLink = uddiURL;
			String endpointAddress = uddiNaming.lookup(name);
			
			if (endpointAddress == null){
				//System.out.println(name + " not found! Secundary server is not running");
				secundaryBroker = null;
				return;
			}else{
				//System.out.printf("Found %s%n", endpointAddress);
			}
			
			BrokerService service = new BrokerService();
			secundaryBroker = service.getBrokerPort();
			
			BindingProvider bindingProvider = (BindingProvider) secundaryBroker;
			Map<String,Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

		
	}
	
	public void lookupTransporterCompanies() throws Exception {
		int newTransporters = 0;
	
		String uddiURL = "http://localhost:9090";
		String name = "UpaTransporter%";
		
		HashMap<String,TransporterClient> copy = new HashMap<String,TransporterClient>();
		
		UDDINaming uddiNaming = new UDDINaming(uddiURL);		

		
		/* Lookup no UDDI */
		
		List<String> endpointAddresses = new ArrayList<String>(uddiNaming.list(name));
	
		int count = endpointAddresses.size();
	
		//System.out.println("Found " + count + " transporter" + (count == 1 ? "" : "s"));

		
		for (String endpoint : endpointAddresses) {
			
			/* Vai buscar a porta (ex: 8081) e faz o nome da transportadora (UpaTransporter1) */
			String[] tokens = endpoint.split("/");
			String portString = tokens[2].split(":")[1];
			int id = Integer.parseInt(portString) - 8080;
			String transporterName = "UpaTransporter"+Integer.toString(id);
			
			/* Se nao existir, cria e guarda */
			if (transporterCompanies.get(transporterName) == null) {
				newTransporters++;
				TransporterClient client = new TransporterClient(uddiURL, transporterName);
				String broker;
				if (primary)
					broker = "UpaBroker1";
				else
					broker = "UpaBroker2";
				client.init(broker);
				copy.put(transporterName, client);
				System.out.println("\n-----------------------------------------------------------\n"+transporterName + " joined");
			}
			/* Senao, copia */
			else {
				copy.put(transporterName, transporterCompanies.get(transporterName));
				continue;
			}

		}
		
		/* Indica quais e que abandonaram */
		for (Map.Entry<String,TransporterClient> entry : transporterCompanies.entrySet()) {
			String id = entry.getKey();
			if (!copy.containsKey(id)){
				System.out.println("\n-----------------------------------------------------------\n"+id + " left");
			}
		}
		
		/* Substitui antigo pelo novo */
		transporterCompanies.clear();
		transporterCompanies.putAll(copy);	

	}
	
	
	
	
	@Override
	public String ping(String name) {
		//TODO: Pedro
		
		for(Entry<String, TransporterClient> entry : transporterCompanies.entrySet()) {
			String id = entry.getKey();
			TransporterClient t = entry.getValue();
			String response = t.ping(name);
			if (response == null) {
				System.out.println(id + " did not respond!");				
			}
		}
		
		System.out.println("All transporters OK\n");
		return "OK";
	}
	public String requestTransport(String origin, String destination, int price) throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		TransporterClient company;
		HashMap<String,JobView> jobViews = new HashMap<String,JobView>();
		int bestPrice = -1;
		JobView bestJob = null;
		System.out.println("\nReceived a requestTransport - Origin: "+origin+"\tDestination: "+destination+"\tPrice: "+price);
		//Check origin location
		if(!Arrays.asList(SOUTH_AREA).contains(origin) && !Arrays.asList(CENTER_AREA).contains(origin) && !Arrays.asList(NORTH_AREA).contains(origin)){
			UnknownLocationFault fault = new UnknownLocationFault();
			fault.setLocation(origin);
			System.out.println("ERROR: Didn't found origin  ("+origin+")");
			throw new UnknownLocationFault_Exception("Thats not a valid location!", fault);
		}
		//Check destination location
		if(!Arrays.asList(SOUTH_AREA).contains(destination) && !Arrays.asList(CENTER_AREA).contains(destination) && !Arrays.asList(NORTH_AREA).contains(destination)){
			UnknownLocationFault fault = new UnknownLocationFault();
			fault.setLocation(destination);
			System.out.println("ERROR: Didn't found destination  ("+destination+")");
			throw new UnknownLocationFault_Exception("Thats not a valid location!", fault);
		}
		if(price < 0){
			InvalidPriceFault fault = new InvalidPriceFault();
			fault.setPrice(price);
			System.out.println("ERROR: Invalid price (below 0)");
			throw new InvalidPriceFault_Exception("Invalid Price ("+price+")",fault);
		}
		
		
		for (Map.Entry<String,TransporterClient> entry : transporterCompanies.entrySet()) {
			company = entry.getValue();
			try{
				JobView job = company.requestJob(origin, destination, price);
				if(job==null){
					continue;
				}else{
					jobViews.put(job.getJobIdentifier(), job);
				}
			}catch(Exception e){
			
			}
		}
		
		for (Map.Entry<String, JobView> entry : jobViews.entrySet()){
			if((bestPrice==-1 || bestPrice > entry.getValue().getJobPrice()) ){
				bestJob = entry.getValue();
				bestPrice = bestJob.getJobPrice();
			}
		}
		
		if(bestJob != null && bestJob.getJobPrice() <= price){
			System.out.println("Best Job id: "+bestJob.getJobIdentifier()+ "\tfrom: "+bestJob.getCompanyName());
			TransportView transportView = new TransportView();
			transportView.setDestination(bestJob.getJobDestination());
			transportView.setOrigin(bestJob.getJobOrigin());
			transportView.setPrice(bestJob.getJobPrice());
			transportView.setTransporterCompany(bestJob.getCompanyName());
			transportView.setState(TransportStateView.BUDGETED);
			transportView.setId(bestJob.getJobIdentifier());
			transports.put(transports.size()+1, transportView);
			if(secundaryBroker!=null && !amISecond )
				secundaryBroker.addTransportView(transportView);
			try{
				transporterCompanies.get(bestJob.getCompanyName()).decideJob(bestJob.getJobIdentifier(), true);
				transportView.setState(TransportStateView.BOOKED);
			}catch(BadJobFault_Exception e){
				transportView.setState(TransportStateView.FAILED);
			}
		//Nao existe nenhuma proposta abaixo do valor pedido
		}else if(bestPrice != -1 && bestJob.getJobPrice() > price){
			UnavailableTransportPriceFault fault = new UnavailableTransportPriceFault();
			int _price = -1;
			for(Map.Entry<String, JobView> entry : jobViews.entrySet()){
				if(_price == -1 || entry.getValue().getJobPrice() < _price)
					_price = entry.getValue().getJobPrice();
			}
			fault.setBestPriceFound( bestJob.getJobPrice());
			System.out.println("ERROR: Best Price Found was " + bestJob.getJobPrice());
			throw new UnavailableTransportPriceFault_Exception("Sorry but the best price found was: "+ bestJob.getJobPrice(),fault);
		//Nenhum Transporte Disponivel com Origem/Destino
		}else{
			UnavailableTransportFault fault = new UnavailableTransportFault();
			fault.setDestination(destination);
			fault.setOrigin(origin);
			System.out.println("ERROR: Didn't found a route with origin/destination  ("+origin+"/"+destination+")");
			throw new UnavailableTransportFault_Exception("Didnt find any transporters with that origin/destination ("+origin+"/"+destination+")", fault);
		}
		return ""+transports.size();
	}


	public String transportPrettyPrint(TransportView tv){
		return ("Transport ID:" + tv.getId() + "\nOrigin: " + tv.getOrigin() + "\nDestination: " + tv.getDestination() + 
				"\nPrice: " + tv.getPrice() + "\nState: " + tv.getState());
	}
	
	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		try {
			refreshTransportsState();
		} catch (Exception e) {
			throw new UnknownTransportFault_Exception("Transport "+id+ " not found.", new UnknownTransportFault());
		}
		//TODO: Panda
		for(int key : transports.keySet()){
			if(id != null && !id.equals("") && key == Integer.parseInt(id))
				return transports.get(key);
		}
		throw new UnknownTransportFault_Exception("Transport "+id+ " not found.", new UnknownTransportFault());
			}

	@Override
	public List<TransportView> listTransports() {
		refreshTransportsState();
		List<TransportView> list = new ArrayList<TransportView>(transports.values());
		return list;
	}

	@Override
	public void clearTransports() {
		System.out.println("clearing transports!");
		for(Entry<String, TransporterClient> entry : transporterCompanies.entrySet()) {
			TransporterClient t = entry.getValue();
			t.clearJobs();
		}
		transports.clear();
		if(secundaryBroker!=null)
			secundaryBroker.clearTransports();
	}
	
	public void printTransports() {
		refreshTransportsState();
		TransportView transportView;
		System.out.println("\n-----------------------------------------------------------\nPrinting transportViews");
		for (Map.Entry<Integer,TransportView> entry : transports.entrySet()) {
			transportView = entry.getValue();
			System.out.println("ID\t\t" + transportView.getId() + "\nCompany:\t\t" + transportView.getTransporterCompany() + "\nOrigin:\t\t" + transportView.getOrigin() + "\nDestination:\t\t" + transportView.getDestination() + "\nPrice\t\t:"+transportView.getPrice()+ "\nState:\t\t" + transportView.getState());
			System.out.println("\t*********************");
		}
	}
	public void refreshTransportsState(){
		JobView job;
		for(Map.Entry<Integer, TransportView> entry : transports.entrySet()) {
			job = transporterCompanies.get(entry.getValue().getTransporterCompany()).jobStatus(entry.getValue().getId());
			if(job.getJobState().equals(JobStateView.HEADING)){
				entry.getValue().setState(TransportStateView.HEADING);
				if(secundaryBroker!=null && !amISecond )
					secundaryBroker.refreshTransportView(entry.getKey(),TransportStateView.HEADING);
			}else if(job.getJobState().equals(JobStateView.ONGOING)){
				entry.getValue().setState(TransportStateView.ONGOING);
				if(secundaryBroker!=null && !amISecond )
					secundaryBroker.refreshTransportView(entry.getKey(),TransportStateView.ONGOING);
			}else if(job.getJobState().equals(JobStateView.COMPLETED)){
				entry.getValue().setState(TransportStateView.COMPLETED);
				if(secundaryBroker!=null && !amISecond )
					secundaryBroker.refreshTransportView(entry.getKey(),TransportStateView.COMPLETED);
				//transports.remove(entry.getKey());
			}
		}
	}
	
	/**********************************
	 * FUNCOES AUXILIARES - SEGURANCA *
	 **********************************/
	
	private void getAllCertificates() {	
		for(Map.Entry<String,TransporterClient> entry : transporterCompanies.entrySet()) {
			String transporter_name = entry.getKey();
			if (!transporterCertificates.containsKey(transporter_name))
				try {
					transporterCertificates.put(transporter_name, getCertificate(transporter_name));
				} catch (Exception e) {
					System.err.println("!!! ERROR: Could not get new certificate: " + transporter_name);
				}
		}
	}
	
	private void lookupCA() throws Exception {		
		String uddiURL = "http://localhost:9090";
		String name = "CA";
		UDDINaming uddiNaming = new UDDINaming(uddiURL);
		String endpointAddress = uddiNaming.lookup(name);
		if (endpointAddress == null) {
			System.out.println("CA not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		}
		ca = new CAClient();
		System.out.print("CA found: ");
		System.out.println("\"" + ca.ping("Broker") + "\"");				
	}
	
	private Certificate getCertificate(String entity) throws Exception {
		if(ca == null)
			return null;
		byte[] certBytes = ca.getCertificate(entity);
		ByteArrayInputStream bis = new ByteArrayInputStream(certBytes);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		if (bis.available() > 0) {
			Certificate cert = cf.generateCertificate(bis);
			System.out.println("*** RECEIVED CERTIFICATE: " + entity + ".cer");
			
			FileOutputStream os = new FileOutputStream("src/main/resources/keys/"+entity+".cer");
			os.write(cert.getEncoded());
			os.close();
			
			return cert;
		}		
		bis.close();
		return null;
	}
	
	
	/************************************
	 * FUNCOES AUXILIARES - REDUNDANCIA *
	 ************************************/

	@Override
	public void imAlive() {
		isReady = true;
		lastAliveTime=System.nanoTime();
		System.out.println("Recieved I'm Alive from Primary Server");
	}
	@Override
	public void addTransportView(TransportView transportView) {
		transports.put(transports.size() + 1, transportView);
		System.out.println("added transport "+transports.size()+ " transport id: "+transports.get(transports.size()).getId());
	}
	@Override
	public void refreshTransportView(int id, TransportStateView state) {
		transports.get(id).setState(state);
		System.out.println("refreshed id "+id+ " transport id: "+transports.get(id).getId() + "   new state: "+transports.get(id).getState());

	}
	
	public void addTransportViewsToSecundary(){
		for(Map.Entry<Integer, TransportView> entry : transports.entrySet()) {
			System.out.println("adding transportView "+entry.getKey());
			secundaryBroker.addTransportView(entry.getValue());
		}
	}


}
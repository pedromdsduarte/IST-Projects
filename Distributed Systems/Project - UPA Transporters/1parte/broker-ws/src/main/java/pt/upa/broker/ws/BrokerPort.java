package pt.upa.broker.ws;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;

import javax.jws.WebService;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;


import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.BadJobFault;
import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.cli.TransporterClient;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;
//import pt.upa.transporter.ws.BadJobFault_Exception;
//import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.cli.*;

@WebService(
	    endpointInterface="pt.upa.broker.ws.BrokerPortType",
	    wsdlLocation="broker.1_0.wsdl",
	    name="BrokerWebService",
	    portName="BrokerPort",
	    targetNamespace="http://ws.broker.upa.pt/",
	    serviceName="BrokerService"
)

public class BrokerPort implements BrokerPortType {
	
	private HashMap<Integer,TransportView> transports;
	private HashMap<String,TransporterClient> transporterCompanies;
	private static final long LOOKUP_INTERVAL = 5000;
	private Timer lookupTimer;
	
	private static String[] NORTH_AREA = {"Porto", "Braga" , "Viana do Castelo", "Vila Real", "Bragança"};
	private static String[] CENTER_AREA = {"Lisboa","Leiria","Santarém","Castelo Branco","Coimbra","Aveiro","Viseu","Guarda"};
	private static String[] SOUTH_AREA = {"Setúbal","Évora","Portalegre","Beja","Faro"};
	
	public HashMap<Integer,TransportView> gettransports(){
		return transports;
	}
	public HashMap<String,TransporterClient> gettransporterCompanies(){
		return transporterCompanies;
	}
	public void settransports(HashMap<Integer,TransportView> t){
		transports = t;
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
					lookupTransporterCompanies();
				} catch (Exception e) {
					System.err.println("Failed lookup");
				}
		     }
		};
		
		lookupTimer = new Timer(true);
		
		lookupTimer.schedule(lookup, 0, LOOKUP_INTERVAL);
	}
	
	public void init(String uddiURL, String name, String url) throws Exception {
		
		transports = new HashMap<Integer, TransportView>();
		transporterCompanies = new HashMap<String,TransporterClient>();
		
		UDDINaming uddiNaming = null;
		Endpoint endpoint = null;
		
		try {
			uddiNaming = new UDDINaming(uddiURL);
			uddiNaming.rebind(name, url);
			
			endpoint = Endpoint.create(this);
			System.out.printf("Starting %s%n", url);
			endpoint.publish(url);
			
			startLookupTimer();
			ping("Broker");
			
			System.out.println("Awaiting connections");
			System.out.println("Press enter to shutdown");
			System.in.read();
			
		} catch (Exception e) {
			throw e;
		} finally {
			if (lookupTimer != null) {
				lookupTimer.cancel();
			}
			try {
				if (endpoint != null) {
					clearTransports();
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
			
			
		//	TransporterService service = new TransporterService();
		//	TransporterPortType port = service.getTransporterPort();
			
			/* Se nao existir, cria e guarda */
			if (transporterCompanies.get(transporterName) == null) {
				newTransporters++;
				TransporterClient client = new TransporterClient(uddiURL, transporterName);
				client.init();
				copy.put(transporterName, client);
				System.out.println("\n-----------------------------------------------------------\n"+transporterName + " joined");
			}
			/* Senao, copia */
			else {
				copy.put(transporterName, transporterCompanies.get(transporterName));
				continue;
			}

		//	BindingProvider bindingProvider = (BindingProvider) port;
		//	Map<String, Object> requestContext = bindingProvider.getRequestContext();
		//	requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpoint);

		}
		
		/* Indica quais e que abandonaram */
		for (Map.Entry<String,TransporterClient> entry : transporterCompanies.entrySet()) {
			String id = entry.getKey();
			if (!copy.containsKey(id)){
				System.out.println("\n-----------------------------------------------------------\n"+id + " left");
				//Remove tranportViews que correspondem a transportadora que abandonou
	/*			for(Map.Entry<Integer, TransportView> transport : transports.entrySet()){
					TransportView t = transport.getValue();
					System.out.println("transporter id "+ id + "\ttransporter company "+t.getTransporterCompany());
					if(id.equals(transport.getValue().getTransporterCompany() )){
						System.out.println("removed id: "+id+" \t from company: "+t.getTransporterCompany() );
					//	transports.remove(transport.getKey());
					}
				}*/
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
	

	@Override
	public String requestTransport(String origin, String destination, int price) throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		TransporterClient company;
		JobView job = null;
		Integer bestPriceFound = 999;
		String transportViewInfo = "";
		TransportView bestTranportView = null;
		TransportView transportView = null;
		HashMap<Integer,TransportView> transportViews = new HashMap<Integer, TransportView>();
		System.out.println("\n-----------------------------------------------------------\nReceived requestTransport");
		//Check origin location
		if(!Arrays.asList(SOUTH_AREA).contains(origin) && !Arrays.asList(CENTER_AREA).contains(origin) && !Arrays.asList(NORTH_AREA).contains(origin)){
			UnknownLocationFault fault = new UnknownLocationFault();
			fault.setLocation(origin);
			throw new UnknownLocationFault_Exception("Thats not a valid location!", fault);
		}
		//Check destination location
		if(!Arrays.asList(SOUTH_AREA).contains(destination) && !Arrays.asList(CENTER_AREA).contains(destination) && !Arrays.asList(NORTH_AREA).contains(destination)){
			UnknownLocationFault fault = new UnknownLocationFault();
			fault.setLocation(destination);
			throw new UnknownLocationFault_Exception("Thats not a valid location!", fault);
		}
		if(price < 0){
			InvalidPriceFault fault = new InvalidPriceFault();
			fault.setPrice(price);
			throw new InvalidPriceFault_Exception("Thats not a valid price!", fault);
		}
		
		
		
		
		for (Map.Entry<String,TransporterClient> entry : transporterCompanies.entrySet()) {
			company = entry.getValue();
			//System.out.println("\n\tWatching company: " + entry.getKey()+"\n");
			transportView = new TransportView();
			transportView.setOrigin(origin);
			transportView.setDestination(destination);
			transportView.setState(TransportStateView.REQUESTED);
			transportView.setTransporterCompany(company.getCompanyName());
			transports.put(transports.size() + 1, transportView);
			transportViews.put(transportViews.size() +1 , transportView);
			try{
				job = company.requestJob(origin, destination, price);
				if(job == null){
					transportView.setState(TransportStateView.FAILED);
					transports.get(transports.size()).setState(TransportStateView.FAILED);
					transportViews.remove(transports.size());
					continue;
				}else{
					transportView.setPrice(job.getJobPrice());
					transportView.setId(job.getJobIdentifier());
					transportView.setTransporterCompany(job.getCompanyName());
					transportView.setState(TransportStateView.BUDGETED);
				}
			}catch (BadPriceFault_Exception e){
				transportView.setState(TransportStateView.REQUESTED);
				InvalidPriceFault fault = new InvalidPriceFault();
				fault.setPrice(e.getFaultInfo().getPrice());
				throw new InvalidPriceFault_Exception("Thats not a valid price!", fault);
			}catch (BadLocationFault_Exception e){
				transportView.setState(TransportStateView.REQUESTED);
				UnknownLocationFault fault = new UnknownLocationFault();
				fault.setLocation(e.getFaultInfo().getLocation());
				throw new UnknownLocationFault_Exception("The server didnt found "+ e.getFaultInfo().getLocation(), fault);
			}
		}
		for (Map.Entry<Integer,TransportView> entry : transportViews.entrySet()) {
			transportView = entry.getValue();	
			if(transportView.getPrice()!=null){
				if(transportView.getPrice() <= price  && bestPriceFound > transportView.getPrice() && transportView.getState() != TransportStateView.FAILED){
					bestPriceFound = transportView.getPrice();
					bestTranportView = transportView;
				}
				//primeiro caso
				if(bestPriceFound == 999 || bestPriceFound > transportView.getPrice()){
					bestPriceFound = transportView.getPrice();
					bestTranportView = transportView;
				}else{
					try {
						transporterCompanies.get(transportView.getTransporterCompany()).decideJob(transportView.getId(), false);
						transportView.setState(TransportStateView.FAILED);
					} catch (BadJobFault_Exception e) {
						transportView.setState(TransportStateView.FAILED);
					}
					
				}
			}
			
			/*if(job.getJobPrice() <= price &&  bestPriceFound > job.getJobPrice()){
				bestPriceFound = job.getJobPrice();
				jobInfo = "ID\t\t" + job.getJobIdentifier() + "\nCompany:\t\t" + job.getCompanyName() + "\nOrigin:\t\t" + job.getJobOrigin() + "\nDestination:\t\t" + job.getJobDestination() + "\nPrice\t\t:"+ job.getJobPrice() + "\nState:\t\t" + job.getJobState();	
			}
			if(bestPriceFound == 999 || bestPriceFound > job.getJobPrice()){
				bestPriceFound = job.getJobPrice();*/	
		}
		for (Map.Entry<Integer,TransportView> entry : transportViews.entrySet()) {
			transportView = entry.getValue();			
			if(transportView == bestTranportView && bestPriceFound <= price){
				try{
					transporterCompanies.get(transportView.getTransporterCompany()).decideJob(transportView.getId(), true);
					transportView.setState(TransportStateView.BUDGETED);
					transportViewInfo = "ID\t\t" + transportView.getId() + "\nCompany:\t\t" + transportView.getTransporterCompany() + "\nOrigin:\t\t" + transportView.getOrigin() + "\nDestination:\t\t" + transportView.getDestination() + "\nPrice\t\t:"+transportView.getPrice()+ "\nState:\t\t" + transportView.getState();	
				}catch(BadJobFault_Exception e){
					transportView.setState(TransportStateView.FAILED);
				}
			}else{
				transportView.setState(TransportStateView.FAILED);
			}
			System.out.println("\n*********************TransportView Final************************\n");
			System.out.println("ID\t\t" + transportView.getId() + "\nCompany:\t\t" + transportView.getTransporterCompany() + "\nOrigin:\t\t" + transportView.getOrigin() + "\nDestination:\t\t" + transportView.getDestination() + "\nPrice\t\t:"+transportView.getPrice()+ "\nState:\t\t" + transportView.getState());
		}

		if(transportViewInfo == ""){
			UnavailableTransportFault fault = new UnavailableTransportFault();
			fault.setDestination(destination);
			fault.setOrigin(origin);
			throw new UnavailableTransportFault_Exception("The companies didnt send any porposes for you (or maybe there are no routes with that combination origin/destination)",fault);
		}
		if(bestPriceFound > price){
			UnavailableTransportPriceFault fault = new UnavailableTransportPriceFault();
			fault.setBestPriceFound(bestPriceFound);
			throw new UnavailableTransportPriceFault_Exception("Ops! Sorry but we didn't find a price lesser than " + price + " :(", fault);
		}
		transportViews.clear();
		System.out.println("\n-----------------------------------------------------------\n\toferta aceite:\n" + transportViewInfo);
		return transportViewInfo;
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		//TODO: Panda
		
		String id_iterator = null;
		TransportView transport;
		for (Map.Entry<Integer,TransportView> entry : transports.entrySet()) {
			transport = entry.getValue();
			id_iterator = transport.getId();
			if (id.equals(id_iterator))
				return transport;
		}
		
		throw new UnknownTransportFault_Exception("Transport "+id+ " not found.", new UnknownTransportFault());
	}

	@Override
	public List<TransportView> listTransports() {
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
	}
	
	public void printTransports() {
		TransportView transportView;
		System.out.println("\n-----------------------------------------------------------\nPrinting transportViews");
		for (Map.Entry<Integer,TransportView> entry : transports.entrySet()) {
			transportView = entry.getValue();
			System.out.println("ID\t\t" + transportView.getId() + "\nCompany:\t\t" + transportView.getTransporterCompany() + "\nOrigin:\t\t" + transportView.getOrigin() + "\nDestination:\t\t" + transportView.getDestination() + "\nPrice\t\t:"+transportView.getPrice()+ "\nState:\t\t" + transportView.getState());
			System.out.println("\t*********************");
		}
	}

}

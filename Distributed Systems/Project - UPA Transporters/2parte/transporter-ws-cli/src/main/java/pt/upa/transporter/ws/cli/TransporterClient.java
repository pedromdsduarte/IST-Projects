package pt.upa.transporter.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.*;
import pt.upa.ws.handlers.DigitalSignatureHandler;


public class TransporterClient implements TransporterPortType {
	private String uddiURL;
	private String name;
	private TransporterPortType port;

	private String _broker;
	
	public TransporterClient(String _uddiURL, String _name){
		uddiURL = _uddiURL;
		name = _name;
	}
	
	public TransporterClient(String _name){
		name = _name;
	}
	
	public void init(String broker)  throws Exception {
		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		UDDINaming uddiNaming = new UDDINaming(uddiURL);

		//System.out.printf("Looking for '%s'%n", name);
		String endpointAddress = uddiNaming.lookup(name);

		if (endpointAddress == null) {
			System.out.println("Not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		}

		//System.out.println("Creating stub ...");
		TransporterService service = new TransporterService();
		port = service.getTransporterPort();

		//System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		
	
		setBroker(broker);
		prepareContext();
	}

	@Override
	public String ping(String name) {
		prepareContext();
		return port.ping(name);
	}
	@Override
	public JobView requestJob(String origin, String destination, int price)	throws BadLocationFault_Exception, BadPriceFault_Exception {
		prepareContext();
		return port.requestJob(origin, destination, price);
	}
	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		prepareContext();
		return port.decideJob(id, accept);
	}
	@Override
	public JobView jobStatus(String id) {
		prepareContext();
		return port.jobStatus(id);
	}
	@Override
	public List<JobView> listJobs() {
		prepareContext();
		return port.listJobs();
	}
	@Override
	public void clearJobs() {
		prepareContext();
		port.clearJobs();
	}
	public TransporterPortType getPort(){
		return port;
	}
	public String getCompanyName(){
		return name;
	}

	public String getBroker() {
		return _broker;
	}
	
	public void setBroker(String broker) {
		_broker = broker;
	}
	
	private void prepareContext() {
		String sender = getBroker();
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		System.out.printf("%s put token '%s' on request context%n", this.getClass().getSimpleName(), sender);
		requestContext.put(DigitalSignatureHandler.SENDER_PROPERTY, sender);
	}
}

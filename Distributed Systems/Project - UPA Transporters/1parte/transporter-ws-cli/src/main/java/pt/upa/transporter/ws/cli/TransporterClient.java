package pt.upa.transporter.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.*;






public class TransporterClient implements TransporterPortType {
	private String uddiURL;
	private String name;
	private TransporterPortType port;
	public TransporterClient(String _uddiURL, String _name){
		uddiURL = _uddiURL;
		name = _name;
	}
	
	public TransporterClient(String _name){
		name = _name;
	}
	
	public void init()  throws Exception {
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

		
	}

	@Override
	public String ping(String name) {
		return port.ping(name);
	}
	@Override
	public JobView requestJob(String origin, String destination, int price)	throws BadLocationFault_Exception, BadPriceFault_Exception {
		return port.requestJob(origin, destination, price);
	}
	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		return port.decideJob(id, accept);
	}
	@Override
	public JobView jobStatus(String id) {
		return port.jobStatus(id);
	}
	@Override
	public List<JobView> listJobs() {
		return port.listJobs();
	}
	@Override
	public void clearJobs() {
		port.clearJobs();
	}
	public TransporterPortType getPort(){
		return port;
	}
	public String getCompanyName(){
		return name;
	}
}

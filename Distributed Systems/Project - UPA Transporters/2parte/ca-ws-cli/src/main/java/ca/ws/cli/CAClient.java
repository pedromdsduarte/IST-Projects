package ca.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import javax.xml.ws.BindingProvider;

// classes generated from WSDL
import ca.ws.*;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

public class CAClient implements CA {

	private CA port;
	private final String uddiURL = "http://localhost:8079/ca-ws/endpoint";
	private final String name = "CA";
	
	public CAClient() throws Exception {

		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		UDDINaming uddiNaming = new UDDINaming(uddiURL);

		System.out.printf("Looking for '%s'%n", name);
		String endpointAddress = uddiNaming.lookup(name);

		if (endpointAddress == null) {
			System.out.println("Not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		}
	

		System.out.println("Creating stub ...");
		CAImplService service = new CAImplService();
		port = service.getCAImplPort();

		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

	}

	@Override
	public String ping(String name) {
		return port.ping(name);
	}

	@Override
	public byte[] getCertificate(String entity) {
		return port.getCertificate(entity);
	}



	
}

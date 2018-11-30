package pt.upa.broker.ws.cli;

import java.util.Map;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;

public class BrokerClient {
	
	// TODO
	
	public void init(String uddiURL, String name) throws Exception {
		
		UDDINaming uddiNaming = null;
		String endpointAddress = null;
		
		try {
		
			System.out.printf("Contacting UDDI at %s%n", uddiURL);
			uddiNaming = new UDDINaming(uddiURL);
	
			System.out.printf("Looking for '%s'%n", name);
			endpointAddress = uddiNaming.lookup(name);
	
			if (endpointAddress == null) {
				System.out.println("Not found!");
				return;
			} else {
				System.out.printf("Found %s%n", endpointAddress);
			}
	
			System.out.println("Creating stub ...");
			BrokerService service = new BrokerService();
			BrokerPortType port = service.getBrokerPort();
	
			System.out.println("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
			
			///////////////////////////////////////////////////////////////////////////////////////////////
			//                                   ZONA DE COMANDOS                                        //
			///////////////////////////////////////////////////////////////////////////////////////////////
			// 																							 //
			// 		port.requestTransport(String Origem, String Destino, int Preco						 //
			// 																							 //
			// 		port.viewTransport(String id)														 //
			// 																							 //
			// 		port.clearTransports()																 //
			// 																							 //
			// 		port.printTransports()																 //
			// 																							 //
			// 																							 //
			///////////////////////////////////////////////////////////////////////////////////////////////
			
			String result = port.requestTransport("Lisboa", "Porto", 42);
			System.out.println(result);
			
		} catch (Exception e) {
			throw e;
		}
		
	}

}

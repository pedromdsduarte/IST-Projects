package pt.upa.broker.ws.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;
import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

public class BrokerClient {
	private BrokerPortType port = null;

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
			port = service.getBrokerPort();
			
			System.out.println("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
			
			int connectionTimeout = 3000;
            // The connection timeout property has different names in different versions of JAX-WS
            // Set them all to avoid compatibility issues
            final List<String> CONN_TIME_PROPS = new ArrayList<String>();
            CONN_TIME_PROPS.add("com.sun.xml.ws.connect.timeout");
            CONN_TIME_PROPS.add("com.sun.xml.internal.ws.connect.timeout");
            CONN_TIME_PROPS.add("javax.xml.ws.client.connectionTimeout");
            // Set timeout until a connection is established (unit is milliseconds; 0 means infinite)
            for (String propName : CONN_TIME_PROPS)
                requestContext.put(propName, connectionTimeout);
            System.out.printf("Set connection timeout to %d milliseconds%n", connectionTimeout);

            int receiveTimeout = 3000;
            // The receive timeout property has alternative names
            // Again, set them all to avoid compability issues
            final List<String> RECV_TIME_PROPS = new ArrayList<String>();
            RECV_TIME_PROPS.add("com.sun.xml.ws.request.timeout");
            RECV_TIME_PROPS.add("com.sun.xml.internal.ws.request.timeout");
            RECV_TIME_PROPS.add("javax.xml.ws.client.receiveTimeout");
            // Set timeout until the response is received (unit is milliseconds; 0 means infinite)
            for (String propName : RECV_TIME_PROPS)
                requestContext.put(propName, 1000);
            System.out.printf("Set receive timeout to %d milliseconds%n", receiveTimeout);

			
			
			
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
			
			//sport.clearTransports();// To start fresh
			//String j1 = port.requestTransport("Porto", "Lisboa", 69);
			//String j2 = port.requestTransport("Beja", "Lisboa", 10);
			//String j3 = port.requestTransport("Lisboa", "Coimbra", 10);
			//TransportView jtv1 = port.viewTransport(j1);
			//TransportView jtv2 = port.viewTransport(j2);
			//port.viewTransport(j1);

			System.out.println("[PING] " + port.ping("Broker Client"));
			//String rt = port.requestTransport("Lisboa", "Beja", 10);

			//System.out.println("sleeping 15 seconds");
			//Thread.sleep(15000);

			//TransportView vt = port.viewTransport(rt);
			//System.out.println("Estado: "+vt.getState());
			
		} catch (Exception e) {
			throw e;
		}
		
	}

	public String transportPrettyPrint(TransportView tv){
		return ("Transport ID:" + tv.getId() + "\nOrigin: " + tv.getOrigin() + "\nDestination: " + tv.getDestination() + 
				"\nPrice: " + tv.getPrice() + "\nState: " + tv.getState());
	}
	
	public void listPrettyPrint( List<TransportView> list){
		int i=1;
		System.out.println("YOUR TRANSPORT'S LIST");
		System.out.println("ID\tORIGIN\tDESTINATION\tPRICE\tSTATE");
		for(TransportView tv : list){
			if (tv.getDestination().length()<=6)
				System.out.println("" + i + "\t" + tv.getOrigin() + "\t" + tv.getDestination() + "\t\t" + tv.getPrice() + "\t" + tv.getState());
			else
				System.out.println("" + i + "\t" + tv.getOrigin() + "\t" + tv.getDestination() + "\t" + tv.getPrice() + "\t" + tv.getState());
			i++;
		}
	}
	
	public void presentation() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String str = null;
		String[] arg = null;
		System.out.println("PLEASE INSERT A COMMAND TO CONTINUE");
		System.out.println("Request Job   - USAGE: request <origin> <destination> <price>");
		System.out.println("Get Job State - USAGE: state <id>");
		System.out.println("List Jobs     - USAGE: list");
		System.out.println("Exit program  - USAGE: exit");
		System.out.print(">");
		try {
			while((str = in.readLine()) != null){
				arg= str.split(" ");
			    if(arg[0].equals("exit") && arg.length == 1) return;
			    else if(arg[0].equals("request")){
			    	if(arg.length != 4) System.out.println("ERROR: Request command takes 3 arguments");
			    	try {
			    		System.out.println("\n««««««««««««««««««««««");
			    		System.out.println("SUCCESS REQUESTING TRANSPORT\nYour transport ID: " + port.requestTransport(arg[1], arg[2], Integer.parseInt(arg[3])));
			    		System.out.println("««««««««««««««««««««««\n");
					} catch (InvalidPriceFault_Exception | UnavailableTransportFault_Exception
							| UnavailableTransportPriceFault_Exception | UnknownLocationFault_Exception e) {
						System.out.println("ERROR: " + e.getMessage() + "\n");
					} catch(NumberFormatException e) {
						System.out.println("ERROR: The <price> parameter must be an integer\n");
					}
			    	
			    }
			    else if(arg[0].equals("state")){
			    	if(arg.length != 2) System.out.println("\nERROR: State command takes 1 arguments\n");
			    	try {
			    		System.out.println("\n««««««««««««««««««««««");
						TransportView tv = port.viewTransport(arg[1]);
						System.out.println(this.transportPrettyPrint(tv));
						System.out.println("\nYOUR TRANSPORT'S DETAILS");
			    		System.out.println("««««««««««««««««««««««\n");
					} catch (UnknownTransportFault_Exception e) {
						System.out.println("ERROR:" + e.getMessage() + "\n");
					}
			    	
			    }
			    else if(arg[0].equals("list")){
			    	if(arg.length != 1) System.out.println("\n\nERROR: List command takes 0 arguments\n\n");
		    		System.out.println("\n««««««««««««««««««««««");
			    	listPrettyPrint(port.listTransports());
		    		System.out.println("««««««««««««««««««««««\n");			    
			    }
			    else{
		    		System.out.println("\n««««««««««««««««««««««");
			    	System.out.println("\nERROR: Unknown command " + arg[0] + ".\n\n");
		    		System.out.println("««««««««««««««««««««««\n");

			    }
				System.out.println("PLEASE INSERT A COMMAND TO CONTINUE");
				System.out.println("Request Job   - USAGE: request <origin> <destination> <price>");
				System.out.println("Get Job State - USAGE: state <id>");
				System.out.println("List Jobs     - USAGE: list");
				System.out.println("Exit program  - USAGE: exit");
				System.out.print(">");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

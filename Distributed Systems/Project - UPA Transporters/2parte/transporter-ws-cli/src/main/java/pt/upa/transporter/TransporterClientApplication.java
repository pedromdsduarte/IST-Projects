package pt.upa.transporter;

import java.util.Map;
import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import javax.xml.ws.BindingProvider;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.*;

public class TransporterClientApplication {

	public static void main(String[] args) throws Exception {
		// Check arguments
				if (args.length < 2) {
					System.err.println("Argument(s) missing!");
					System.err.printf("Usage: java %s uddiURL name%n", TransporterClientApplication.class.getName());
					return;
				}

				String uddiURL = args[0];
				String name = args[1];

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
				TransporterService service = new TransporterService();
				TransporterPortType port = service.getTransporterPort();

				System.out.println("Setting endpoint address ...");
				BindingProvider bindingProvider = (BindingProvider) port;
				Map<String, Object> requestContext = bindingProvider.getRequestContext();
				requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

				/*try {
					String result = port.ping("friend");
					System.out.println(result);
					JobView job = port.requestJob("Mars", "Évora", 1);
				} catch (BadPriceFault_Exception e){
					System.err.println("Dude you need to spend some $$ if you want to get a ride. (50cent maybe?)");
				} catch (BadLocationFault_Exception e){
					System.err.println("Did you want to go to Mars or something!? Give me a valid location!");
				}
				catch (Exception e) {
					System.err.println("Caught: " + e);
				}*/
				
				String result = port.ping("test handler");
				System.out.println(result);
	}
}

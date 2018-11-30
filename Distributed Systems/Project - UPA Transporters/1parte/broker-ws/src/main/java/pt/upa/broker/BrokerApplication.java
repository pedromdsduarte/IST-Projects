package pt.upa.broker;

import javax.xml.ws.Endpoint;

import pt.upa.broker.ws.BrokerPort;

public class BrokerApplication {

	public static void main(String[] args) throws Exception {
		System.out.println(BrokerApplication.class.getSimpleName() + " starting...");

		if (args.length < 3) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL wsName wsURL%n", BrokerApplication.class.getName());
			return;
		}
		

		String uddiURL = args[0];
		String name = args[1];
		String url = args[2];

		
		try {
			BrokerPort port = new BrokerPort();
			port.init(uddiURL,name,url);

		} catch (Exception e) {
			System.out.printf("Caught exception: %s%n", e);
			e.printStackTrace();

		} 
	}

}

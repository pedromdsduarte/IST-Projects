package pt.upa.transporter;

import javax.xml.ws.Endpoint;

import pt.upa.transporter.ws.TransporterPort;

public class TransporterApplication {

	public static void main(String[] args) throws Exception{
		// Check arguments
		if (args.length < 3) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL wsName wsURL%n", TransporterApplication.class.getName());
			return;
		}

		String uddiURL = args[0];
		String name = args[1];
		String url = args[2];
		
		try {
			TransporterPort port = new TransporterPort();
			
			port.init(uddiURL, name, url);
			
		} catch (Exception e) {
			System.out.printf("Caught exception: %s%n", e);
			e.printStackTrace();

		}

	}

	
}

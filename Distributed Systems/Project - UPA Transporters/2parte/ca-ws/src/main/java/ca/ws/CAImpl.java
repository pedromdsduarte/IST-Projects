package ca.ws;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.jws.WebService;

@WebService(endpointInterface = "ca.ws.CA")
public class CAImpl implements CA {
	
	final String PATH_TO_KEYS = "src/main/resources";

	public String ping(String name) {
		return "Hello " + name + ", I am the CA!";
	}
	
	public byte[] getCertificate(String entity) {
		
		System.out.println("*** RECEIVED CERTIFICATE REQUEST FOR: " + entity);
		
		Path path = Paths.get(PATH_TO_KEYS, entity+".cer");
		byte[] data = null;
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			System.err.println("!!! ERROR: Requested certificate was not found: " + entity + ".cer");
		}
		return data;
	}

}

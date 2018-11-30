package ca.ws;

import javax.jws.WebService;

@WebService
public interface CA {

	public String ping(String name);
	
	public byte[] getCertificate(String entity);

}

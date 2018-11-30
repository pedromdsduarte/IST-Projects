package pt.upa.ws.handlers;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;


public class DigitalSignatureHandler implements SOAPHandler<SOAPMessageContext> {
	
	private final String KEYSTORE_PASSWORD = "ins3cur3";
	private final String KEY_PASSWORD = "1nsecure";
	private final String BASE_PATH = "src/main/resources/keys/";
	
	public static final String SENDER_PROPERTY = "my.sender.property";
	public static final String SENDER_HEADER = "senderHeader";
	public static final String SENDER_NS = "urn:sender";	
	private String sender;
	
	public static final String SIGNATURE_HEADER = "signatureHeader";
	public static final String SIGNATURE_NS = "urn:signature";
	
	public static final String NONCE_HEADER = "nonceHeader";
	public static final String NONCE_NS = "urn:nonce";
	
    public static final String CONTEXT_PROPERTY = "my.property";
    
    private final boolean ATTACK_SIMULATION = false;

	
	private ArrayList<String> nonces = new ArrayList<String>();
	
	private SecureRandom random = new SecureRandom();

	public String nextSessionId() {
		return new BigInteger(130, random).toString(32);
	}
	

	@Override
	public Set<QName> getHeaders() {
		return null;
	}
	
	@Override
	public boolean handleMessage(SOAPMessageContext smc) throws RuntimeException {
        Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound) {
           handleOutboundMessage(smc);
        } else {
        	handleInboundMessage(smc);
        }
        return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}
	
	@Override
	public void close(MessageContext context) {
	}
	
	private void handleInboundMessage(SOAPMessageContext smc) throws RuntimeException {
		try {
			// get SOAP envelope header
			SOAPMessage msg = smc.getMessage();
			SOAPPart sp = msg.getSOAPPart();
			SOAPEnvelope se = sp.getEnvelope();
			SOAPHeader sh = se.getHeader();
			
		    String plainBody = getSOAPBodyContent(msg);
			byte[] bodyBytes = plainBody.getBytes();

			// check header
			if (sh == null) {
				System.out.println("Header not found.");
				return;
			}
			
			/************************************************
			 *                    HEADER                    *
			 ************************************************/
			/**************************
			 * Get sender from header *
			 **************************/
			
			Name sender_name = se.createName(SENDER_HEADER, "e", SENDER_NS);
			Iterator it = sh.getChildElements(sender_name);
			if (!it.hasNext()) {
				System.out.printf("Header element %s not found.%n", SENDER_HEADER);
				return;
			}
			SOAPElement sender_element = (SOAPElement) it.next();
			sender = sender_element.getValue();
			System.out.println("[HANDLER-Inbound] Received from " + sender);

			/*****************************
			 * Get signature from header *
			 *****************************/
			
			Name sig_name = se.createName(SIGNATURE_HEADER, "e", SIGNATURE_NS);
			it = sh.getChildElements(sig_name);
			if (!it.hasNext()) {
				System.out.printf("Header element %s not found.%n", SIGNATURE_HEADER);
				return;
			}
			SOAPElement sigElement = (SOAPElement) it.next();
			String sigText = sigElement.getValue();
			byte[] digitalSignature =  DatatypeConverter.parseBase64Binary(sigText);
			//System.out.println("[HANDLER-Inbound] Signature received: " + printHexBinary(digitalSignature));
			
			/*****************************
			 *   Get nonce from header   *
			 *****************************/
			
			Name nonce_name = se.createName(NONCE_HEADER, "e", NONCE_NS);
			it = sh.getChildElements(nonce_name);
			if (!it.hasNext()) {
				System.out.printf("Header element %s not found.%n", NONCE_HEADER);
				return;
			}
			SOAPElement nonceElement = (SOAPElement) it.next();
			String nonceText = nonceElement.getValue();
			if (!nonces.contains(nonceText)) {
				nonces.add(nonceText);
				System.out.println("[HANDLER-Inbound] Nonce received: " + nonceText);
			} else {
				System.out.println("[HANDLER-Inbound] WARNING: Nonce was already received before!");
			}
			
			/************************************************
			 *                   SIGNATURE                  *
			 ************************************************/
			
			// data modification ...
			if (ATTACK_SIMULATION) {
				digitalSignature[3] = 12;
				System.out.println("Tampered bytes: (look closely around the 7th hex character)");
				System.out.println(printHexBinary(digitalSignature));
			}
			
			
			PublicKey key = getPublicKeyFromCertificate(readCertificateFile("../broker-ws/src/main/resources/keys/"+sender+".cer"));
			boolean verified = verifyDigitalSignature(digitalSignature, bodyBytes, key);
			if(verified)
				System.out.println("[HANDLER-Inbound] Signature received is valid");
			else
				System.out.println("[HANDLER-Inbound] WARNING: Signature received is not valid!");
			
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	private void handleOutboundMessage(SOAPMessageContext smc) throws RuntimeException {
		
		sender = (String) smc.get(SENDER_PROPERTY);
		
		try {
			//get SOAP envelope
			SOAPMessage msg = smc.getMessage();
	        SOAPPart sp = msg.getSOAPPart();
	        SOAPEnvelope se;
			
	        // add header	        
			se = sp.getEnvelope();
		    SOAPHeader sh = se.getHeader();
		    if (sh == null)
		    	sh = se.addHeader();
		    
		    /***********************************************************/
		     
		    String plainText = getSOAPBodyContent(msg);
			byte[] plainBytes = plainText.getBytes();
									
			/*********************
			 * COMPUTE SIGNATURE *
			 *********************/

			byte[] digitalSignature = makeDigitalSignature(plainBytes, getPrivateKeyFromKeystore(getKeystoreFile(),
					KEYSTORE_PASSWORD.toCharArray(), getAlias(), KEY_PASSWORD.toCharArray()));

			/************************************************
			 *                    HEADER                    *
			 ************************************************/

			/************************
			 * Add sender to header *
			 ************************/
			
			Name sender_name = se.createName(SENDER_HEADER, "e", SENDER_NS); 
			SOAPHeaderElement senderElement = sh.addHeaderElement(sender_name);
			senderElement.addTextNode(sender);
			
			/***************************
			 * Add signature to header *
			 ***************************/
			
		    Name sig_name = se.createName(SIGNATURE_HEADER, "e", SIGNATURE_NS);
			SOAPHeaderElement sigElement = sh.addHeaderElement(sig_name);
			
		    String sigText = DatatypeConverter.printBase64Binary(digitalSignature);
			sigElement.addTextNode(sigText);  	
			
			//System.out.println("[HANDLER-Outbound] Signature sent: " + printHexBinary(digitalSignature));
			
			/***************************
			 *   Add nonce to header   *
			 ***************************/
			
			Name nonce_name = se.createName(NONCE_HEADER, "e", NONCE_NS);
			SOAPHeaderElement nonceElement = sh.addHeaderElement(nonce_name);
			String nonce = nextSessionId();
			nonceElement.addTextNode(nonce);
			System.out.println("[HANDLER-Outbound] Nonce sent: " + nonce);
		    
		} catch (Exception e) {
			throw new RuntimeException();
		}
        

	}



	/**********************
	 * FUNCOES AUXILIARES *
	 **********************/

	private String getKeystoreFile() {
		return BASE_PATH + sender + ".jks";
	}
	
	private String getAlias() {
		return sender;
	}
	
    public String getSOAPBodyContent(SOAPMessage sm) throws SOAPException, TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError{
    	
		SOAPBody body = sm.getSOAPBody();
	
		//getting body string
        DOMSource source = new DOMSource(body);
        StringWriter stringResult = new StringWriter();
        TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
        String bodyString = stringResult.toString();
		return bodyString;
	}

	/** auxiliary method to calculate digest from text and cipher it */
	public static byte[] makeDigitalSignature(byte[] bytes, PrivateKey privateKey) throws Exception {
		// get a signature object using the SHA-1 and RSA combo
		// and sign the plain-text with the private key
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initSign(privateKey);
		sig.update(bytes);
		byte[] signature = sig.sign();
		
		return signature;
	}
	
	/**
	 * Reads a PrivateKey from a key-store
	 * 
	 * @return The PrivateKey
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKeyFromKeystore(String keyStoreFilePath, char[] keyStorePassword,
			String keyAlias, char[] keyPassword) throws Exception {

		KeyStore keystore = readKeystoreFile(keyStoreFilePath, keyStorePassword);
		PrivateKey key = (PrivateKey) keystore.getKey(keyAlias, keyPassword);

		return key;
	}

	/**
	 * Reads a KeyStore from a file
	 * 
	 * @return The read KeyStore
	 * @throws Exception
	 */
	public static KeyStore readKeystoreFile(String keyStoreFilePath, char[] keyStorePassword) throws Exception {
		FileInputStream fis;
		try {
			fis = new FileInputStream(keyStoreFilePath);
		} catch (FileNotFoundException e) {
			System.err.println("Keystore file <" + keyStoreFilePath + "> not found.");
			return null;
		}
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(fis, keyStorePassword);
		return keystore;
	}
	
	/**
	 * Returns the public key from a certificate
	 * 
	 * @param certificate
	 * @return
	 */
	public static PublicKey getPublicKeyFromCertificate(Certificate certificate) {
		return certificate.getPublicKey();
	}

	/**
	 * Reads a certificate from a file
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Certificate readCertificateFile(String certificateFilePath) throws Exception {
		FileInputStream fis;

		try {
			fis = new FileInputStream(certificateFilePath);
		} catch (FileNotFoundException e) {
			System.err.println("Certificate file <" + certificateFilePath + "> not found.");
			return null;
		}
		BufferedInputStream bis = new BufferedInputStream(fis);

		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		if (bis.available() > 0) {
			Certificate cert = cf.generateCertificate(bis);
			return cert;
			// It is possible to print the content of the certificate file:
			// System.out.println(cert.toString());
		}
		bis.close();
		fis.close();
		return null;
	}

	
	/**
	 * auxiliary method to calculate new digest from text and compare it to the
	 * to deciphered digest
	 */
	public static boolean verifyDigitalSignature(byte[] cipherDigest, byte[] bytes, PublicKey publicKey)
			throws Exception {

		// verify the signature with the public key
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initVerify(publicKey);
		sig.update(bytes);
		try {
			return sig.verify(cipherDigest);
		} catch (SignatureException se) {
			System.err.println("Caught exception while verifying signature " + se);
			return false;
		}
	}
}

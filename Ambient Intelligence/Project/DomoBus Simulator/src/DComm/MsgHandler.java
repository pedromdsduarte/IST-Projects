package DComm;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import App.DomoBusBE;

public class MsgHandler {
	
	

	public int processGet(int deviceAddr, int propDesc, byte[] value_a) {
		//Default handler for get
		System.out.println("I am the handler for GET: d=" +
			deviceAddr + " p=" + propDesc + " space=" +
			value_a.length);
		
		DomoBusBE dmb = DCommAPI.init("").getDmb();
		
		return dmb.handleGet(deviceAddr, propDesc, value_a);

	}
	
	public int processSet(int deviceAddr, int propDesc, byte[] value_a) {
		//Default handler for set
		System.out.println("I am the handler for SET: d=" +
			deviceAddr + " p=" + propDesc + " value_len=" +
			value_a.length + "value=" + value_a);
		DomoBusBE dmb = DCommAPI.init("").getDmb();
		
		
		
		int property_type = propDesc & dmb.DS_PROPERTY_TYPE_MASK;
		
		String params = deviceAddr + " " + propDesc + " ";
		if (property_type == dmb.DS_VALUE_TYPE_ARRAY) {
			String value = new String(value_a, StandardCharsets.UTF_8).trim();
			params += "\"" + value + "\"";
		}
		
		else {
			System.out.println("Received array " + dmb.byteToHexString(value_a));
			int value = ByteBuffer.wrap(value_a).order(ByteOrder.LITTLE_ENDIAN).getInt();
			System.out.println("Setting value " + value);
			params += value;
		}
		
		
		
		int status =  dmb.handleSet(params);
		
		return status;
		//return dmb.processSet(deviceAddr, propDesc, value_a);
		
		
	}
	
	public int processExec() {
		//Default handler for exec
		System.out.println("I am the default handler for EXEC");
		
		return 0; /* ok */
	}
	
	public int processNotify(int deviceAddr, int propDesc, byte[] value_a) {
		//Default handler for notify
		System.out.println("I am the handler for NOTIFY: d=" +
			deviceAddr + " p=" + propDesc + " value_len=" +
			value_a.length + "value=" + value_a);
		return 0; /* ok */
	}
	
	public void processError() {
		//Default handler for error
		System.out.println("I am the default handler for ERROR");
		
	}
	
	public void processAnswerGet() {
		//Default handler for answer to get
		System.out.println("I am the default handler for ANSWER TO GET");
		
	}
	
}

package DComm;

public class MsgHandler {

	public int processGet(int deviceAddr, int propDesc, byte[] value_a) {
		//Default handler for get
		System.out.println("I am the handler for GET: d=" +
			deviceAddr + " p=" + propDesc + " space=" +
			value_a.length);
//teste...
		value_a[0] = 0; /* 0=valid   else -> invalid value */
		value_a[1] = 3;
		value_a[2] = 0;
		value_a[3] = 0;
		value_a[4] = 0;
		return 0; /* ok */
	}
	
	public int processSet(int deviceAddr, int propDesc, byte[] value_a) {
		//Default handler for set
		System.out.println("I am the handler for SET: d=" +
			deviceAddr + " p=" + propDesc + " value_len=" +
			value_a.length + "value=" + value_a);
		return 0; /* ok */
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

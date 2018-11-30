package DComm;

import App.DomoBusBE;

public class DCommAPI {

	private final static String DCOMM_DLL = "DComm";
	private final static int DCOMM_OK = 0;

	private static DCommAPI instance = null;
	private DomoBusBE dmb;
	private DCommRunnable dcom;
	
	//private MsgHandler handler;

	//////////////////

	static {
		System.loadLibrary(DCOMM_DLL);

		System.out.println("[DEBUG] " + DCOMM_DLL + ".dll loaded");
	}

	

	////////////////

	private native int DComm_config_from_file(String filename);

	private native int DComm_start();

	private native void DComm_stop();

	private native int DComm_register_callbacks(MsgHandler mHandler);

	private native int DComm_process_msg();

	private native int DComm_send_GET(int app_dest, int dev_addr_w, int prop_desc_w, int dev_addr, int prop_desc);

	private native int DComm_send_SET(int app_dest, int dev_addr_o, int prop_desc_o, int dev_addr, int prop_desc,
			byte[] value_a);

	private native int DComm_send_NOTIFY(int app_dest, int dev_addr, int prop_desc, byte[] value_a);
	/*
	 * private native int DComm_send_EXEC(int app_dest, byte[] action_p);
	 * 
	 * private native int DComm_send_MSG(int app_dest, int msg_ctr, int
	 * data_len, byte[] data_p);
	 */

	private native void DComm_print_error(int error_code);

	/////////////////

	private DCommAPI() {
	}

	public static DCommAPI init(String filename) {
		int status;

		if (instance == null) {
			synchronized (DCommAPI.class) {
				if (instance == null) {
					instance = new DCommAPI();

					System.out.println("[DEBUG] Initializing DCommAPI from file " + filename);
					status = instance.DComm_config_from_file(filename);
					if (status != DCOMM_OK) {
						instance.printError(status);
						return null; /* error */
					}

					status = instance.registerCallbacks(new MsgHandler());
					if (status != DCOMM_OK) {
						instance.printError(status);
						return null; /* error */
					}

					status = instance.DComm_start();
					if (status != DCOMM_OK) {
						instance.printError(status);
						return null; /* error */
					}

					instance.startDCommRunnable();
				}
			}
		}
		return instance;
	}

	private void startDCommRunnable() {

		dcom = new DCommRunnable(this);
		dcom.start();
		
	}

	public int registerCallbacks(MsgHandler mHandler) {
		int status;

		status = DComm_register_callbacks(mHandler);
		System.out.println("[REGISTER_CALLBACK] status=" + status);
		// printError(status);

		return status;
	}

	public int sendGet(int app_dest, int dev_addr_w, int prop_desc_w, int dev_addr, int prop_desc) {

		return DComm_send_GET(app_dest, dev_addr_w, prop_desc_w, dev_addr, prop_desc);
	}

	public int sendSet(int app_dest, int dev_addr_o, int prop_desc_o, int dev_addr, int prop_desc, byte[] value_a) {
		return DComm_send_SET(app_dest, dev_addr_o, prop_desc_o, dev_addr, prop_desc, value_a);
	}

	// public int sendNotify(int app_dest, int dev_addr, int prop_desc, byte[]
	// value_a)
	public int sendNotify(int dev_addr, int prop_desc, byte[] value_a) {
		int app_dest = 2; /* ## */
		int status;

		// System.out.println("[SendNotify] "+ app_dest + " " + dev_addr + " " +
		// prop_desc + " " + value_a[0]);

		status = DComm_send_NOTIFY(app_dest, dev_addr, prop_desc, value_a);
		// System.out.println("[SendNotify] "+ app_dest + " " + dev_addr + " " +
		// prop_desc + " " + value_a[0]);
		return status;
	}

	/*
	 * public int sendExec(int app_dest, byte[] action_p) { return
	 * DComm_send_EXEC(app_dest, action_p); }
	 * 
	 * 
	 * public int sendMsg(int app_dest, int msg_ctr, int data_len, byte[]
	 * data_p) { return DComm_send_MSG(app_dest, msg_ctr, data_len, data_p); }
	 */

	public void processMsg() {
		int status;

		status = DComm_process_msg();
		System.out.println("[PROCESS-MSG] Msg received (status=" + status + ")");
		if (status != DCOMM_OK)
			printError(status);
		dmb.updateWorkspace();
	}

	public void stop() {
		DComm_stop();
		System.out.println("[DEBUG] DCommAPI Stopped");
	}

	public void printError(int error_code) {
		DComm_print_error(error_code);
	}

	public int processGet(int deviceAddr, int propDesc, byte[] value_a) {


		value_a[0] = 0; /* 0=valid   else -> invalid value */
		value_a[1] = 72;
		value_a[2] = 0;
		value_a[3] = 0;
		value_a[4] = 0;
		
		return 0;
		
	}

	public void setDmb(DomoBusBE dmb) {
		this.dmb = dmb;
		
	}

	public DomoBusBE getDmb() {
		return dmb;
	}
	
	public void closeDComm() {
		if (dcom != null)
			dcom.stop();
		
	}

	

}

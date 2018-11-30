package DComm;

public class DCommRunnable implements Runnable {
	
	private DCommAPI api;
	private boolean running = true;
	private Thread dcomm_thread;
	
	public DCommRunnable(DCommAPI api) {
		this.api = api;
	}


	public void run() {
		
		while(running) {
			api.processMsg();
			//api.getDmb().updateWorkspace();
		}
		return;
		
		//System.out.println("[DEBUG] Stopped DComm thread");
	 	
	}
	
	public void start() {
		System.out.println("[DEBUG] Started dcomm thread");
		if (dcomm_thread == null) {
			dcomm_thread = new Thread(this, "DCommThread");
			dcomm_thread.start();
		}
	}
	
	public void stop() {
		System.out.println("[DEBUG] Stopped DComm thread");
		running = false;
		if (dcomm_thread != null)
			dcomm_thread.interrupt();
	}

}

package pt.upa.transporter.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import pt.upa.transporter.TransporterApplication;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

@WebService(
	    endpointInterface="pt.upa.transporter.ws.TransporterPortType",
	    wsdlLocation="transporter.1_0.wsdl",
	    name="TransporterWebService",
	    portName="TransporterPort",
	    targetNamespace="http://ws.transporter.upa.pt/",
	    serviceName="TransporterService"
	)
public class TransporterPort implements TransporterPortType {

	private TreeMap<String,JobView> jobs = new TreeMap<String,JobView>();
	private String _name;
	private int _id;
	private String _uddiURL;
	private String _url;
	private static String[] NORTH_AREA = {"Porto", "Braga" , "Viana do Castelo", "Vila Real", "Bragança"};
	private static String[] CENTER_AREA = {"Lisboa","Leiria","Santarém","Castelo Branco","Coimbra","Aveiro","Viseu","Guarda"};
	private static String[] SOUTH_AREA = {"Setúbal","Évora","Portalegre","Beja","Faro"};
	
	private Timer simulationTimer;
	
	public void setAll(String uddiURL, String name, String url){
		_name = name;
		_uddiURL = uddiURL;
		_url = url;
		_id = Integer.parseInt(name.substring(name.length() - 1)); 
		jobs = new TreeMap<String, JobView>();

	}
	
	public void setIdAndName(String name){
		_name = name;
		_id = Integer.parseInt(name.substring(name.length() - 1)); 
	}
	
	public void addJob(JobView j){
		jobs.put(j.getJobIdentifier(), j);
	}
	
	
	private void doSimulation(String id) {
		
		TimerTask simulation = new TimerTask() {
			@Override
		     public void run() {
				try {
					advanceState(id);
				} catch (Exception e) {
					System.err.println("Failed to advance job state");
				}
		     }
		};
		
		simulationTimer = new Timer(true);
		
		Random random = new Random();
		Long min = 1000L;
		Long max = 5000L;
		Random r = new Random();
		Long randomDelay = min+((long)(r.nextDouble()*(max-min)));
		//Long randomDelay = min+random.nextLong()*(max-min);
		
		simulationTimer.schedule(simulation, randomDelay);
	}
	
	private void advanceState(String id) throws BadJobFault_Exception {
		JobView job = jobs.get(id);
		if (job == null) {
			BadJobFault fault = new BadJobFault();
			fault.setId(id);
			throw new BadJobFault_Exception("Bad job: "+ id, fault);
		}
		
		if (job.getJobState() == JobStateView.ACCEPTED) {
			job.setJobState(JobStateView.HEADING);
		} else if (job.getJobState() == JobStateView.HEADING) {
			job.setJobState(JobStateView.ONGOING);
		} else if (job.getJobState() == JobStateView.ONGOING) {
			job.setJobState(JobStateView.COMPLETED);
		} else {
			return;
		}
		
		System.out.println("Job "+ id + " " + job.getJobState());
		doSimulation(id);
			
	}

	public void init(String uddiURL, String name, String url) throws Exception{
		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
		_name = name;
		_uddiURL = uddiURL;
		_url = url;
		_id = Integer.parseInt(name.substring(14));
		jobs = new TreeMap<String,JobView>();
		
		try{			
			endpoint = Endpoint.create(this);
	
			// publish endpoint
			System.out.println(TransporterApplication.class.getSimpleName() + " starting...");
			endpoint.publish(url);
	
			// publish to UDDI
			System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
			uddiNaming = new UDDINaming(uddiURL);
			uddiNaming.rebind(name, url);

			
			// wait
			System.out.println("Awaiting connections");
			System.out.println("Press enter to shutdown");
			System.in.read();
		}catch (Exception e){
			System.out.printf("Caught exception: %s%n", e);
			e.printStackTrace();
		} finally {
			try {
				if(simulationTimer != null)
					simulationTimer.cancel();
				if (endpoint != null) {
					// stop endpoint
					endpoint.stop();
					System.out.printf("Stopped %s%n", url);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when stopping: %s%n", e);
			}
			try {
				if (uddiNaming != null) {
					// delete from UDDI
					uddiNaming.unbind(name);
					System.out.printf("Deleted '%s' from UDDI%n", name);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when deleting: %s%n", e);
			}
		}
	}
		
	@Override
	public String ping(String name) {
		System.out.println("Received ping message: " + name + "\tI am: " + _name + "   id: " + _id);
		return "Received response from my message ( "+name+" ) from "+_name + "    id: " + _id;
	}

	@Override
	public JobView requestJob(String origin, String destination, int price)	throws BadLocationFault_Exception, BadPriceFault_Exception {
		System.out.println("\n-----------------------------------------------------------\nReceived a jobRequest!\t . . . Processing Info . . .");
		
		//Check origin location
		if(!Arrays.asList(SOUTH_AREA).contains(origin) && !Arrays.asList(CENTER_AREA).contains(origin) && !Arrays.asList(NORTH_AREA).contains(origin)){
			BadLocationFault fault = new BadLocationFault();
			fault.setLocation(origin);
			throw new BadLocationFault_Exception("Thats not a valid location!", fault);
		}
		//Check destination location
		if(!Arrays.asList(SOUTH_AREA).contains(destination) && !Arrays.asList(CENTER_AREA).contains(destination) && !Arrays.asList(NORTH_AREA).contains(destination)){
			BadLocationFault fault = new BadLocationFault();
			fault.setLocation(destination);
			throw new BadLocationFault_Exception("Thats not a valid location!", fault);
		}
		if(Math.random() * 100 > 90){
			System.out.println("Im not interested in your prupose!");
			return null;
		}
					
		if(price < 0){
			BadPriceFault fault = new BadPriceFault();
			fault.setPrice(price);
			throw new BadPriceFault_Exception("bad price fault", fault);
		}else if(price > 100){
			return null;
		}
		if(_id % 2 == 0){
			
			if((Arrays.asList(NORTH_AREA).contains(origin) ||Arrays.asList(NORTH_AREA).contains(destination))&& ((Arrays.asList(CENTER_AREA).contains(origin) ||Arrays.asList(CENTER_AREA).contains(destination)))){
				System.out.println("\tNew JobView as been created:");//nice
				JobView job = createJobView(_name,origin,destination,JobStateView.PROPOSED, price);
				System.out.println("ID\t\t" + job.getJobIdentifier() + "\nCompany:\t\t" + job.getCompanyName() + "\nOrigin:\t\t" + job.getJobOrigin() + "\nDestination:\t\t" + job.getJobDestination() + "\nPrice\t\t:"+ job.getJobPrice() + "\nState:\t\t" + job.getJobState());
				return job;
			}else{
				System.out.println("I dont have any routes with that arguments (origin/destination) (" + origin +"/"+ destination+")");
				return null;
			}
		}else{
			if((Arrays.asList(SOUTH_AREA).contains(origin) ||Arrays.asList(SOUTH_AREA).contains(destination)) && ((Arrays.asList(CENTER_AREA).contains(origin) ||Arrays.asList(CENTER_AREA).contains(destination)))){
				System.out.println("\tNew JobView as been created:");//nice
				JobView job = createJobView(_name,origin,destination,JobStateView.PROPOSED, price);
				System.out.println("ID\t\t" + job.getJobIdentifier() + "\nCompany:\t\t" + job.getCompanyName() + "\nOrigin:\t\t" + job.getJobOrigin() + "\nDestination:\t\t" + job.getJobDestination() + "\nPrice\t\t:"+ job.getJobPrice() + "\nState:\t\t" + job.getJobState());
				return job;
			}else{
				System.out.println("I dont have any routes with that arguments (origin/destination)");
				return null;
			}
		}
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		// TODO: Pedro - Done (?)
		JobView job;
		if(jobs == null){
			BadJobFault fault = new BadJobFault();
			fault.setId(id);
			throw new BadJobFault_Exception("Bad job: "+ id, fault);
		}else{
			
			job = jobs.get(id);
		}
		
		if (job == null) {
			BadJobFault fault = new BadJobFault();
			fault.setId(id);
			throw new BadJobFault_Exception("Bad job: "+ id, fault);
		}
		
		if (accept) {
			System.out.println("job accepted");
			job.setJobState(JobStateView.ACCEPTED);
			doSimulation(id);
		} else {
			job.setJobState(JobStateView.REJECTED);		
		}
		
		return job;
	}

	@Override
	public JobView jobStatus(String id) {
		return jobs.get(id);
	}

	@Override
	public List<JobView> listJobs() {
		List<JobView> list = new ArrayList<JobView>(jobs.values());
		return list;
		//TODO: Pedro
	}

	@Override
	public void clearJobs() {
		jobs.clear();
		//TODO: Pedro
	}
	
	public JobView createJobView(String CompanyName, String Origin, String Destination, JobStateView State, int price){
		JobView job = new JobView();
		job.setCompanyName(CompanyName);
		job.setJobDestination(Destination);
		job.setJobOrigin(Origin);
		if(price <= 10 && price > 0){
			ThreadLocalRandom.current().nextInt(1, 10 + 1);
		}else if(price > 10 && price <= 100){
			if(price % 2 == 0){
				if(_id % 2 == 0)
					job.setJobPrice(ThreadLocalRandom.current().nextInt(1, price + 1));
				else
					job.setJobPrice(ThreadLocalRandom.current().nextInt(price, 100 + 1));
			}else{
				if(_id % 2 == 0)
					job.setJobPrice(ThreadLocalRandom.current().nextInt(price, 100 + 1));
				else
					job.setJobPrice(ThreadLocalRandom.current().nextInt(1, price + 1));
			}
		}
		//Prevents creating id that already exists (odds are <0.00001%)
		String randomID = UUID.randomUUID().toString();
		if(!jobs.containsKey(randomID)){
			job.setJobIdentifier(UUID.randomUUID().toString());
		}else{
			while(jobs.containsKey(randomID)){
				randomID = UUID.randomUUID().toString();
				job.setJobIdentifier(UUID.randomUUID().toString());
			}
		}
		
		job.setJobState(State);
		jobs.put(job.getJobIdentifier(), job);
		return job;
	}
	public void insertJob(JobView job){
		jobs.put(job.getJobIdentifier(), job);
	}
	// TODO

}

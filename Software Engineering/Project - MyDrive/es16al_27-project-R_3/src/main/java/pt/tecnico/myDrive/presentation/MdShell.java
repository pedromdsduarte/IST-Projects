package pt.tecnico.myDrive.presentation;

import java.io.File;
import java.util.TreeMap;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.Main;

public class MdShell extends Shell{
	private Long token = null;
	private TreeMap<String,String> envVars;
	private String username = "";
	private String current_path = "/home";
	public static void main(String[] args) throws Exception{
		
	    
        if (args.length != 0 ){
        	for (String s : args) {
	        	restart();
				setup();
				xmlScan(new File(s)); 
        	}
        }else{
        	setup();
        }

		MdShell sh = new MdShell();
		sh.execute();

		FenixFramework.shutdown();
	}
	
	@Atomic
	public static void setup(){
		Main.setup();
	}
	
	@Atomic 
	public static void restart(){
		Main.restart();
	}
	
	@Atomic 
	public static void xmlScan(File f){
		Main.xmlScan(f);
	}
	
	public MdShell(){
		super("MyDrive");
		refreshPath();
		envVars = new TreeMap<String,String>();
		new Login(this);
		new List(this);
		new Write(this);
		new Execute(this);
		new Environment(this);
		new Key(this);
		new ChangeWorkingDirectory(this);
		Command c = get("login");
		c.execute(new String[]{"nobody"});
	}
	
	public Long getCurrentToken(){
		return token;
	}
	
	public void setToken(Long token){
		this.token = token;
	}
	
	public void refreshPath(){
		setPrompt("\033[32m"+username+"\033[37m:\033[33m"+current_path+" \033[37m>");
	}
	
	public void setUsername(String un){
		username = un;
		refreshPath();
	}

	public String getUsername() {
		return username;
	}
	
	public void setCurrentPath(String p){
		current_path = p;
		refreshPath();
	}
	
	public TreeMap<String,String> getEnvVars(){
		return envVars;
	}
	
	public void setEnvVars(TreeMap<String,String> newMap){
		envVars = newMap;
	}
}

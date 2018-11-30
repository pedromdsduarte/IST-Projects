package pt.tecnico.myDrive.presentation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.Main;

public abstract class Shell {
	protected static final Logger log = LogManager.getRootLogger();
	private Map<String,Command> coms = new TreeMap<String,Command>();
	private PrintWriter out;
	private String name;
	private String prompt = null;
	
	public Shell(String n) { this(n, new PrintWriter(System.out, true), true); }
	public Shell(String n, Writer w) { this(n,w,true);}
	public Shell(String n,Writer w, boolean flush) {
		name = n;
		out = new PrintWriter(w,flush);
		prompt = "$ ";
		/*quit command*/
		new Command(this,"quit","Exit the program"){
			public void execute(String[] args){
				FenixFramework.shutdown();
				System.out.println(name + " quit");
				System.exit(0);
			}
		};
		
		/*exec command*/
		new Command(this,"exec","Execute extern command") {
			void execute(String[] args) {
				try{
					Sys.output(out);
					Sys.main(args);
				}catch (Exception e) { 
					throw new RuntimeException(""+e);
				}
			}
		};
		
		/*run command*/
		new Command(this,"do","Run a class method"){
			void execute(String[] args){
				try{
					if(args.length>0)
						shell().run(args[0],Arrays.copyOfRange(args,1,args.length));
					else
						throw new Exception("Nothing to run!");
				}catch (Exception e) { 
					throw new RuntimeException(""+e);
				}
			}
		};
		
		new Command(this,"help","Help command"){
			void execute(String[] args){
				if(args.length==0){
					for(String s: shell().list())
						System.out.println(s);
					System.out.println(name()+" name (for more details)");
				}else{
					for(String s: args)
						if(shell().get(s) != null)
							System.out.println(shell().get(s).help());
				}
			}
		};
	}
	
	public void print(String s) { out.print(s);};
	public void println(String s) { out.println(s);};
	public void flush() { out.flush();}
	
	
	boolean add(Command c){
		return coms.put(c.name(), c) == null ? true : false;
	}
	
	public Command get(String s){
		return coms.get(s);
	}
	
	public Collection<String> list(){
		return Collections.unmodifiableCollection(coms.keySet());
	}
	
	public String getPrompt(){
		return prompt;
	}
	
	public void setPrompt(String p){
		prompt = p;
	}
	
	public void execute() throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String str;
		
		if(prompt==null)
			prompt = "$ ";
		System.out.println(name+" shell ('quit' to exit)");
		System.out.print(prompt);
		while((str=in.readLine()) != null){
			String[] arg = str.split(" ");
			Command c = coms.get(arg[0]);
			if(c != null){
				try{
					c.execute(Arrays.copyOfRange(arg, 1, arg.length));
				}catch(RuntimeException e) {
					System.err.println(arg[0]+": "+e);
					e.printStackTrace();
				}
			}else if(arg[0].length() > 0){
				System.err.println(arg[0]+": command not found! ('help' to list commands)");
				System.out.print(prompt);
			}
			System.out.println(name+" end");
			System.out.print(prompt);
		}
	}
	public static void run(String name, String[] args) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<?> cls;
		Method meth;
		try{
			cls = Class.forName(name);
			meth = cls.getMethod("main", String[].class);
		}catch(ClassNotFoundException cnfe){
			int pos;
			if((pos= name.lastIndexOf('.')) < 0)
				throw cnfe;
			cls = Class.forName(name.substring(0, pos));
			meth = cls.getMethod(name.substring(pos+1),String[].class);
		}
		meth.invoke(null, (Object)args); 
	}
	
	
	private static boolean wildcard(String text, String pattern){
		String[] cards = pattern.split("\\*");
		for (String card: cards){
			int idx = text.indexOf(card);
			if(idx==-1)
				return false;
			text = text.substring(idx + card.length());
		}
		return true;
	}
}

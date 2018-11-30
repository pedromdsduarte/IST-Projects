package pt.tecnico.myDrive;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.File;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.*;
import pt.tecnico.myDrive.service.ListDirectoryService;
	

public class Main {
	static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) throws IOException {
        System.out.println("*** Welcome to the myDrive application! ***");
        
        if (args.length != 0 ){
	        for (String s : args) {
				restart();
				setup();
				xmlScan(new File(s)); 
				xmlPrint();
			}
        }else{
			setup();
			xmlPrint();
			test();
		}
		
		FenixFramework.shutdown(); //Tem de estar sempre presente!!!!   
    }
	
    @Atomic
    public static void xmlScan(File file){
		log.trace("xmlScan: " + FenixFramework.getDomainRoot());
		MyDrive md = MyDrive.getInstance();
		SAXBuilder builder = new SAXBuilder();
		try{
			Document document = (Document)builder.build(file);
			md.xmlImport(document.getRootElement());
		}catch (JDOMException | IOException e){
			e.printStackTrace();
		}
    }
    
    @Atomic
    public static void restart() {
		log.trace("Init: " + FenixFramework.getDomainRoot());
		MyDrive md = MyDrive.getInstance();
		if(!md.getUserSet().isEmpty())
			md.cleanup();
	}
    
    
    @Atomic
    public static void setup() {
		MyDrive md = MyDrive.getInstance();
		
		if(!md.getUserSet().isEmpty())
			return;

		//User root = md.createUser("root", "***", "Super User", "rwxdr-x-");
		User root = new User("root", "***", "Super User", "rwxdr-x-");
		Dir mainDir = new Dir("/", 1, 100, root);
		Dir home = new Dir("home", 2, 100, root);
		Dir rootHome = new Dir("root", 3, 100, root);
		
		md.setRootDir(mainDir);
		md.addUser(root);
		
		mainDir.setOwner(root);
		mainDir.setParentDir(mainDir);
		
		home.setOwner(root);
		home.setParentDir(mainDir);
		
		rootHome.setOwner(root);
		rootHome.setParentDir(home);
		
		root.setHomeDir(rootHome);
		
		md.setLastID(3);
	
	//	md.listDirectories("/home/root");
		User test = md.createUser("test", "blablabla", "Test", "rwxd----");
		App t = md.createApp("echo", test, home, "pt.tecnico.myDrive.Test.echo");
		PlainFile pf = md.createPlainFile("plainfile", test, test.getHomeDir(), "hello.bat\nhello.bat");

		User nobody =md.createUser("nobody", "", "Guest", "rxwdr-x-");

	}
    
    @Atomic
    public static void xmlPrint() {
        log.trace("xmlPrint: " + FenixFramework.getDomainRoot());
        Document doc;
        
        try {
        	doc = MyDrive.getInstance().xmlExport();
        	XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
			xmlOutput.output(doc, new PrintStream(System.out));
			
		} catch (IOException e) { 
			System.out.println(e); 
		} catch (ExportDocumentToXMLException e) {
        	System.out.println(e.getMessage());
        }
    }

    @Atomic
	public static void test() {

		MyDrive md = MyDrive.getInstance();
		long token;

		// testing login
		
		try {

			token = md.login("root", "***");
			User testLogin = md.getSessionManager().getUserByToken(token);
			System.out.println("Name: " + testLogin.getName() + "\nCurrentDir: " + md.getPath(md.getSessionManager().getCurrentDirByToken(token)));

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		
		
		// done testing login
		
		User u = md.getUserByUsername("root");
		
		// create file README
		Dir home = md.browseToDir("/home");
		PlainFile README = md.createPlainFile("README", u, home, "lista de utilizadores");
		
		// create usr/local/bin
		home = md.getRootDir();
		Dir usr = md.createDir("usr", 2, u, home);

		Dir local = md.createDir("local", 2, u, usr);

		
		Dir bin = md.createDir("bin", 2, u, local);
		
		
		// print README content
		md.showFileContent("/home/README");
		
		// remove directory /usr/local/bin
		md.removeFile("/usr/local/bin");
		
		
		
		// remove file /home/README

		md.removeFile("/home/README");
		
		// list directory /home
		System.out.println(md.listDirectories("/home").toString());
	}

}



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
	

public class Main {
	static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) throws IOException {
        System.out.println("*** Welcome to the myDrive application! ***");
        
        for (String s : args) {
			restart();
			//setup();
        	xmlScan(new File(s)); 
		}
		setup();
		test();

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
			md.getInstance().cleanup();
	}
    
    
    @Atomic
    public static void setup() {
		MyDrive md = MyDrive.getInstance();
		
		if(!md.getUserSet().isEmpty())
			return;

		
		User root = md.createUser("root", "***", "Super User", "rwxdr-x-");
		
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
		
		User u = md.getUserByUsername("root");
		
		// create file README
		Dir home = md.browseToDir("/home");
		PlainFile README = md.createPlainFile("README", u, md.getFileCount() + 1, u.getUmask(), "lista de utilizadores", new DateTime());
		README.setDir(home);
		md.addFile(README);
		
		// create usr/local/bin
		//md.browseToDir("/");
		Dir usr = md.createDir("usr", "root",  md.getFileCount() + 1, u.getUmask(), 2);
		usr.setDir((Dir)md.getRootDir());
		usr.setUser(u);
		md.addFile(usr);

		Dir local = md.createDir("local", "root",  md.getFileCount() + 1, u.getUmask(), 2);
		local.setDir(usr);
		local.setUser(u);
		md.addFile(local);

		
		Dir bin = md.createDir("bin", "root",  md.getFileCount() + 1, u.getUmask(), 2);
		bin.setDir(local);
		bin.setUser(u);
		md.addFile(bin);
		
		// print README content
		md.showFileContent("/home/README");
		
		// remove directory /usr/local/bin
		//md.removeFile("/usr/local/bin");
		bin.remove();
		
		xmlPrint();
		
		// remove directory /home/README
		//md.removeFile("/home/README");
		README.remove();
		
		// list directory /home
		md.listDirectories("/home");
	}

}



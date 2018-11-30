import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.File;
import java.util.concurrent.*;
import java.text.DecimalFormat;

import java.util.*;



public class TES {
    static int PORT = 59000;
    static String ECP_NAME = "localhost";
    static int ECP_PORT = 58015;
    static DatagramSocket serverSocket;
    static byte[] receiveData = new byte[1024];
    static byte[] sendData = new byte[1024];
    static Socket socketTCP;
    //static InetAddress address = null;
    //static final int DEADLINE = 2;
    static String deadline = "";

    static int topic = 1;
    static String[] topics = {"Networking","Security","Protocols"};

    static TreeMap<Integer,String> users = new TreeMap<Integer,String>();

    static String keyPath;

    public static void badArguments() {
        System.err.println("The program must be invoked as follows:"
                        + " java TES [-f TOPICNumber] [-p TESport] [-n ECPname] [-e ECPport]\n"
                        + "[-p TESport], [-n ECPname] and [-e ECPport] are optional flags. TOPICNumber is NOT optional.\n");
        System.exit(-1);
    }

    //static String QID = "", checkQID;


    public static class tcpThread extends Thread {
        Socket connectionSocket;
        DatagramSocket serverSocket;
        Calendar deadline_calendar;
        String topicName="";
        Boolean closed;
        String QID = "", checkQID;
        InetAddress address = null;
        BufferedReader inputFromClient;
        DataOutputStream sendStream;

        int SID;

        int score = 0;
        String[]Answers = new String[5];
    	  DateFormat dateFormat = new SimpleDateFormat("ddmmyyyy_HH:mm:ss");

        public tcpThread(Socket socket, DatagramSocket serverSocket) {
	          this.serverSocket = serverSocket;
            this.connectionSocket = socket;
            this.closed = false;


            try {
      	      address = connectionSocket.getInetAddress().getByName(ECP_NAME);
      	      //inputFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      	      //sendStream = new DataOutputStream(connectionSocket.getOutputStream());
      	    }
	          catch (Exception e) {
	              e.printStackTrace();
	          }

        }

        public static int scorer(String[] answers, String[] keys){
        int score = 0;
        for (int i = 0; i < 5; i++) {
            if (answers[i].equals(keys[i]))
                score += 20;
        }
        return score;
    }
    public static boolean reached_deadline(String checkDate) {
        try{
            Date now = new Date();
            Calendar gc = new GregorianCalendar();
            gc.setTime(now);

            DateFormat df = new SimpleDateFormat("ddMMMyyyy_HH:mm:ss");
            Calendar deadline = Calendar.getInstance();
            deadline.setTime(df.parse(checkDate));

            Date d2 = gc.getTime();
            String strDate1 = df.format(d2);
            if(gc.after(deadline)){
                return true;
            }
            return false;

        }catch (Exception e) {
                System.err.println("\nCannot parse deadline time\n");
                System.exit(-1);
        }
        return false;
    }

    public static String getDeadline(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("ddMMMyyyy_HH:mm:ss");
        Date now = new Date();
        Calendar gc = new GregorianCalendar();
        gc.setTime(now);
        gc.add(Calendar.HOUR, 1);
        Date d2 = gc.getTime();
        //String strDate = sdfDate.format(now);
        String strDate1 = sdfDate.format(d2);
        return strDate1;
    }
    public static Calendar deadline_date(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("ddMMyyyy_HH:mm:ss");
        Date now = new Date();
        Calendar gc = new GregorianCalendar();
        gc.setTime(now);
        gc.add(Calendar.HOUR, 1);
        return gc;
    }

    public static String getTopicName(int topic_number){

        try {
            return topics[topic_number-1];
        }
        catch (Exception e) {
            System.err.println("\nERROR: Something went wrong while fetching the topic name\n");
            System.exit(-1);
        }
        return null;

        /*
        try {
                File topics =new File("topics.txt");
                InputStream fi1s = new FileInputStream(topics);
                InputStreamReader isr1 = new InputStreamReader(fi1s,"UTF-8");
                BufferedReader br1 = new BufferedReader(isr1);
                String topico = br1.readLine();
                for(int i = 0; i < topic_number-1; ++i){
                    br1.readLine();
                }
                String lineIWant = br1.readLine();
                return lineIWant;
        }
        catch (IOException e) {
            System.err.println("\nInput/Output stream error.\n");
            System.exit(-1);
        }
        catch (Exception e) {
            System.err.println("\nAn unknown error has occurred. Aborting.\n");
            System.exit(-1);
        }
        return null;*/
    }


//------------------------------------------------------------------------------

    public static void uploadFile(DataOutputStream sendStream, FileInputStream requestedFile, int fileSize) throws IOException {

        int bytesRead = 0;
        int totalRead = 0;
      	byte[] buffer = new byte[1024];


      	/*bytesRead = requestedFile.read(buffer);
      	System.out.println("\n\nNumero de bytes que vou ler é: "+bytesRead+"\nO tamanho do ficheiro é: "+fileSize+"\n\n");
      	bytesRead = 0;*/


      	//System.out.println(fileSize-totalRead+" bytes left.");
      	while((bytesRead = requestedFile.read(buffer)) >= 0) {
      	    totalRead += bytesRead;
      	    //System.out.println(fileSize-totalRead+" bytes left.");
      	    sendStream.write(buffer);
      	    sendStream.flush();
      	    if((fileSize - totalRead) < 1024) {
                  byte[] rest = new byte[fileSize-totalRead];
                  totalRead += rest.length;
                  requestedFile.read(rest);
                  sendStream.write(rest);
                  sendStream.flush();
                  //System.out.println("Uploaded File.");
                  sendStream.write('\n');
                  sendStream.flush();
                  break;
            }
      	}
    }

//------------------------------------------------------------------------------


        public void run() {
            try{
                
              //connectionSocket = socketTCP.accept();

             String clientSentence;

           



            //DataOutputStream outputToClient = new DataOutputStream(connectionSocket.getOutputStream());

			inputFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			sendStream = new DataOutputStream(connectionSocket.getOutputStream());


			clientSentence = inputFromClient.readLine();
			//System.out.println("RECEIVED FROM CLIENT: "+clientSentence);
			String[] parts = clientSentence.split(" ");

			if(parts[0].trim().equals("RQT") && parts.length == 2){
			    if (!connectionSocket.isConnected()) {
			        System.err.println("\nConnection socket is closed. Aborting.\n");
			        System.exit(-1);
			    }


	        	System.out.println("Request: "+parts[1]+" "+address.getHostAddress()+":"+connectionSocket.getPort() + "\tclient sentence: "+clientSentence);
	        	if(parts[1].trim().length() == 5){
	        	    try {
	        	        if (users.containsKey(SID) ){
	        	            System.err.println("\nERROR: Student is already taking a questionnaire.\n");
	        	            return;
	        	        }
	        	        SID = Integer.parseInt(parts[1].trim());
	        	        //System.out.println("SID = " + SID + " and SID length is " + parts[1].trim().length());
	        	    }
	        	    catch (NumberFormatException exception) {
	        	        sendStream.write("ERR\n".getBytes());
	        	        sendStream.close();
	        	    }
	        	}
	        	else {
	        	    sendStream.write("ERR\n".getBytes());
	        	    sendStream.close();
	        	}
                
                Random gerador = new Random();
                int numero = gerador.nextInt(2)+1;
              

                
	        	String FilePath = "Questionnaires/"+"T"+topic+"QF"+"00"+numero+".pdf";   //mudar isto para que o
	        	                                                                    //"001" seja random

	        	keyPath = "Questionnaires/"+"T"+topic+"QF"+"001"+"A.txt";
                System.out.println("\nFilename of pdf: "+FilePath+ "\tkeys at: "+keyPath);
	        	
        		File file = new File(FilePath);
        		int fileSize = (int) file.length();

		 	if (file.exists()){
	        	    QID = parts[1]+"_"+getDeadline().replace(":","");
                    users.put(SID,QID);
	        	    System.out.println("########################## QID: " + QID);

			    FileInputStream requestedfile = new FileInputStream(file);

			    //System.out.println("File: " + FilePath);
			    deadline = getDeadline();
			    String Aqt = "AQT "+QID+" "+deadline+ " " +fileSize + " "; //Depois tratar do tempo real
			    //try {
						    sendStream.write(Aqt.getBytes());
				//System.out.println("written: "+sendStream.size());
			    //}
			    /*catch(IOException exception) {
				exception.printStackTrace();
				sendStream.close();
				System.exit(-1);
			    }*/

			    //Sends the bytes corresponding to the file data
			    try {
  			    uploadFile(sendStream, requestedfile, fileSize);
  			    requestedfile.close();
			    }
			    catch (IOException exception) {
    				System.err.println("\nSomething went wrong while uploading the file.\n");
    				exception.printStackTrace();
    				sendStream.close();
    				requestedfile.close();
			    }

          connectionSocket.shutdownOutput();
			    //sendStream.close();

		      }
		      else {
	                sendStream.write("ERR\n".getBytes());
	                sendStream.close();
		      }

	          }

			// FALTA METER ISTO A DAR O ERR, nao esquecer
			if (parts[0].equals("RQS") && parts.length == 8) {
			    //sendStream = new DataOutputStream(connectionSocket.getOutputStream(),true);

			    try {
			        SID = Integer.parseInt(parts[1]);
			    }
			    catch (NumberFormatException exception) {
			        sendStream.write("ERR\n".getBytes());
			    }
				checkQID = parts[2];

                String QID = users.get(SID);

			    if(reached_deadline(deadline)){
			        sendStream.write(("AQS " + QID + " -1").getBytes());
			    }

			    else if(SID!=Integer.parseInt(parts[1]) || !QID.equals(checkQID)){
			        sendStream.writeBytes("AQS " + QID +" -2");
			    }


				//VERIFICAÇÃO DA CHAVE
			    for(int i=0;i<5;i++)
			        Answers[i]=parts[i+3];
				  try{
				    FileReader file_answers = new FileReader(keyPath);
				    BufferedReader buffer_answers = new BufferedReader(file_answers);
				    //String answers_string = buffer_answers.readLine();
				    //String[] answers_keys =  answers_string.split(" ");
				    String[] answers_keys = new String[5];

				    for(int i = 0; i < 5; i++) {
				        answers_keys[i] = buffer_answers.readLine();
				    }

				    score = (int)scorer(Answers,answers_keys);
				    System.out.println("\nReceived submit from: "+SID+" Score: "+(int)score+"%");


				    String Aqs = "AQS"+" "+QID+" "+(int)score+"\n";
				    //System.out.println("AQS: " + Aqs);
				    sendStream.write(Aqs.getBytes());
				}catch(Exception e){
				    e.printStackTrace();
				    System.err.println("Error while trying to read answers file "+keyPath);
				}


				String topicName = getTopicName(topic);

				if(topicName == null) {
				    System.err.println("\nUnknown error.\n");
				    sendStream.close();
				}


				try{
				    String Iqr="IQR"+" "+SID+" "+QID+" "+topicName+" "+score+"\n";
				  //  System.out.println("IRQ: "+Iqr);
				    byte[] sendData = Iqr.getBytes();
				    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address , ECP_PORT); //Duvida no IPAddress
				    if(serverSocket == null) System.out.println("socket ta null crl");
				    serverSocket.send(sendPacket);
				    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				    serverSocket.receive(receivePacket);
				    String sentence = new String(receivePacket.getData());
                    String[] responseParts = sentence.trim().split(" ");
        
                    if (responseParts[0].trim().equals("AWI")) {
                            System.out.println("\nReceived ECP acknowledge for QID "+responseParts[1]);
                            users.remove(SID);
                    }
                    else if (responseParts[0].trim().equals("ERR")) {
                        System.out.println("\nERROR: ECP received wrong information from IQR for QID "+QID+"\n");
                    }
        		}
			    catch(Exception e){
				    e.printStackTrace();
				    System.err.println("Error sending IQR request!");
				}
			}




		//inputFromClient.close();
        }catch(Exception e){
	    e.printStackTrace();
            System.err.println("erro na thread");
            System.exit(-1);
        }
    }
    }



public static void main(String args[]) throws Exception {


//------------------------------------------------------------------------------
//User Application Invocation error test zone-----------------------------------
//------------------------------------------------------------------------------


//------If number of arguments greater than 6 kill process----------------------
        if (args.length > 8 || (args.length % 2) != 0) {
                badArguments();
        }


        boolean pIsNotSet, nIsNotSet, eIsNotSet, fIsNotSet;
        pIsNotSet = nIsNotSet = eIsNotSet = fIsNotSet = true;

        for (int i = 0; i < args.length; i++) {
                if(args.length == 1) {
                    if(!args[i].equals("-p") || !args[i].equals("-n") || !args[i].equals("-e") || !args[i].equals("-f")) {
                        badArguments();
                    }
                }

                if (args[i].equals("-p") && pIsNotSet) {
                    try{
                        PORT = Integer.parseInt(args[i+1]);
                        pIsNotSet = false;
                    }
                    catch(NumberFormatException exception) {
                        System.err.println("\nERROR: The port number must be an Integer.\n");
                        System.exit(-1);
                    }
                }

                else if (args[i].equals("-n") && nIsNotSet) {
                    ECP_NAME = args[i+1];
                    nIsNotSet = false;
                }

                else if (args[i].equals("-e") && eIsNotSet) {
                    try {
                        ECP_PORT = Integer.parseInt(args[i+1]);
                        eIsNotSet = false;
                    }
                    catch (NumberFormatException exception) {
                        System.err.println("\nERROR: The ECP port number must be an Integer.\n");
                        System.exit(-1);
                    }
                }

                else if (args[i].equals("-f") && fIsNotSet) {
                    try {
                        topic = Integer.parseInt(args[i+1]);
                        fIsNotSet = false;
                    }
                    catch (NumberFormatException exception) {
                        System.err.println("\nERROR: The topic number must be an Integer.\n");
                        System.exit(-1);
                    }
                }
        }
        if (fIsNotSet) {
            badArguments();
        }

        if (args.length == 4) {
            if (!(pIsNotSet || nIsNotSet || eIsNotSet))
                badArguments();
        }

        if (args.length == 6) {
            if ((pIsNotSet && nIsNotSet) || (pIsNotSet && eIsNotSet) || (nIsNotSet && eIsNotSet)) {
                badArguments();
            }
        }

        if (args.length == 8) {
            if (pIsNotSet || nIsNotSet || eIsNotSet) {
                badArguments();
            }
        }

//------------------------------------------------------------------------------


		DatagramSocket serverSocket = null;
		byte[] receiveData = new byte[1024];

	    //topic = PORT-59000;
	    System.out.println("topic: "+topic);
	    ServerSocket socketTCP = null;
	    String QID = "", checkQID;

	    try {
    		serverSocket = new DatagramSocket();
        	socketTCP = new ServerSocket(PORT);
		}



		catch(UnknownHostException uh) {
            System.err.println("\nERROR: Cannot connect to host.\n");
            System.exit(-1);
        }
        catch(BindException be) {
            be.printStackTrace();
            System.err.println("\nERROR: Unable to open the socket or to bind socket to specified port.\n");
            System.exit(-1);
        }

        System.out.println("ECP_PORT: "+ECP_PORT + "\nPORT: "+PORT+"\nECP_NAME: "+ECP_NAME);


		while(true) {
            Socket connectionSocket;
            try{
            connectionSocket = socketTCP.accept();
            if(connectionSocket.isConnected()){
                Thread t = new Thread(new tcpThread(connectionSocket,serverSocket));
                t.start();
            }
            }catch(Exception e){
                System.err.println("Error while threading. . .");
            }


    	    }

        }

    }

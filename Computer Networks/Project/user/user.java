import java.io.*;
import java.net.*;

public class user {
    
    static DatagramSocket udpSocket;
    static Socket tcpSocket;
	static int port = 58015;
	static String host = "localhost";
	static InetAddress address;
	
	static BufferedReader _stdin = new BufferedReader(
                                   new InputStreamReader(System.in));
	
	static int SID;

//------------------------------------------------------------------------------	

    public static void badArguments() {
        System.err.println("The program must be invoked as follows:"
                        + " java user SID [-n ECPname] [-p ECPport]\n"
                        + "[-n ECPname] and [-p ECPport] are optional flags.\n");
        System.exit(-1);
    }

//------------------------------------------------------------------------------	
	
	/**
     * udpConnection(byte[] buffer,InetAddress add,int port, DatagramSocket sock)
     *
     * Method that provides the UDP connection between the User and the
     * ECP. This method is meant to treat the "list" command
     *
     * @param buffer is the message sent by the user to the server
     * @param add is the IP address of the server host
     * @param port is the number of the port to connect
     * @param sock is the socket open by the connection
     * @return returns an array containing the parsed answer of the server
     * (without spaces).
     */
	
	public static String[] udpConnection(byte[] buffer, InetAddress add, int port, DatagramSocket sock) {

        try{

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, add, port);

            sock.send(packet);

            byte[] responseBuffer = new byte[1024];
            packet = new DatagramPacket(responseBuffer, responseBuffer.length);

            udpSocket.receive(packet);

            //System.out.println("\nECP responded: " + new String(packet.getData())+"\n");

            return new String(packet.getData()).split(" ");

        }
        catch(EOFException eof){ }
        catch(IOException io){
            System.err.println("\nInput/Output stream error.\n");
        }

        return null;
    }
    
//------------------------------------------------------------------------------        

    public static int downloadFile(String fileName, DataInputStream tcpMessageIn, int fileSize) throws IOException {
        
        int bytesRead;
        int sizeRead = 0;
        byte[] buffer = new byte[1024];
        File file = new File(fileName);

        DataInputStream inputStream = tcpMessageIn;
        FileOutputStream fileStream = new FileOutputStream(file);
        
        System.out.print("Downloading... ");
        while((bytesRead = inputStream.read(buffer)) >= 0) {
	        System.out.print("");
            sizeRead += bytesRead;
            fileStream.write(buffer);
            fileStream.flush();
            try {
                Thread.sleep(2);
            } catch(Exception e) {}
        }

        System.out.println("Complete!");
	fileStream.close();
        return sizeRead-1; // \n does not count 
    }

//------------------------------------------------------------------------------  
   
    /**
     * readResponse(DataInputStream input)
     * Method that receives an input stream and reads from it one byte
     *  at a time, and returns the string corresponding to the message
     *  "AQT QID time size " from the USER-TES interaction
     * Used only for this specific case.
     *
     * @param input
     *  Input Stream from the socket
     * @return 
     *  String of the message "AQT QID time size "
     */
   
    public static String readResponse(DataInputStream input) throws IOException {
        byte[] temp = new byte[1];
        String res = "";
        int spaces = 0;
        while(spaces < 4) {         //there are 4 spaces in the message until
                                    //the "data" (excluding)
            input.read(temp);
            //System.out.print(new String(temp));
            res += new String(temp);
            if (temp[0] == (byte)' ')
                spaces++;
        }
        return res;
    }  
    
//------------------------------------------------------------------------------    
    
    /**
     * receiveFile(Socket tcpSocket, String filen)
     * Method that receives a connected socket and a name of a file to retrieve
     * and retrieves the bytes that compose the file over a TCP protocol, by
     * retrieving every packet of data sent from the TES server
     *
     * @param tcpSocket
     *  Socket connected with the TES server by a TCP protocol
     * @param SID
     *  SID of the student requesting the file
     * @return 
     *  Integer containing the requested file QID
     */
    
    public static String receiveFile(Socket tcpSocket, int SID){
        try {

                String msg = "RQT "+ SID + "\n";
                String QID = null;
                int size = 0;
                int msgLen;
                byte[] response = new byte[1024];
                String responseString = null;

                DataOutputStream tcpMessageOut = new DataOutputStream(tcpSocket.getOutputStream());
                DataInputStream tcpMessageIn = new DataInputStream(tcpSocket.getInputStream());
				
                tcpMessageOut.writeBytes(msg);
		//tcpMessageIn.read(response);
		//responseString = new String(response);
                
		try {
                    responseString = readResponse(tcpMessageIn);
                } 
                catch (IOException e) {
                    //e.printStackTrace();
                    System.err.println("\nERROR: readResponse error.\n");
                    System.exit(-1);
                }
                System.out.println("Received: "+ responseString);

                String[] tcpParsed = responseString.trim().split(" "); 
                
                if(tcpParsed == null) {
                    System.out.println("\nERROR: Could not connect to the TES Server.\n");
                    return null;
                }

                if(tcpParsed[0].equals("AQT") && tcpParsed.length == 4) {
                    QID = tcpParsed[1].trim();    
                    String fileName = QID.replace(":","")+".pdf";

                    String deadline = tcpParsed[2].trim();
                    
                    try {
                        size = Integer.parseInt(tcpParsed[3].trim());
                    }
                    catch(NumberFormatException exception) {
                        System.err.println("\nERROR: The size must be an Integer.\n");
                        //tcpMessageIn.close();
                        System.exit(-1);
                    }

                    int bytesReceived = 0;
                    try {
                        bytesReceived = downloadFile(fileName, tcpMessageIn, size);
                    }
                    catch (IOException exception) {
                        System.err.println("\nERROR: Something went wrong while downloading the file.\n");
                        exception.printStackTrace();
                        return null;
                    }

                    if (size != bytesReceived) {
                        System.err.println("\nERROR: Size of the file doesn't match message received from the server.\n");
                        return null;
                    }

                    System.out.println("Received file " + fileName+"\n");
                    return QID;
                    
                }
                
                else { 
                    System.err.println("\nERR: Invalid response from TES server.\n");
                    return null;
                }

        }
        catch(IOException io) { 
            System.err.println("\nInput/Output stream error.\n");
            return null;
        }

    }
    
//------------------------------------------------------------------------------
    
    /**
     * validatedAnswer(String[] sequence)
     * Method that receives an answer sequence and validates it
     *
     * @param sequence
     *  Array of answers from the user
     * @return 
     *  Boolean containing true if the answer is valid, false otherwise
     */
    
    public static boolean validatedAnswer(String[] sequence) {
        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i].charAt(0) < (int)'A' || sequence[i].charAt(0) > (int)'D')
                if(sequence[i].charAt(0) != 'N')
                    return false;
        }
        return true;
    }

//------------------------------------------------------------------------------

    /**
     * getscore(String submit, Socket tcpSocket)
     * Method that receives the submission message and a connected socket
     *
     * @param submit
     *  String containing the submission message to send to the TES server
     * @param tcpSocket
     *  Socket connected with the TES server by a TCP protocol
     * @return 
     *  Integer containing the score of the submission
     */

    public static int getScore(String submit, Socket tcpSocket) {
        try {
            byte[] response = new byte[1024];
            DataInputStream tcpMessageIn = new DataInputStream(tcpSocket.getInputStream());
            DataOutputStream tcpMessageOut = new DataOutputStream(tcpSocket.getOutputStream());
    
            tcpMessageOut.writeBytes(submit);
	        tcpMessageOut.flush();
            
            tcpMessageIn.read(response);
            
            tcpMessageIn.close();
            tcpMessageOut.close();
            
            String responseString = new String(response);
            
            //System.out.println("Received: "+ responseString);
            String[] parsed = responseString.split(" ");
            int score = -1;
            String QID = null;
            
            if(parsed == null) {
                System.out.println("\nERROR: Could not connect to the TES Server.\n");
            }

            if (parsed[0].trim().equals("AQS") || parsed.length <= 3) {
                
                QID = parsed[1].trim(); 
                
                try {
                    score = Integer.parseInt(parsed[2].trim());
                } 
                catch (NumberFormatException exception) {
                    System.err.println("\nERROR: Score must be Integer.\n");
                    return -3;
                }

                
                return score;
            }
            
            else if (parsed[0].trim().equals("ERR")) {
                System.err.println("\nERR: Bad request.\n");
                System.exit(-1);
                
            }
            
            else { 
                System.err.println("\nERROR: Invalid response from TES server.\n");
                return -3;
            }
        }
        catch(IOException io) {
            System.err.println("\nInput/Output stream error.\n");
            return -3;
        }
        return -3;
    }

//------------------------------------------------------------------------------

    
    public static void main(String[] args) {
        
        
//------------------------------------------------------------------------------
//User Application Invocation error test zone-----------------------------------
//------------------------------------------------------------------------------

        if (args.length < 1 || args.length > 5 || (args.length)%2 == 0) {
            badArguments();
        }
        
        try  {
            SID = Integer.parseInt(args[0]);
        }
        catch(NumberFormatException exception) {
            System.err.println("The SID must be an Integer");
            System.exit(-1);
        }
        
//------Se for do tipo "java user XXXXX -p" ou "java user XXXXX -n"-------------
        
        if (args.length == 3) {
            if (args[1].equals("-n")) {
                host = args[2];
            }
            else if (args[1].equals("-p")) {
                    try {
                        port = Integer.parseInt(args[2]);
                    }
                    catch(NumberFormatException exception) {
                        System.err.println("The port number must be an Integer.\n");
                    	System.exit(-1);
                    }
            }
            else {
                badArguments();
            }
            
        }
        
//------Se for com todas as flags "java user XXXXX -n YYYY -p ZZZZ" ------------        
        
        else if (args.length == 5) {
            if (args[1].equals("-n") && args[3].equals("-p")) {
                host = args[2];
                try {
                    port = Integer.parseInt(args[4]);
                }
                catch(NumberFormatException exception) {
                    System.err.println("The port number must be an Integer.\n");
                    System.exit(-1);
                }
            }
            else {
                badArguments();
            }
        }


//------------------------------------------------------------------------------
//Connection Setup--------------------------------------------------------------
//------------------------------------------------------------------------------
        try {
            address = InetAddress.getByName(host);
            udpSocket = new DatagramSocket();
            //udpSocket.setSoTimeout(1000);
        } 
        catch(UnknownHostException uh) {
            System.err.println("Cannot connect to host.\n");
            System.exit(-1);
        }
        catch(SocketException sE) {
            System.err.println("Unable to open the socket or to bind socket to specified port.\n");
            System.exit(-1);
        }


        System.out.println("User is attempting to connect to host: "
                +host +" address: "+address.getHostAddress()+":"+ port);

//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------


        String userCommand;
        String[] parsedCommand;
        String[] serverAnswer;
        String QID = null;
        int tesPort = 0;
        InetAddress tesIP = null;
        
        int ntopics;
        
        while(true) {
            
            System.out.println("Insert one of the following commands:"+
                "\nlist\nrequest \nsubmit\nexit");
            System.out.print("> ");
            
            try {
                userCommand = _stdin.readLine();
                parsedCommand = userCommand.split(" ");
                
                
                //--------------------------------------------------------------------
                //Command "list"------------------------------------------------------
                //--------------------------------------------------------------------
                
                if (parsedCommand.length == 1 && parsedCommand[0].equals("list")) {
                    serverAnswer = udpConnection("TQR\n".getBytes(), address, port, udpSocket);
                    
                    if (serverAnswer == null) {
                        System.out.println("\nERROR: Could not connect to ECP Server.\n");
                        continue;
                    }
                    
                    if(!serverAnswer[0].equals("AWT") && !serverAnswer[0].equals("EOF") && !serverAnswer[0].equals("ERR")) {
                        System.err.println("\nERROR: Unknown server answer: "+serverAnswer[0]+"\nAborting Connection.\n");
                        continue;
                    }
                    
                    else {
                        
                        if (serverAnswer[0].equals("ERR")) {
                            System.err.println("\nERR: Incorrect TQR formulation.\n");
                            continue;
                        }
                        
                        if (serverAnswer[0].equals("EOF")) {
                            System.err.println("EOF: No questionnaire topics available.\n");
                            continue;
                        }
                        
                        try {
                            ntopics = Integer.parseInt(serverAnswer[1]);
                        } 
                        catch(NumberFormatException exception) {
                            System.err.println("\nERROR: The number of topics is not an Integer.\n");
                        	continue;
                        }
                        
                        if (serverAnswer.length - 2 != ntopics) {
                            System.err.println("\nERROR: Mismatching list size to number of listed topics.\n");
                            continue;
                        }
                        
                        System.out.print("\n");
                        for(int i = 2; i < serverAnswer.length; i++) {
                            System.out.println(i-1 + "- " + serverAnswer[i]);
                        }
                        
                        
                    }
                    
                }
                
                //--------------------------------------------------------------------
                //Command "request"---------------------------------------------------
                //--------------------------------------------------------------------
                
                if(parsedCommand.length == 2 && parsedCommand[0].equals("request")) {
                    
                    String request = "TER " + parsedCommand[1] + "\n";
                    
                    
                    serverAnswer = udpConnection(request.getBytes(),address, port, udpSocket);
                    
                    if (serverAnswer == null) {
                        System.out.println("\nERROR: Could not connect to ECP Server.\n");
                        System.exit(-1);
                    }
                    
                    if(!serverAnswer[0].equals("AWTES") && !serverAnswer[0].equals("EOF") && !serverAnswer[0].equals("ERR")) {
                        System.err.println("\nERROR: Unknown server answer: "+serverAnswer[0]+"\nAborting Connection.\n");
                        continue;
                    }
                    
                    else {
                        
                        if (serverAnswer[0].equals("ERR")) {
                            System.err.println("\nERR: Incorrect TER formulation.\n");
                            continue;
                        }
                        
                        tesIP = InetAddress.getByName(serverAnswer[1]); 

                        try {
                            tesPort = Integer.parseInt(serverAnswer[2].trim());
                        } 
                        catch (NumberFormatException exception) {
                            System.err.println("\nERROR: "+serverAnswer[2]+" port must be an Integer.\n");
                            System.out.println(exception.getMessage());
                        }
                        try {
                            tcpSocket = new Socket(tesIP,tesPort);
                            
                        } 
                        catch (IOException ex) {
                            System.err.println("\nERROR: Unable to establish TCP connection with the TES server.\n");
                            continue;
                        }
                        
                        System.out.println("\n"+serverAnswer[1] + ":" + tesPort+"\n");
                        
                        QID = receiveFile(tcpSocket,SID);
                        tcpSocket.close();
                        if(QID == null) {
                            System.exit(-1);
                        }
                        
                        
                    }
                    
                }
                
                //--------------------------------------------------------------------
                //Command "submit"----------------------------------------------------
                //--------------------------------------------------------------------
                
                if(parsedCommand.length == 6 && parsedCommand[0].equals("submit")) {
                    
                    
                    //QID validation
                    if (QID == null) {
                        System.out.println("\nERROR: You must first request a questionnaire.\n");
                        continue;
                    }
                    
                    //TCP Socket creation
                    try {
                        tcpSocket = new Socket(tesIP,tesPort);
                    } 
                    catch (IOException ex) {
                        System.err.println("\nERROR: Unable to establish TCP connection with the TES server.\n");
                        continue;
                    }
                    

                    String[] answerSequence = new String[5];
                    for(int i = 0; i < 5; i++) {
                        answerSequence[i] = parsedCommand[i+1];
                    }
                    
                    
                    
                    String submit = "RQS " + SID + " " + QID;
                    for(int i = 0; i < answerSequence.length; i++) {
                        submit += " " + answerSequence[i];
                    }
                    submit += "\n";
                    
                    if (!validatedAnswer(answerSequence)) {
                        System.out.println("\nERROR: Answers must take values of A,B,C or D. Please use 'N' to indicate absence of reply.\n");
                        continue;
                    }
                    //System.out.println("enviei: "+submit);
                    int score = getScore(submit, tcpSocket);
                    if (score >= 0) 
                        System.out.println("Obtained score: " + score + "%");
                    else if (score == -1)
                        System.out.println("\nDeadline to answer the questionnaire was exceeded.\n");
                    else if (score == -2)
                        System.out.println("\nERROR: SID and QID do not match.\n");
                    else {
                        System.err.println("\nERROR: Something went wrong while fetching the score.\n");
                    }
                        
                    tcpSocket.close();
                    
                    
                }
                
                ///--------------------------------------------------------------------
                //Command "exit"-------------------------------------------------------
                //---------------------------------------------------------------------

                if (parsedCommand.length == 1 && parsedCommand[0].equals("exit")) {
                    _stdin.close();
                    System.exit(1);
                }
                
                //--------------------------------------------------------------------
                
            }
            
            catch(IOException io){
                System.err.println("\nInput/Output stream error.\n");
            }
            
        }


    }
}
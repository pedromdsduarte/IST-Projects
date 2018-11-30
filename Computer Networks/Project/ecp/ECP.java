import java.io.*;
import java.net.*;

class ECP {
    
    //UDP+File atributes
    static int PORT = 58015;
    static DatagramSocket serverSocket;
    static byte[] receiveData = new byte[1024];
    static byte[] sendData = new byte[1024];
    static BufferedReader in;
    static String line;
    static String scoresFile = "scores.txt";
    
    /**
     * readTopics()
     * Reads the content of the topics.txt (the topics that are on the file)
     */
    public static String readTopics(){
        try{
            String tempString="";
            in = new BufferedReader(new FileReader("topics.txt"));
            int count = 0;
            while((line= in.readLine()) != null){
                String[] topicArray = line.split("\\s");
                count++;
                tempString+=(" "+topicArray[0]);
            }
            tempString =  "AWT "+count + tempString + "\n";
            in.close();
            return tempString;
        }catch(IOException io){
            System.err.println("Something went wrong with topics.txt... \nServer is closing!");
            System.exit(-1);
        }
        return null;
    }
    
    /**
     * getTopicAddress(String index)
     * Reads the content of topics.txt and gets the address and port of the TES
     * server
     * @param index is the topic requested by user
     */
     public static String getTopicAddress(String index){
        try{
            String tempString="";
            in = new BufferedReader(new FileReader("topics.txt"));
            for(int i=0; i<Integer.parseInt(index); i++){
                line = in.readLine();
                if(line!=null){
                String[] topicInfo = line.split("\\s");
                tempString="AWTES "+topicInfo[1]+" "+topicInfo[2];
                }else{ break;}
            }
            tempString+="\n";
            in.close();
            return tempString;
        }catch(IOException io){
            System.err.println("Something went wrong with topics.txt... \nServer is closing!");
            System.exit(-1);     
        }
        return null;
     }
//------------------------------------------------------------------------------	

    public static void main(String[] args){
        
//------------------------------------------------------------------------------
//User Application Invocation error test zone-----------------------------------
//------------------------------------------------------------------------------


//------If number os arguments greater than 2 kill process----------------------
        if ( args.length > 2) {
                System.err.println("The program must be invoked as follows:"
                            + " java ECP [-p ECPport]\n"
                            + " [-p ECPport] is an optional flag.");
                System.exit(-1);
        }


//------If arguments type  "java ECP XXXXX"-------------------------------------
        /*if(args.length==1){
            try{
                PORT = Integer.parseInt(args[0]);
                }catch(NumberFormatException exception) {
                    System.err.println("The port number must be an Integer.");
                    System.exit(-1);
                }
        }
        */
        
        
//------If arguments type "java ECP -p XXXXX"-----------------------------------   
        else if(args.length==2){
            if(args[0].equals("-p")){
                try{
                    PORT = Integer.parseInt(args[1]);
                }catch(NumberFormatException exception) {
                System.err.println("The port number must be an Integer.");
                System.exit(-1);
            }}else{
                System.err.println("The flag must be '-p'.");
                System.exit(-1);
            }
        }
        
        
        else if (args.length == 1){
            System.err.println("The program must be invoked as follows:"
                            + " java ECP [-p ECPport]\n"
                            + " [-p ECPport] is an optional flag.");
            System.exit(-1);
        }
        
        
//------------------------------------------------------------------------------
//Connection Setup--------------------------------------------------------------
//------------------------------------------------------------------------------
        try{
            serverSocket = new DatagramSocket(PORT);
            System.out.println("Running on port "+ PORT);
        }catch(SocketException sE){
                System.err.println("Unable to open the socket or to bind socket "
                + "to specified port");
                System.exit(-1);
        }
        
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
        
        while(true){
            try{
                receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
                serverSocket.receive(receivePacket);
                
                //saving ip and port from client
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                
                //creating a response
                String tempString = new String(receivePacket.getData());
                //System.out.println("RECEIVED: "+tempString+ "\t\t\tFROM: "+ receivePacket.getAddress().getHostAddress()+":"+ receivePacket.getPort());
                
                //completar com a ligação UDP ao TES??
                String[] requestedArray = tempString.trim().split(" ");
                
                if (requestedArray[0].equals(" \n")){
                    System.err.println("ERROR: no request");
                    
                }else{
                    
                    if(requestedArray[0].equals("TQR") && requestedArray.length<3){
                        //  CODIGO 'TQR' LISTAR
                        System.out.println("TQR request was sent by "+ receivePacket.getAddress().getHostAddress()+":"+ receivePacket.getPort());
                        tempString = readTopics();
                        
                        
                    }else if(requestedArray[0].equals("TER") && requestedArray.length<4){
                        //  CODIGO 'TER' ENVIAR INFO PARA CLIENTE
                        System.out.println("TER request was sent by user with topic number "+requestedArray[1]);
                        tempString= getTopicAddress(requestedArray[1]);
                        
                    }else if(requestedArray[0].equals("IQR") && requestedArray.length<7){
                        // CODIGO 'IQR' RECEBER SCORE DO TES
                        System.out.println(requestedArray[1]+", "+requestedArray[3]+", "+requestedArray[4]+"%");
                        
                        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(scoresFile, true));
                        fileWriter.write(requestedArray[1] + " " + requestedArray[2] + " "+ requestedArray[3] + " "+
                                            requestedArray[4] + "% \n");
                        fileWriter.close();
                        
                        tempString="AWI "+requestedArray[2]+"\n";
                    }
                }
                sendData = tempString.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
                tempString = "";
        
                
            
            }catch(IOException io){
                System.err.println("\nInput/Output stream error.\n");
            }
        }
    }
}

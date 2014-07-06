package Sender;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.StringTokenizer;

/**
 *
 * @author nikhil pankil
 */
public class Client
{  
	
	public static String file;
	public static int MSS_size;
    private static byte[] buffer;
   // private static int port = 8550;
   // private DatagramSocket socket;
    private static BufferedReader stdin;
    private  StringTokenizer userInput;
  //  private DatagramPacket initPacket, packet1,packet2,packet3,packet4,packet5;
    public static int global=0,global1=0,global2=0,global3=0,global4=0,global5=0;
    
    
  
    public static void main(String[] args) throws IOException
    { 
    	int serverID = 0;
        DatagramSocket socket1 = new DatagramSocket();
        DatagramSocket socket2= new DatagramSocket();// any random socket created for connecting
        DatagramSocket socket3 = new DatagramSocket();
        DatagramSocket socket4 = new DatagramSocket();
        DatagramSocket socket5 = new DatagramSocket();  
    //    InetAddress address = InetAddress.getByName("localhost");
      
        DatagramPacket packet1,packet2,packet3,packet4,packet5;
        


        stdin = new BufferedReader(new InputStreamReader(System.in));
        
        String selectedAction = selectAction();
        String[] inputsplit = selectedAction.split(" ");
        InetAddress s1 = InetAddress.getByName(inputsplit[1]);
    /*    InetAddress s2 = InetAddress.getByName(inputsplit[2]);
        InetAddress s3 = InetAddress.getByName(inputsplit[3]);
        InetAddress s4 = InetAddress.getByName(inputsplit[4]);
        InetAddress s5 = InetAddress.getByName(inputsplit[5]);
        int port=Integer.parseInt(inputsplit[6]);
        file=inputsplit[7];
        MSS_size=Integer.parseInt(inputsplit[8]);   */
        //MM: multiple ports for 3
    //   int port1=Integer.parseInt(inputsplit[9]);
    //    int port2=Integer.parseInt(inputsplit[10]);
        //MM:
        int port=Integer.parseInt(inputsplit[2]);
        file=inputsplit[3];
        MSS_size=Integer.parseInt(inputsplit[4]);
        buffer = new byte[MSS_size];
        
   
        
        byte[] bytes=(selectedAction).getBytes();
        int len=(selectedAction).getBytes().length;
        int actionport=port;
   //     userInput = new StringTokenizer(selectedAction);
      //  System.out.println(userInput.nextToken());
        try
        {
 /*  note       switch (messageType.valueOf(userInput.nextToken()))
            {     
                case put:                               note    */
        	        
                    
                    Thread t1=new Thread(new SERVERConnection(socket1, file, buffer, ++serverID , bytes, len, s1, actionport));
                 //   System.out.println("Server id: "+serverID +" IP ADDRESS"+s1);
                    t1.start();
                 //   System.out.println(serverID);
     /*             
             //     packet2= new DatagramPacket((selectedAction).getBytes(), (selectedAction).getBytes().length, s2, port );
              //    socket.send(packet2);
                     Thread t2=new Thread(new SERVERConnection(socket2,file, buffer, ++serverID , bytes, len, s2, actionport));
             //        System.out.println("Server id: "+serverID +" IP ADDRESS"+s2);   
                     t2.start();
             //        System.out.println(serverID);
                
                    
                   //MM: multiple ports for 3   
                    Thread t3= new Thread(new SERVERConnection(socket3, file, buffer, ++serverID , bytes, len, s3, actionport));
             //       System.out.println("Server id: "+serverID +" IP ADDRESS"+s3);   
                    t3.start();
             //       System.out.println(serverID);
                  //MM:
                  
                    Thread t4= new Thread(new SERVERConnection(socket4, file, buffer, ++serverID , bytes, len, s4, actionport));
              //      System.out.println("Server id: "+serverID +" IP ADDRESS"+s4);   
                    t4.start();
              //      System.out.println(serverID);
                    
                    
                    Thread t5= new Thread(new SERVERConnection(socket5, file, buffer, ++serverID , bytes, len, s5, actionport));
               //     System.out.println("Server id: "+serverID +" IP ADDRESS"+s5);   
                    t5.start();
              //      System.out.println(serverID);

            /* code for threaded class        
                    File theFile = new File(file);

                    initPacket = receivePacket();

                    //create object to handle out going file
                    UDPFileSender fileHandler = new UDPFileSender(socket, initPacket);
                    fileHandler.sendFile(theFile);
                    
 end of code for threaded class  */
                    
                    
         //           break;


/* note               case get:                                             

        			packet = new DatagramPacket((selectedAction).getBytes(), (selectedAction).getBytes().length, s1, 7735);
                    socket.send(packet);

                    initPacket = receivePacket();

                    socket.send(initPacket);

                    //create Object to handle incoming file
                    new UDPFileReceiver(socket);
                 //note   break;
         }
   
     note      */ 
        } catch (Exception e)
        {
            System.err.println("not valid input");
        }
   //  System.out.println("Connection Closed");
  //   socket.close();
    }
/* code for threaded class 
    private static DatagramPacket receivePacket() throws IOException
    {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return packet;
    }
end of code for threaded class  */
    
 // for 3 servers
    public static String selectAction() throws IOException
    {
        System.out.println("COMMANDS: p2mpclient *s1* *s2* *s3* *portno* *filename* *MSS_size*");
        System.out.println("\t  example: p2mpclient 192.168.1.1 192.168.1.2 192.168.1.3 7735 data.txt 512");
        System.out.print("udp> ");

        return stdin.readLine();
    }
 /* for 5 servers
     public static String selectAction() throws IOException
    {
        System.out.println("COMMANDS: p2mp client *s1* *s2* *s3* *s4* *s5* *portno* *filename* *MSS_size*");
        System.out.println("\t  example: p2mp client 192.168.1.1 192.168.1.2 192.168.1.3 192.168.45.12 192.168.154.12 7735 data.txt 512");
        System.out.print("udp> ");

        return stdin.readLine();
    }
 */  
    
  //synchronized methods for incrementing global variables
    public static synchronized void incrementglobal1()
    {
    	global1= global1+1;
    }
    
    public static synchronized void incrementglobal2()
    {
    	global2= global2+1;
    }
    public static synchronized void incrementglobal3()
    {
    	global3= global3+1;
    }
    public static synchronized void incrementglobal4()
    {
    	global4= global4+1;
    }
    public static synchronized void incrementglobal5()
    {
    	global5= global5+1;
    }
    
   
    
    
    
 //synchronized methods for returning values of global variables  
    public static synchronized int valglobal1()
    {
    	return global1;
    }
    public static synchronized int valglobal2()
    {
    	return global2;
    }
    public static synchronized int valglobal3()
    {
    	return global3;
    }
    public static synchronized int valglobal4()
    {
    	return global4;
    }
    public static synchronized int valglobal5()
    {
    	return global5;
    }    
}
package Sender;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.io.File;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;

import Global.Message;

public class SERVERConnection implements Runnable
{ 	
    DatagramPacket initPacket,packet;
    String file;
	DatagramSocket serverSocket;
	byte[] bytes;
	int len;
	InetAddress s;
	int port;
    byte[] buffer;
    int serverID;
	   
    private int segmentID;
    private int reSendCount;
    private byte[] msg,msg1;
    private FileInputStream fileReader;
    private DatagramSocket datagramSocket;
    private int fileLength, currentPos, bytesRead;
    private final int packetOverhead = 106; 
    Long start=0l, end=0l;
    
    PrintWriter tempwriter;
        
    public SERVERConnection(DatagramSocket serverSocket, String file, byte[] buffer, int serverID, byte[] bytes, int len, InetAddress s, int port) throws IOException
    {
        this.serverSocket = serverSocket;
       // this.packet = packet;
        this.file=file;
        this.buffer=buffer;
        this.serverID = serverID;
        this.bytes=bytes;
        this.len=len;
        this.s=s;
        this.port=port;
    }
@Override
public void run()
{
try
{
	
	System.out.println("Inside thread");
	packet = new DatagramPacket(bytes, len, s, port );
    serverSocket.send(packet);
	 File theFile = new File(file);

     initPacket = receivePacket();
     initialization(serverSocket, initPacket);
     sendFile(theFile);
     //create object to handle out going file
    // UDPFileSender fileHandler = new UDPFileSender(serverSocket, initPacket);
    // fileHandler.sendFile(theFile);
}
catch(Exception e)
{
	e.printStackTrace();
}
}

private DatagramPacket receivePacket() throws IOException
{
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
    serverSocket.receive(packet);
    return packet;
}
public void initialization(DatagramSocket socket, DatagramPacket initPacket) throws IOException
{
    msg1 = new byte[Client.MSS_size];
    buffer = new byte[Client.MSS_size];
    datagramSocket = socket;
    System.out.println("In UDPFILESENDER");
    //setup DatagramSocket with correct Inetaddress and port of receiver
    datagramSocket.connect(initPacket.getAddress(), initPacket.getPort());
    segmentID = 0;
}
public void sendFile(File theFile) throws IOException
{
	System.out.println("Server ID :"+serverID);
	System.out.println("In send file:UDPSENDER");
    fileReader = new FileInputStream("C:\\"+Client.file);
    fileLength = fileReader.available();

    System.out.println("*** Filename: " + theFile.getName() + " ***");
    System.out.println("*** Bytes to send: " + fileLength + " ***");

    send((theFile.getName() + "::" + fileLength).getBytes());

    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
    datagramSocket.receive(reply);
    
    //waits for receiver to indicate OK to send
    if (new String(reply.getData(), 0, reply.getLength()).equals("OK"))
    {
        System.out.println("*** Got OK from receiver - sending the file ***");

        //outer while to control when send operation complete
        //inner while to control ACK messages from receiver
        //one=2nd condition of while for 1 server
        //two=2nd condition of while for 2 servers
        // three=2nd condition of while for 3 servers
        // five=2nd condition of while for 5 servers
        // boolean = true;
   //     boolean two=((Client.valglobal1()==Client.valglobal2())||((serverID==1)&&(Client.valglobal1()==0))||((serverID==2)&&(Client.valglobal2()==0))||((serverID==1)&&(Client.valglobal2()==(Client.valglobal1()+1)))||((serverID==2)&&(Client.valglobal1()==(Client.valglobal2()+1))));
    //   boolean three=(((Client.valglobal1()==Client.valglobal2())&&(Client.valglobal2()==Client.valglobal3()))||(((serverID==1)&&(Client.valglobal1()==0))||((serverID==2)&&(Client.valglobal2()==0))||((serverID==3)&&(Client.valglobal3()==0)))||(((serverID==1)&&((Client.valglobal2()==Client.valglobal1()+1)||Client.valglobal3()==Client.valglobal1()+1))||((serverID==2)&&((Client.valglobal1()==Client.valglobal2()+1)||Client.valglobal3()==Client.valglobal2()+1))||((serverID==3)&&((Client.valglobal1()==Client.valglobal3()+1)||Client.valglobal2()==Client.valglobal3()+1))));
     
       // computing for five
 /*       boolean part1= ((Client.valglobal1()==Client.valglobal2())&&(Client.valglobal2()==Client.valglobal3())&&(Client.valglobal3()==Client.valglobal4())&&(Client.valglobal4()==Client.valglobal5()));
        
        boolean part2= (((serverID==1)&&(Client.valglobal1()==0))||((serverID==2)&&(Client.valglobal2()==0))||((serverID==3)&&(Client.valglobal3()==0))||((serverID==4)&&(Client.valglobal4()==0))||((serverID==5)&&(Client.valglobal5()==0)));
        
        boolean part31= (((serverID==1)&&((Client.valglobal2()==Client.valglobal1()+1)||(Client.valglobal3()==Client.valglobal1()+1))||(Client.valglobal4()==Client.valglobal1()+1)||(Client.valglobal5()==Client.valglobal1()+1)));
        boolean part32=	(((serverID==2)&&((Client.valglobal1()==Client.valglobal2()+1)||(Client.valglobal3()==Client.valglobal2()+1))||(Client.valglobal4()==Client.valglobal2()+1)||(Client.valglobal5()==Client.valglobal2()+1)));	
        boolean part33= (((serverID==3)&&((Client.valglobal1()==Client.valglobal3()+1)||(Client.valglobal2()==Client.valglobal3()+1))||(Client.valglobal4()==Client.valglobal3()+1)||(Client.valglobal5()==Client.valglobal3()+1)));
        boolean part34= (((serverID==4)&&((Client.valglobal1()==Client.valglobal4()+1)||(Client.valglobal2()==Client.valglobal4()+1))||(Client.valglobal3()==Client.valglobal4()+1)||(Client.valglobal5()==Client.valglobal4()+1)));
        boolean part35= (((serverID==5)&&((Client.valglobal1()==Client.valglobal5()+1)||(Client.valglobal2()==Client.valglobal5()+1))||(Client.valglobal3()==Client.valglobal5()+1)||(Client.valglobal4()==Client.valglobal5()+1)));
        
       
        
        boolean five=(((Client.valglobal1()==Client.valglobal2())&&(Client.valglobal2()==Client.valglobal3())&&(Client.valglobal3()==Client.valglobal4())&&(Client.valglobal4()==Client.valglobal5()))||(((serverID==1)&&(Client.valglobal1()==0))||((serverID==2)&&(Client.valglobal2()==0))||((serverID==3)&&(Client.valglobal3()==0))||((serverID==4)&&(Client.valglobal4()==0))||((serverID==5)&&(Client.valglobal5()==0)))||((((serverID==1)&&((Client.valglobal2()==Client.valglobal1()+1)||(Client.valglobal3()==Client.valglobal1()+1))||(Client.valglobal4()==Client.valglobal1()+1)||(Client.valglobal5()==Client.valglobal1()+1)))||(((serverID==2)&&((Client.valglobal1()==Client.valglobal2()+1)||(Client.valglobal3()==Client.valglobal2()+1))||(Client.valglobal4()==Client.valglobal2()+1)||(Client.valglobal5()==Client.valglobal2()+1)))||(((serverID==3)&&((Client.valglobal1()==Client.valglobal3()+1)||(Client.valglobal2()==Client.valglobal3()+1))||(Client.valglobal4()==Client.valglobal3()+1)||(Client.valglobal5()==Client.valglobal3()+1)))||(((serverID==4)&&((Client.valglobal1()==Client.valglobal4()+1)||(Client.valglobal2()==Client.valglobal4()+1))||(Client.valglobal3()==Client.valglobal4()+1)||(Client.valglobal5()==Client.valglobal4()+1)))||(((serverID==5)&&((Client.valglobal1()==Client.valglobal5()+1)||(Client.valglobal2()==Client.valglobal5()+1))||(Client.valglobal3()==Client.valglobal5()+1)||(Client.valglobal4()==Client.valglobal5()+1))))) ;
   */   
        start = System.currentTimeMillis();
        while(currentPos < fileLength)
        {
        	while(((currentPos < fileLength)&&true))   // change the 2nd condition for different number of receivers
     //   while ((currentPos < fileLength)&&(((Client.valglobal1()==Client.valglobal2())&&(Client.valglobal2()==Client.valglobal3()))||(((serverID==1)&&(Client.valglobal1()==0))||((serverID==2)&&(Client.valglobal2()==0))||((serverID==3)&&(Client.valglobal3()==0)))||(((serverID==1)&&((Client.valglobal2()==Client.valglobal1()+1)||Client.valglobal3()==Client.valglobal1()+1))||((serverID==2)&&((Client.valglobal1()==Client.valglobal2()+1)||Client.valglobal3()==Client.valglobal2()+1))||((serverID==3)&&((Client.valglobal1()==Client.valglobal3()+1)||Client.valglobal2()==Client.valglobal3()+1))))); // change the 2nd condition of while according to number of servers
    //   while(true)
        	{
       // 	System.out.println("Current server id :"+this.serverID);
        	
        		 bytesRead = fileReader.read(msg1);
                 
                 SenderChecksum ch = new SenderChecksum();
                 
         		short check=ch.SenderChecksum(Client.MSS_size, msg1);
         		SenderHeader h=new SenderHeader();
         		byte [] msg;
         		
         		msg=h.SenderHeader(check,segmentID,msg1);
         		int msglen = msg.length;
                 Message message = new Message(segmentID, msg, msglen);
                 System.out.println("Sending segment " + message.getSegmentID() + " with " + bytesRead + " byte payload.");
                 byte[] test = serialize(message);
          //  System.out.println(test.length);
            send(test, test.length);
            currentPos = currentPos + bytesRead;
            
            //handle ACK of sent message object, timeout of 2 seconds. If segementID ACK is not received
            //resend segment.
            datagramSocket.setSoTimeout(200); 
            boolean receiveACK = false;
            while (!receiveACK)
            {
                try
                {
                    datagramSocket.receive(reply);
                    if(this.serverID==1)
                    {
                    Client.incrementglobal1();
               //     System.out.println("Client.valglobal1() "+Client.valglobal1());
               //     System.out.println("Client.valglobal2() "+Client.valglobal2());
                    }
                    if(this.serverID==2)
                    {
                    Client.incrementglobal2();
              //     System.out.println("Client.valglobal1() "+Client.valglobal1());
              //    System.out.println("Client.valglobal2() "+Client.valglobal2());
                    } 
                   if(this.serverID==3)
                    {
                    Client.incrementglobal3();
                 //  System.out.println("Client.valglobal1() "+Client.valglobal1());
                 //  System.out.println("Client.valglobal2() "+Client.valglobal2());
                    } 
                   
                 //  three=(((Client.valglobal1()==Client.valglobal2())&&(Client.valglobal2()==Client.valglobal3()))||(((serverID==1)&&(Client.valglobal1()==0))||((serverID==2)&&(Client.valglobal2()==0))||((serverID==3)&&(Client.valglobal3()==0)))||(((serverID==1)&&((Client.valglobal2()==Client.valglobal1()+1)||Client.valglobal3()==Client.valglobal1()+1))||((serverID==2)&&((Client.valglobal1()==Client.valglobal2()+1)||Client.valglobal3()==Client.valglobal2()+1))||((serverID==3)&&((Client.valglobal1()==Client.valglobal3()+1)||Client.valglobal2()==Client.valglobal3()+1))));
                   
                   if(this.serverID==4)
                    {
                    Client.incrementglobal4();
                 //  System.out.println("Client.valglobal1() "+Client.valglobal1());
                 //  System.out.println("Client.valglobal2() "+Client.valglobal2());
                    } 
                    if(this.serverID==5)
                    {
                    Client.incrementglobal5();
                 //  System.out.println("Client.valglobal1() "+Client.valglobal1());
                 //  System.out.println("Client.valglobal2() "+Client.valglobal2());
                    } 
                    
                } catch (SocketTimeoutException e)
                {
                    send(test,msglen);
                    System.out.println("Timeout, sequence number : "+message.getSegmentID());
                    System.out.println("*** Sending sequence number " + message.getSegmentID() + " with " + bytesRead + " payload again. ***");
                    reSendCount++;                   
                }
                if (new String(reply.getData(), 0, reply.getLength()).equals(Integer.toString(message.getSegmentID())))
                {
                    System.out.println("Received ACK to sequence number " + new String(reply.getData(), 0, reply.getLength()));
                    segmentID++;
                    receiveACK = true;
                }
            }
        } 
        }//end of while for sending the whole file
        end= System.currentTimeMillis();
        System.out.println("*** File transfer complete...");
        System.out.println(reSendCount + " packets had to be resent. ***");
        System.out.println("Total time to send the file to all the receivers : "+(end-start)+" milliseconds");
 /*     tempwriter = new PrintWriter(new BufferedWriter(new FileWriter("C:\\record.txt", true)));  // writing the time required output to the file record.txt which is created by us and not by system
        tempwriter.println(end-start);       
        tempwriter.close();
        System.out.println("Wrote the time required in the file record.txt");    */
    } else
    {
        System.out.println("Recieved something other than OK... exiting");
    }
}

private void send(byte[] message, int length) throws IOException
{
    DatagramPacket packet = new DatagramPacket(message, length);
    datagramSocket.send(packet);
}

private void send(byte[] message) throws IOException
{
    DatagramPacket packet = new DatagramPacket(message, message.length);
    datagramSocket.send(packet);
}

public byte[] serialize(Object obj) throws IOException
{
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
    objectStream.writeObject(obj);
    objectStream.flush();
    return byteStream.toByteArray();
}

}


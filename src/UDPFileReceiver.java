package Receiver;

import java.io.*;
import java.net.*;
import java.util.*;

import Global.Message;

public class UDPFileReceiver
{
	String input[];
    private byte[] buffer;
    int MSS_size;
    private Message receiveMSG,receiveMSG1;
    private DatagramSocket socket;
    private String filename, initString;
    private FileOutputStream fileWriter;
    private DatagramPacket initPacket, receivedPacket;
    private int bytesReceived, bytesToReceive, simulateBadConnection, expectedSegmentID;
    private final boolean simulateMessageFail = true;//true if you want to simulate a bad connection
    private final boolean simulateACKFail = false;
    private String dest_filename;
    private double p;
    private static String ACK;
    BufferedReader inputfromUser=new BufferedReader(new InputStreamReader(System.in));
    public UDPFileReceiver(DatagramSocket socket, String dest_filename, double probability) throws IOException
    {
    //	System.out.println("In UDPFILERECEIVER");
        this.socket = socket;
        this.dest_filename=dest_filename;
        p=probability;
        buffer=new byte[65507]; //65507 is the max size of UDP for Windows
        
        System.out.println("*** Ready to receive file on port: " + socket.getLocalPort() + " ***");
        
        initPacket = receivePacket();
        initString = "Recieved-" + new String(initPacket.getData(), 0, initPacket.getLength());
 //       System.out.println("initstring"+initString);
        
        //get the file name and byte size of file name
        StringTokenizer t = new StringTokenizer(initString, "::");
        filename = t.nextToken();
 //       System.out.println("filename:"+filename);
        bytesToReceive = new Integer(t.nextToken()).intValue();
        
        // User enter the loss probability
      //  System.out.println("Enter the loss probability");
      //  double p=Double.parseDouble(inputfromUser.readLine());
        
        System.out.println("*** The file will be saved as: " + dest_filename + " ***");
        System.out.println("*** Expecting to receive: " + bytesToReceive + " bytes ***");
 
        //tell the sender OK to send data
        send(initPacket.getAddress(), initPacket.getPort(), ("OK").getBytes());

        fileWriter = new FileOutputStream(filename);

        //two while loops. First checks that there is still more data to receive
        //and inner do/while is to error check on received packets and catch missing ACK
        //sent to sender
        while (bytesReceived < bytesToReceive)
        {
            receiveMSG = new Message();
            receiveMSG1 = new Message();
            do
            {
                receivedPacket = receivePacket();
               try{ deserialize(receivedPacket.getData());
               
               
               
               
               ReceiverHeader h= new ReceiverHeader();
               byte[] mmm=h.ReceiverHeader(receiveMSG1.getPacket());
             
               
               receiveMSG= new Message(receiveMSG1.getSegmentID(),mmm,mmm.length);
               MSS_size=receiveMSG.getPacket().length;
                    // receiveMSG is updated in the method deserialize, hence we take MSS_size after invoking deserialize
               }
               catch(Exception e){}  
               
           /*    try
                {
                  receiveMSG = (Message) deserialize(receivedPacket.getData());
                } catch (ClassNotFoundException ex)
                {
                    System.out.println("*** Message packet failed. ***");
                }
                
        */

                //logically if the last ACK sent fails to be received the UDPSender will resend the last segment.
                //A simple check on the segemntID will catch this as it will be equal to expectedID - 1. Resending the ACK
                //for this previous segment will eventually get through to the server to send the next expected segment.
                if ((expectedSegmentID - 1) == receiveMSG.getSegmentID())
                {
                	
                    ACK = Integer.toString(receiveMSG.getSegmentID());
                    send(initPacket.getAddress(), initPacket.getPort(), (ACK).getBytes());
                    System.out.println("*** Resending ACK for segment " + ACK + " ***");
                }
                if (simulateMessageFail)
                {
                    simulateBadConnection = (Math.random() < p) ? 1 : 0 ; //simulate a p% chance a message object is lost
                    if(simulateBadConnection==1)
                    {
                    	System.out.println("Packet loss, sequence number : "+ Integer.toString(receiveMSG.getSegmentID()));
                    }
                }

                //by adding 1 to segmentIDExpected we can make the receiver determine a message object is lost
                //as the server has a 2 second timeout before it will resend we can check the error control in this way.
            } while (receiveMSG.getSegmentID() != (expectedSegmentID + simulateBadConnection));

            expectedSegmentID++;

            //handles the last byte segmentID size .getBytesToWrite()
          fileWriter.write(receiveMSG.getPacket(), 0, receiveMSG.getBytesToWrite());

            System.out.println("Received sequence number " + receiveMSG.getSegmentID());

            //adding payload size for outer while condition
            bytesReceived = bytesReceived + MSS_size;

           //simulate a p% chance an ACK is lost
            if (simulateACKFail)
            {
                if ((Math.random() < p))
                {
                    String ACK = Integer.toString(receiveMSG.getSegmentID());
                    send(initPacket.getAddress(), initPacket.getPort(), (ACK).getBytes());
                } else
                {
                    System.out.println("*** failed to send ACK ***");
                }
            } else
            {
                String ACK = Integer.toString(receiveMSG.getSegmentID());
                send(initPacket.getAddress(), initPacket.getPort(), (ACK).getBytes());
            }
        }
        System.out.println("*** File transfer complete. ***");
        fileWriter.close();
    }

    private DatagramPacket receivePacket() throws IOException
    {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
  //      System.out.println("Waiting for packet");
        socket.receive(packet);
  //      System.out.println("Got packet");
        return packet;
    }

    private void send(InetAddress recv, int port, byte[] message) throws IOException
    {
        DatagramPacket packet = new DatagramPacket(message, message.length, recv, port);
        socket.send(packet);
    }
/*
    private Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
        return (Message) objectStream.readObject();
        
    }
 
   */
    private void deserialize(byte[] bytes) throws IOException, ClassNotFoundException
    {
    	try
    	{    	
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
        receiveMSG1= (Message) objectStream.readObject();
        }
    	catch(Exception e)
        {
        	e.printStackTrace();
        }
    	
    }   
    
}
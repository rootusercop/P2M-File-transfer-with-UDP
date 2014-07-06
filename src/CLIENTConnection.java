package Receiver;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author nikhil pankil
 */
public class CLIENTConnection implements Runnable
{
    String file;
    public int MSS_size;
    private int clientID;
    private byte[] buffer;
    private int bytesToReceive;
    private DatagramSocket clientSocket;
    private DatagramPacket packet, initPacket;
    private String userInput, filename, initString;
    private String dest_filename;
    private double probability;

    public CLIENTConnection(DatagramSocket clientSocket, DatagramPacket packet, int clientID, String dest_filename, double probability) throws IOException
    {
        this.clientSocket = clientSocket;
        this.packet = packet;
        this.clientID = clientID;
        this.dest_filename=dest_filename;
        this.probability=probability;
    }

    @Override
    public void run()
    {
        try
        {
          //  buffer = new byte[618];
        	
            System.out.println("THREAD: " + new String(packet.getData(), 0, packet.getLength()));

            initString = new String(packet.getData(), 0, packet.getLength());

  /* note note        StringTokenizer t = new StringTokenizer(initString);
        
           userInput = t.nextToken();
            filename = t.nextToken();           
            if (t.hasMoreTokens())                    
            {
                bytesToReceive = new Integer(t.nextToken()).intValue();
            }
 note note */           
            String[] inputsplit = initString.split(" ");
        //    file=inputsplit[6];
       //     bytesToReceive=Integer.parseInt(inputsplit[7]);   //bytestoreceive is same as mss size
            
            buffer = new byte[MSS_size];//
/*            switch (messageType.valueOf(userInput))
            {
                case put:                                                             */
                    //sends a message gets the new port information to the client
                    send(packet.getAddress(), packet.getPort(), ("OK").getBytes());

                    //create Object to handle incoming file
                   new UDPFileReceiver(clientSocket, dest_filename, probability);
/*
                    break;
                case get:
                    File theFile = new File(filename);

                    send(packet.getAddress(), packet.getPort(), ("OK").getBytes());

                    //create object to handle out going file
                    UDPFileSender fileHandler = new UDPFileSender(clientSocket, packet);
                    fileHandler.sendFile(theFile);
                    break;
                default:
                    System.out.println("Incorrect command received.");
                    break;
            }                                                 */
        } catch (IOException ex)
        {
            Logger.getLogger(CLIENTConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("*** Transfer for client " + clientID + " complete. ***");
    }

    private void send(InetAddress recv, int port, byte[] message) throws IOException
    {
        DatagramPacket packet = new DatagramPacket(message, message.length, recv, port);
        clientSocket.send(packet);
    }

    private void send(byte[] message) throws IOException
    {
        DatagramPacket packet = new DatagramPacket(message, message.length);
        clientSocket.send(packet);
    }

/*    public enum messageType
    {
        get, put;
    }                     */
}
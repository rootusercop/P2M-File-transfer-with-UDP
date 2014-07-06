package Receiver;

import java.io.*;
import java.net.*;

public class Server
{
    private static int clientID = 1;
    private static DatagramSocket serverSocket;
    private static BufferedReader stdin;
    private static int serverport;
    private static String dest_filename;
    private static double probability;
    
    public static void main(String[] args) throws IOException
    {
    	stdin=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Server started.");
        String selectedAction = selectAction();
        String[] inputsplit = selectedAction.split(" ");
        serverport=Integer.parseInt(inputsplit[1]);
        dest_filename=inputsplit[2];
        probability=Double.parseDouble(inputsplit[3]);
        
        byte[] buffer = new byte[512];   // Have to set this size

        /*
         * PROJECT INSTRUCTION The server should be multi-threaded, and
         * have one thread per connection.
         */
        serverSocket = new DatagramSocket(serverport);
        while (true)
        {
            try
            {
                DatagramPacket packet =  new DatagramPacket(buffer, buffer.length );
                serverSocket.receive(packet);
                System.out.println("SERVER: Accepted connection.");
                System.out.println("SERVER: received "+new String(packet.getData(), 0, packet.getLength()));

                //new socket created with random port for thread
                DatagramSocket threadSocket = new DatagramSocket();

                Thread t = new Thread(new CLIENTConnection(threadSocket, packet, clientID++, dest_filename, probability));

                t.start();

            } catch (Exception e)
            {
                System.err.println("Error in connection attempt.");
            }
        }
    }
    public static String selectAction() throws IOException
    {
        System.out.println("COMMANDS: p2mpserver *portno* *filename* *loss_probability*");
        System.out.println("\t  example: p2mpserver 7735 data.txt 0.05");
        System.out.print("udp> ");

        return stdin.readLine();
    }
}

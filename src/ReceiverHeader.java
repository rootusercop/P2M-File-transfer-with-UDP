                                                                     
                                                                     
                                                                     
                                             
package Receiver;

//import Client.Checksum;

public class ReceiverHeader {
	
	
	

	
	
	public byte[] ReceiverHeader(byte [] c)
	{
	

	
	
	String sreceive = new String(c);
//	System.out.println("sreceive"+sreceive);
	String[] demux= sreceive.split("__");
//	System.out.println("demux0"+demux[0]);
	int chcheck= (Integer.parseInt((demux[0]),16));
//	System.out.println("chcheck"+chcheck);
	int seqchk = Integer.parseInt(demux[1],16);
	
	//System.out.println("checksum" + chcheck);
	//System.out.println("seqno" + seqchk);
	
	
//	System.out.println("pattern" + demux[2]);
	
	byte[] databyte = demux[3].getBytes();
	ReceiverChecksum chrec= new ReceiverChecksum();
	short checkrec1=chrec.ReceiverChecksum(databyte.length,databyte);
	
	//System.out.println("Received checksum" + (checkrec1));
/*	
	if(chcheck!=checkrec1)
	{
		System.out.println("Header is corrupted for sequence number : "+ seqchk);
	}
	
*/
	
	return databyte;
	
	}
}
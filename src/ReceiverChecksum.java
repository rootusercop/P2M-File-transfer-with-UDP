package Receiver;

public class ReceiverChecksum {

//	String s="1 ";
//	byte[] 	binarybyte=s.getBytes();	
	
//	System.out.println(binarybyte[0]);
	
//    Checksum c = new Checksum();
 //   System.out.println(binarybyte.length);
 //   long ans= c.Checksum(binarybyte.length,binarybyte);
 //   System.out.println(ans);
//}
public short ReceiverChecksum(int len_udp, byte[] buff)
{
//u16 prot_udp=17;
int padd=0;
int word16;
int sum;	



	
	// Find out if the length of data is even or odd number. If odd,
	// add a padding byte = 0 at the end of packet
//	if (padding&1==1){
	//	padd=1;
//		buff[len_udp]=0;
//	}
	
	//initialize sum to zero
	sum=0;
	
	// make 16 bit words out of every two adjacent 8 bit words and 
	// calculate the sum of all 16 vit words
	for (int i=0;i<len_udp+padd;i=i+2){
		word16 =((buff[i]<<8)&0xFF00)+(buff[i+1]&0xFF);
		sum = sum + word16;
	}	
	// add the UDP pseudo header which contains the IP source and destination addresses
/*	for (int i=0;i<4;i=i+2){
		word16 =((src_addr[i]<<8)&0xFF00)+(src_addr[i+1]&0xFF);
		sum=sum+word16;	
	}
	for (i=0;i<4;i=i+2){
		word16 =((dest_addr[i]<<8)&0xFF00)+(dest_addr[i+1]&0xFF);
		sum=sum+word16; 	
	}
	// the protocol number and the length of the UDP packet
	sum = sum + prot_udp + len_udp;
*/
	// keep only the last 16 bits of the 32 bit calculated sum and add the carries
	short sum1;
    	while ((sum >> 16)>1)
		sum = (sum & 0xFFFF)+(sum >> 16);
    //	System.out.println(sum);
		
	// Take the one's complement of sum
	sum = sum^0xFFFF;
	
	sum1=(short)sum;

	
	int sum2=~(sum1^0xFFFF);
//	System.out.println("sum"+sum);
// System.out.println("sum2"+sum2);

return ((short)sum2);
}
}
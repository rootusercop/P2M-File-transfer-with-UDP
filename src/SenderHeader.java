                                                                     
                                                                     
                                                                     
                                             
package Sender;

public class SenderHeader {
	
	/*	byte[] message=new byte[]{0001,0x4f};
		int len=message.length;
		Checksum ch = new Checksum();
		short check=ch.Checksum(len, message);
		Header h = new Header(check,1000,message);
		*/
			
	public byte[] SenderHeader(short checksum, int seqno, byte[] mssdata)
	{  // System.out.println("Sequence no: "+ seqno);
		String ccheck=Integer.toHexString(checksum & 0xffff); //need to verify the checksum length
		String sseqno=Integer.toHexString(seqno);
//		System.out.println("Sequence no: "+sseqno);
		
		String pattern="5555";
		
		String hdr = ccheck + "__" +sseqno+ "__" + pattern+ "__";
		byte[] array = hdr.getBytes();
	  //  System.out.println("hdr:"+hdr);
		byte [] c=new byte[array.length + mssdata.length];
		System.arraycopy(array,0,c,0,array.length);
		System.arraycopy(mssdata,0,c,array.length,mssdata.length);
	//	System.out.println(c);
	//	System.out.println(array.length);	
		return (c);
						
	}
}
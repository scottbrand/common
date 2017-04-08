package test.io.github.scottbrand.common.main;

import io.github.scottbrand.common.Hex;
import io.github.scottbrand.common.ServiceLocator;



public class GridArray
{

	byte[] grid = new byte[9];
	
	
	public void decode(String hexString)
	{
		grid = Hex.toByteArray(hexString);
	}
	
	
	public byte[] longToArray(long v)
	{
		byte[] b = new byte[8];
		b[0] = (byte)(v >>> 56);
		b[1] = (byte)(v >>> 48);
		b[2] = (byte)(v >>> 40);
		b[3] = (byte)(v >>> 32);
		b[4] = (byte)(v >>> 24);
		b[5] = (byte)(v >>> 16);
		b[6] = (byte)(v >>>  8);
		b[7] = (byte)(v >>>  0);
		
		return b;
	}
	
	
	public long toLong(byte[] data) {
	    if (data == null || data.length != 8) return 0x0;
	    // ----------
	    return (long)(
	            // (Below) convert to longs before shift because digits
	            //         are lost with ints beyond the 32-bit limit
	            (long)(0xff & data[0]) << 56  |
	            (long)(0xff & data[1]) << 48  |
	            (long)(0xff & data[2]) << 40  |
	            (long)(0xff & data[3]) << 32  |
	            (long)(0xff & data[4]) << 24  |
	            (long)(0xff & data[5]) << 16  |
	            (long)(0xff & data[6]) << 8   |
	            (long)(0xff & data[7]) << 0
	            );
	}
	
	public static void main(String[] args)
	{
		GridArray ga = new GridArray();
		ga.grid[0] = 15;		// From Year YY
		ga.grid[1] = 3;			// From Month MM
		ga.grid[2] = 5;			// From Day YY
		
		ga.grid[3] = 15;		// To Year YY
		ga.grid[4] = 12;		// To Month MM
		ga.grid[5] = 25;		// To Day DD
		
		ga.grid[6] = 35;		// Condition Code (Can be numeric value between 0-255)
		ga.grid[7] = (byte)199;	// Grid Code (Can be numeric value between 0-255)
		
		ga.grid[8] = (1 << 6) | 13;		// Null Indicator in high bit and dbID in between 0 and 15
		
		String asHex =  Hex.toHexString(ga.grid);
		ServiceLocator.getLogger().debug("AS HEX: " + asHex + "\n");
		
		ga.decode(asHex);
		
		System.out.format("From Date MM/DD/YY: %02d/%02d/%02d\n",ga.grid[1],ga.grid[2],ga.grid[0]);
		System.out.format("To   Date MM/DD/YY: %02d/%02d/%02d\n",ga.grid[4],ga.grid[5],ga.grid[3]);
		System.out.format("Cond Code: %02d\n",ga.grid[6] < 0 ? 256 + ga.grid[6] : ga.grid[6]);
		System.out.format("Grid Code: %02d\n",ga.grid[7] < 0 ? 256 + ga.grid[7] : ga.grid[7]);
		System.out.format("Grid DBID: %02d\n",ga.grid[8] & 0xF);
		System.out.format("Null Ind:  %1d\n" ,ga.grid[8] >> 6);
		
		long n = System.currentTimeMillis();
		byte[] b = ga.longToArray(n);
		 asHex = Hex.toHexString(b);
		ServiceLocator.getLogger().debug(n + " as hex:" + asHex);
		ServiceLocator.getLogger().debug(" back as long:" + ga.toLong(b));
		ServiceLocator.getLogger().debug(" back as long:" + ga.toLong(Hex.toByteArray(asHex)));
		
	}
	
	
	
}

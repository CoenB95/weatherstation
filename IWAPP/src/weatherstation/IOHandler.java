package weatherstation;

public class IOHandler {
	
	/** Base address of the top number-field (5 digits)*/
	public static final short NUMBER_FIELD_1 = 0x10;
	
	/** Base address of the mid-left number-field (3 digits)*/
	public static final short NUMBER_FIELD_2 = 0x20;
	
	/** Base address of the mid-right number-field (3 digits)*/
	public static final short NUMBER_FIELD_3 = 0x30;
	
	/** The value to send to a number-fields digit to appear cleared.*/
	public static final short CLEARED_NUMBER = 0x100;
	
	public IOHandler() {
		IO.init();
	}
	
	/**
	 * Writes a number to one of the three number-fields of the app.
	 * 
	 * @param address the base address (one of {@link #NUMBER_FIELD_1},
	 * {@link #NUMBER_FIELD_2} or {@link #NUMBER_FIELD_3}).
	 * @param number the decimal number you want to display.
	 * @param leadingZeros pass true to show leading zeros, false otherwise.
	 */
	public void writeNumber(short address, int number, 
			boolean leadingZeros) {
		if (number < 0) throw new IllegalArgumentException(
				"Value may not be negative, I can't handle that properly.");
		
		// Fifth number position, ten-thousands.
		if (leadingZeros || number >= 10000)
			IO.writeShort(address + 8, (short) number/10000 % 10);
		else IO.writeShort(address + 8, CLEARED_NUMBER);
		
		// Fourth number position, thousands.
		if (leadingZeros || number >= 1000)
			IO.writeShort(address + 6, (short) number/1000 % 10);
		else IO.writeShort(address + 6, CLEARED_NUMBER);
		
		// Third number position, hunderds.
		if (leadingZeros || number >= 100)
			IO.writeShort(address + 4, (short) number/100 % 10);
		else IO.writeShort(address + 4, CLEARED_NUMBER);
		
		// Second number position, tens.
		if (leadingZeros || number >= 10)
			IO.writeShort(address + 2, (short) number/10 % 10);
		else IO.writeShort(address + 2, CLEARED_NUMBER);
		
		// First number position (most right), single units.
		IO.writeShort(address + 0, number % 10);
	}
}

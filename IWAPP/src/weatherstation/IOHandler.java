package weatherstation;

public class IOHandler {

	private MatrixHandler matrixHandler;

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
		matrixHandler = new MatrixHandler();
	}

	public void clearNumber(int address) {
		IO.writeShort(address, 0x100);
	}

	public void clearNumbers(int address, int span) {
		for (int s = 0;s < span;s++) {
			clearNumber(address + (s*2));
		}
	}

	public void setSegment(int address, int segment) {
		IO.writeShort(address, IO.readShort(address) | (0x100 | (1 << segment)));
	}

	public void setSegments(int base_address, int segment, int span) {
		for (int s = 0;s < span;s++) {
			setSegment(base_address + (s*2), segment);
		}
	}

	public void clearSegment(int address, int segment) {
		IO.writeShort(address, IO.readShort(address) & ~(1 << segment));
	}

	public void clearSegments(int base_address, int segment, int span) {
		for (int s = 0;s < span;s++) {
			clearSegment(base_address + (s*2), segment);
		}
	}

	/**
	 * Writes a number to one of the three number-fields of the app.
	 * 
	 * @param address the base address (one of {@link #NUMBER_FIELD_1},
	 * {@link #NUMBER_FIELD_2} or {@link #NUMBER_FIELD_3}).
	 * @param number the decimal number you want to display.
	 * @param leadingZeros pass true to show leading zeros, false otherwise.
	 */
	public void writeNumber(int address, int number, 
			boolean leadingZeros) {
		if (number < 0) throw new IllegalArgumentException(
				"Value may not be negative, I can't handle that properly.");

		// Fifth number position, ten-thousands.
		if (leadingZeros || number >= 10000)
			IO.writeShort(address + 8, number/10000 % 10);
		else IO.writeShort(address + 8, CLEARED_NUMBER);

		// Fourth number position, thousands.
		if (leadingZeros || number >= 1000)
			IO.writeShort(address + 6, number/1000 % 10);
		else IO.writeShort(address + 6, CLEARED_NUMBER);

		// Third number position, hunderds.
		if (leadingZeros || number >= 100)
			IO.writeShort(address + 4, number/100 % 10);
		else IO.writeShort(address + 4, CLEARED_NUMBER);

		// Second number position, tens.
		if (leadingZeros || number >= 10)
			IO.writeShort(address + 2, number/10 % 10);
		else IO.writeShort(address + 2, CLEARED_NUMBER);

		// First number position (most right), single units.
		IO.writeShort(address + 0, number % 10);
	}

	public MatrixHandler getMatrixHandler() {
		return matrixHandler;
	}

	public static class MatrixHandler {

		private static final int Y_POS = 0;
		private static final int X_POS = 5;
		private static final int CODE_POS = 12;

		private static final int OFF_CODE = 0;
		private static final int ON_CODE = 1;
		private static final int SHIFT_CODE = 2;

		int row = 0;
		int index = 0;

		private MatrixHandler() {
			
		}

		public void appendText(String text) {
			for (char x:text.toCharArray()) {
				IO.writeShort(0x40, x);
				index++;
				if (x == '\n') {
					index = 0;
					row++;
				}
				if (index >= 21) {
					IO.writeShort(0x40, '\n');
					index = 0;
					row++;
				}
				if (row >= 3) {
					clearMatrix();
				}
			}
		}
		
		public void clearMatrix() {
			IO.writeShort(0x40, 0xFE);
			IO.writeShort(0x40, 0x01);
			row = 0;
			index = 0;
		}

		public void clearPixel(int x, int y) {
			IO.writeShort(0x42, (OFF_CODE << CODE_POS) |
					(x << X_POS) | (y << Y_POS));
		}

		public void drawPixel(int x, int y) {
			IO.writeShort(0x42, (ON_CODE << CODE_POS) |
					(x << X_POS) | (y << Y_POS));
		}

		public void drawLine(int x1, int y1, int x2, int y2) {
			int diff_x = x2-x1;
			int diff_y = y2-y1;
			double largest = Math.max(diff_x, diff_y);
			for (int o = 0;o < largest;o++) {
				IO.writeShort(0x42, (ON_CODE << CODE_POS) |
						((x1 + (int) (diff_x/largest*o)) << X_POS) | 
						((y1 + (int) (diff_y/largest*o)) << Y_POS));
			}
		}

		public void shiftDisplay(int x, int y) {
			IO.writeShort(0x42, (SHIFT_CODE << CODE_POS) |
					(-x << X_POS) | (-y << Y_POS));
		}
	}
}

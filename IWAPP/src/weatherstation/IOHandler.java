package weatherstation;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The IOHandler handles communication between your program
 * and the WSDisplay. This is a wrapper around the basic IO
 * class with some utility methods to ease everyday tasks.<p/>
 * In contrast to the IO class, this handler requires an object to be created.
 * This is to prevent multiple unneeded initializations.
 * @see IO
 * @author CoenB95, Dokugan
 * @version 1.0
 */
public class IOHandler {

	private MatrixHandler matrixHandler;
	
	private ButtonHandler buttonListener;
	private boolean left_on;
	private boolean right_on;
	private boolean select_on;
	
	private static boolean[][] matrixBoard = new boolean[128][32];

	/** Base address of the top number-field (5 digits)*/
	public static final short NUMBER_FIELD_1 = 0x10;

	/** Base address of the mid-left number-field (3 digits)*/
	public static final short NUMBER_FIELD_2 = 0x20;

	/** Base address of the mid-right number-field (3 digits)*/
	public static final short NUMBER_FIELD_3 = 0x30;

	/** The value to send to a number-fields digit to appear cleared.*/
	public static final short CLEARED_NUMBER = 0x100;

	/** Creates a new {@link IOHandler}*/
	public IOHandler() {	
		IO.init();
		
		matrixHandler = new MatrixHandler();
		setupButtonHandler();
	}

	/**
	 * Clears the number digit at the specified address.
	 * @param address the address of the digit on the WeatherStation.
	 */
	public void clearNumber(int address) {
		IO.writeShort(address, 0x100);
	}

	/**
	 * Clears multiple number digits of the display, starting on the base-address.
	 * @param address the base-address of the digits on the WeatherStation.
	 * @param span the number of digits that should be cleared.
	 */
	public void clearNumbers(int address, int span) {
		for (int s = 0;s < span;s++) {
			clearNumber(address + (s*2));
		}
	}

	public void setOnButtonListener(ButtonHandler b) {
		buttonListener = b;
	}
	
	/**
	 * Turns on a specific segment of a digit.
	 * @param address address of the digit on the WeatherStation.
	 * @param segment the segment number.
	 */
	public void setSegment(int address, int segment) {
		IO.writeShort(address, IO.readShort(address) | (0x100 | (1 << segment)));
	}

	/**
	 * Turns on specific segments of a digit.
	 * @param base_address address of the digit on the WeatherStation.
	 * @param segment the base segment number.
	 * @param span the number of segments.
	 */
	public void setSegments(int base_address, int segment, int span) {
		for (int s = 0;s < span;s++) {
			setSegment(base_address + (s*2), segment);
		}
	}

	private void setupButtonHandler() {
		ScheduledExecutorService service = 
				Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(() -> {
			if (buttonListener == null) return;
			if ((IO.readShort(ButtonHandler.BUTTON_LEFT) == 1) != left_on) {
				left_on = !left_on;
				buttonListener.onButtonClicked(ButtonHandler.BUTTON_LEFT);
			}
			if ((IO.readShort(ButtonHandler.BUTTON_RIGHT) == 1) != right_on) {
				right_on = !right_on;
				buttonListener.onButtonClicked(ButtonHandler.BUTTON_RIGHT);
			}
			if ((IO.readShort(ButtonHandler.BUTTON_SELECT) == 1) != select_on) {
				select_on = !select_on;
				buttonListener.onButtonClicked(ButtonHandler.BUTTON_SELECT);
			}
		}, 500, 50, TimeUnit.MILLISECONDS);
	}
	/**
	 * Turns off a specific segment of a digit.
	 * @param address address of the digit on the WeatherStation.
	 * @param segment the segment number.
	 */
	public void clearSegment(int address, int segment) {
		IO.writeShort(address, IO.readShort(address) & ~(1 << segment));
	}

	/**
	 * Turns off specific segments of a digit.
	 * @param base_address address of the digit on the WeatherStation.
	 * @param segment the base segment number.
	 * @param span the number of segments.
	 */
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
		
		//inverts the pixels of a specified area on the matrixboard
		public void MatrixInvert(short ltopx, short ltopy, short rbottomx, short rbottomy)
		{
			int length = rbottomx - ltopx;
			int width = rbottomy - ltopy;
			
			//System.out.println(IO.readShort(0x42 | 0x00));
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
			
			for (int i = 0; i < 128; i++)
			{
				for (int j = 0; j < 32; j++)
				{
					matrixBoard[i][j] = false;
				}
			}
		}

		public void clearPixel(int x, int y) {
			IO.writeShort(0x42, (OFF_CODE << CODE_POS) | (x << X_POS) | (y << Y_POS));
			System.out.println( IO.readShort(0x42 | (OFF_CODE << CODE_POS) | (x << X_POS) | (y << Y_POS)));
			matrixBoard[x][y] = false;
		}

		public void drawPixel(int x, int y) {
			IO.writeShort(0x42, (ON_CODE << CODE_POS) | (x << X_POS) | (y << Y_POS));
			System.out.println( IO.readShort(0x42 | (OFF_CODE << CODE_POS) | (x << X_POS) | (y << Y_POS)));
			matrixBoard[x][y] = true;
		}

		public void drawLine(int x1, int y1, int x2, int y2) {
			int diff_x = (x2-x1);
			int diff_y = (y2-y1);
			diff_x += diff_x < 0 ? -1 : 1;
			diff_y += diff_y < 0 ? -1 : 1;
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
	
	@FunctionalInterface
	public interface ButtonHandler {
		public static final int BUTTON_LEFT = 0x90;
		public static final int BUTTON_RIGHT = 0x100;
		public static final int BUTTON_SELECT = 0x80;
		
		void onButtonClicked(int button);
	}
}

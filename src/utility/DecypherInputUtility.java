package utility;

import model.ModelParameters;

/**
 * 
 * Utility class to decypher console mode inputs given in the format 12,7 or
 * l,g.
 * 
 * @author Team AFK
 * @version 1.0
 * 
 */

public class DecypherInputUtility {

	/**
	 * 
	 * Decyphers the given input into usable coordinates
	 * 
	 * @param input
	 *            the string to decypher
	 * @param axis
	 *            which axis it applies to. Correct values are "x" or "y".
	 * @return an int representing the coordinate
	 * @throws DecypheringFailureException
	 */

	public static int decypher(String input, String axis)
			throws DecypheringFailureException {

		try {
			int index = 0;
			String[] split = input.split(",");

			/*
			 * choice between x axis or y axis
			 */
			if (axis.equals("x")) {
				index = 0;
			} else if (axis.equals("y")) {
				index = 1;
			} else {
				return -1;
			}

			/*
			 * conversion
			 */
			String coordinate = split[index].trim();

			if (isInteger(coordinate)) {
				/*
				 * It's a number
				 */
				return Integer.parseInt(coordinate);
			} else {
				/*
				 * It's not a number, maybe a string ?
				 */
				if (coordinate.length() > 1) {
					throw new DecypheringFailureException(input);
				}
				/*
				 * It's not a number, but a char
				 */
				char xChar = coordinate.charAt(0);
				char minChar = (char) ('a');
				char maxChar = (char) ('a' + ModelParameters.GOBAN_SIZE_LARGE);

				if (xChar >= minChar && xChar <= maxChar) {
					return Integer.valueOf(xChar - 'a' + 1);
				} else {
					return -1;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new DecypheringFailureException(input);
		} catch (StringIndexOutOfBoundsException e) {
			throw new DecypheringFailureException(input);
		}
	}

	/**
	 * 
	 * Checks if given <code>s</code> is integer
	 * 
	 * @param s
	 *            the string to test
	 * @return true if <code>s</code> is an integer
	 */

	private static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}

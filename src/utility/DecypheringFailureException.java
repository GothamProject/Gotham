package utility;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

/**
 * 
 * Exception raised when decyphering a console mode input fails. Usually the
 * coordinates are not given in the specified format : 12,7 or l,g
 * 
 * @author Team AFK
 * @version 1.0
 * 
 */

public class DecypheringFailureException extends Exception {

	private static final long serialVersionUID = -7887102834477385586L;
	private static Logger logger = LoggerUtility
			.getLogger(DecypheringFailureException.class);

	public DecypheringFailureException(String string) {
		super("Decyphering failed on input " + string);
		logger.warn(getMessage());
	}

}

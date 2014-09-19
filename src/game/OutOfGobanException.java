package game;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

public class OutOfGobanException extends IncorrectMoveException {

	private static final long serialVersionUID = -2924377894593355658L;
	private static Logger logger = LoggerUtility
			.getLogger(OutOfGobanException.class);

	public OutOfGobanException(int xCoordinate, int yCoordinate) {
		super("Incorrect move detected : Out of bound on (" + xCoordinate
				+ ", " + yCoordinate + ")");
		logger.debug(getMessage());
	}

}
package game;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

public class IntersectionAlreadyOccupiedException extends IncorrectMoveException {

	private static final long serialVersionUID = 4390320495792522686L;
	private static Logger logger = LoggerUtility
			.getLogger(IntersectionAlreadyOccupiedException.class);

	public IntersectionAlreadyOccupiedException(int xCoordinate, int yCoordinate) {
		super("Incorrect move detected : Already occupied on (" + xCoordinate
				+ ", " + yCoordinate + ")");
		logger.warn(getMessage());
	}

}

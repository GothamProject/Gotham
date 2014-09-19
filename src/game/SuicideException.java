package game;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

public class SuicideException extends IncorrectMoveException {

	private static final long serialVersionUID = -2942033573248060891L;
	private static Logger logger = LoggerUtility
			.getLogger(SuicideException.class);

	public SuicideException(int xCoordinate, int yCoordinate) {
		super("Incorrect move detected : Suicide on (" + xCoordinate + ", "
				+ yCoordinate + ")");
		logger.warn(getMessage());
	}

}

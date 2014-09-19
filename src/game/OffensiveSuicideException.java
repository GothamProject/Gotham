package game;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

public class OffensiveSuicideException extends Exception {

	private static final long serialVersionUID = -2942033573248060891L;
	private static Logger logger = LoggerUtility
			.getLogger(OffensiveSuicideException.class);

	public OffensiveSuicideException(int xCoordinate, int yCoordinate) {
		super("Offensive Suicide on (" + xCoordinate + ", " + yCoordinate + ")");
		logger.info(getMessage());
	}

}

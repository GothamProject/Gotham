package game;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

public class IntersectionNotOccupiedException extends Exception {

	private static final long serialVersionUID = 8923118126560874212L;
	private static Logger logger = LoggerUtility
			.getLogger(IntersectionNotOccupiedException.class);

	public IntersectionNotOccupiedException(int xCoordinate, int yCoordinate) {
		super("Intersection (" + xCoordinate + ", " + yCoordinate
				+ ") is already free !");
		logger.info(getMessage());
	}

}

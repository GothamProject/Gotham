package model;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

public class NotAdjacentIntersectionException extends Exception {

	private static final long serialVersionUID = 7182725973015835552L;
	private static Logger logger = LoggerUtility
			.getLogger(NotAdjacentIntersectionException.class);

	public NotAdjacentIntersectionException(int xCoordinate, int yCoordinate) {
		super("Attempt to add non adjacent intersections to a territory ("
				+ xCoordinate + ", " + yCoordinate + ")");
		logger.info(getMessage());
	}

}

package model;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

public class TerritoryAlreadyContainsIntersectionException extends Exception {

	private static final long serialVersionUID = -287898097251306415L;
	private static Logger logger = LoggerUtility
			.getLogger(TerritoryAlreadyContainsIntersectionException.class);

	public TerritoryAlreadyContainsIntersectionException(int xCoordinate,
			int yCoordinate) {
		super(
				"Attempt to add an already existing intersection to a territory ("
						+ xCoordinate + ", " + yCoordinate + ")");
		logger.info(getMessage());
	}
}

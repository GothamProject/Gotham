package model;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

public class IntersectionNotFoundInTerritoryException extends Exception {

	private static final long serialVersionUID = -6213175908673286875L;
	private static Logger logger = LoggerUtility
			.getLogger(IntersectionNotFoundInTerritoryException.class);

	public IntersectionNotFoundInTerritoryException(Intersection intersection) {
		super("The intersection (" + intersection.getXCoordinate() + ","
				+ intersection.getYCoordinate()
				+ ") was not found in any territories.");
		logger.debug(getMessage());
	}
}

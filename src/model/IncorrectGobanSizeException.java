package model;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

public class IncorrectGobanSizeException extends Exception {

	private static final long serialVersionUID = 5485998055899340809L;
	private static Logger logger = LoggerUtility
			.getLogger(IncorrectGobanSizeException.class);

	public IncorrectGobanSizeException(int size) {
		super("Incorrect Goban size : " + size
				+ " is not allowed ! Regular values are 9x9, 13x13 or 19x19.");
		logger.warn(getMessage());
	}

}
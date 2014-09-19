package game;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

public class ComputerNotInGameException extends Exception {

	private static final long serialVersionUID = 1622658516374982252L;
	private static Logger logger = LoggerUtility
			.getLogger(ComputerNotInGameException.class);

	public ComputerNotInGameException() {
		super("The computer was not found in this game.");
		logger.warn(getMessage());
	}

}
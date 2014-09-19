package game;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

public class HistoryIsEmptyException extends Exception {

	private static final long serialVersionUID = -1697858991916187504L;
	private static Logger logger = LoggerUtility
			.getLogger(HistoryIsEmptyException.class);

	public HistoryIsEmptyException() {
		super("History is empty !");
		logger.info(getMessage());
	}

}

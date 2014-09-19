package run.gui;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

public class PlayersNotSelectedException extends Exception {

	private static final long serialVersionUID = -1462115919285178745L;
	private static Logger logger = LoggerUtility
			.getLogger(PlayersNotSelectedException.class);

	public PlayersNotSelectedException() {
		super("Please choose a player.");
		logger.warn(getMessage());
	}

}

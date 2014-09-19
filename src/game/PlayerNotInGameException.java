package game;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

public class PlayerNotInGameException extends Exception {

	private static final long serialVersionUID = 7403164300268842532L;
	private static Logger logger = LoggerUtility
			.getLogger(PlayerNotInGameException.class);

	public PlayerNotInGameException(String name) {
		super("Player " + name + " does not exist in the game.");
		logger.warn(getMessage());
	}

}

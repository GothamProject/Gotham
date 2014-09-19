package game;

import logger.LoggerUtility;
import model.User;

import org.apache.log4j.Logger;

public class UserNotInGameException extends Exception {

	private static final long serialVersionUID = 6925793000152233589L;
	private static Logger logger = LoggerUtility
			.getLogger(UserNotInGameException.class);

	public UserNotInGameException(User user) {
		super("The user " + user.getName() + " was not found in this game.");
		logger.warn(getMessage());
	}

}
package model;

public class UserAlreadyRegisteredException extends Exception {

	private static final long serialVersionUID = 1640583863220546512L;

	public UserAlreadyRegisteredException(String name) {
		super("User with name " + name + " is already registered. Please enter another username.");
	}

	
}

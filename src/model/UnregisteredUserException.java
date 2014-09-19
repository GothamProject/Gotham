package model;

public class UnregisteredUserException extends Exception {

	private static final long serialVersionUID = 5348979246450513649L;

	public UnregisteredUserException(String name) {
		super("No user with name " + name + " was found.");
	}

	
}

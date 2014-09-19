package model;

import java.io.IOException;

/**
 * Exception thrown when a directory cannot be created by the program.
 * @author Team AFK
 * @version 1.0
 */

public class CannotCreateDirectoryException extends IOException {

	private static final long serialVersionUID = -4252156046459161072L;

	public CannotCreateDirectoryException(String path) {
		super("The directory \"" + path + "\" could not be created!");
	}

}
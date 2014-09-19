package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import run.gui.GraphicalParameters;
import run.gui.NewGame;

/**
 * Manages the <code>Users</code> of the software.
 * 
 * @author Team AFK
 * @version 1.2
 */

// FIXME please just fix me

public class UsersRegister implements Serializable {

	private static final long serialVersionUID = -2452724340565045036L;

	public static final String USERS_SAVE_FILE_PATH = "./program_datas/";

	public static final String USERS_SAVE_FILE_NAME = "users";

	private Map<String, User> users = new HashMap<String, User>();

	private static UsersRegister instance = new UsersRegister();

	private UsersRegister() {
		loadFile();
	}

	public static UsersRegister getInstance() {
		return instance;
	}

	public void addUser(String name) throws UserAlreadyRegisteredException,
			FileNotFoundException, IOException {
		if (!contains(name)) {
			User user = new HumanUser(name);
			users.put(name, user);
			saveFile();
		} else {
			throw new UserAlreadyRegisteredException(name);
		}
	}

	public void deleteUser(String name) throws UnregisteredUserException,
			FileNotFoundException, IOException {
		if (users.get(name) != null) {
			users.remove(name);
			saveFile();
		} else {
			throw new UnregisteredUserException(name);
		}
	}

	public void modifyUserName(String previousName, String newName)
			throws UnregisteredUserException, UserAlreadyRegisteredException {
		if (!users.containsKey(previousName))
			throw new UnregisteredUserException(previousName);
		else if (users.containsKey(newName))
			throw new UserAlreadyRegisteredException(newName);
		else
			users.get(previousName).setName(newName);
	}

	public User getUser(String name) throws UnregisteredUserException {
		User user = users.get(name);
		if (user != null) {
			return user;
		} else {
			throw new UnregisteredUserException(name);
		}
	}

	public boolean contains(User user) {
		String name = user.getName();
		if (users.containsKey(name)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean contains(String name) {
		if (users.containsKey(name)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns a list of the names in the Register. Used to populate JList in
	 * the graphical setup interface.
	 * 
	 * @return the list of names
	 * @see NewGame
	 * @since 1.1
	 */
	public String[] getUsersList(String needle) {
		int size = users.size();
		String[] output = new String[size + 1];
		int i = 1;
		output[0] = GraphicalParameters.COMPUTER_NAME_FIELD;
		for (Map.Entry<String, User> entry : users.entrySet()) {
			String name = entry.getValue().getName();
			if (name.matches("(?i)(^" + needle + "(.*))")) {
				output[i] = name;
				i++;
			}

		}
		return output;
	}

	/**
	 * Returns the top five of player ranked by <code>highScore</code>.
	 * 
	 * @return the list of names
	 * @see User#getHighScore()
	 * @since 1.2
	 */
	public String getTopFive() {

		String output = "";

		List<User> topFive = new ArrayList<User>();
		for (User user : users.values()) {
			topFive.add(user);
		}

		Collections.sort(topFive);

		for (int i = topFive.size() - 1; i >= 0 && i > topFive.size() - 6; i--) {
			output += topFive.get(i).getName() + " with "
					+ topFive.get(i).getHighScore() + ":";
		}

		return output;
	}

	public void saveFile() throws FileNotFoundException, IOException {

		/*
		 * Creating directory and file if needed
		 */
		createDirectoryAndFile();

		ObjectOutputStream stream;
		try {
			stream = new ObjectOutputStream(new FileOutputStream(
					USERS_SAVE_FILE_PATH + USERS_SAVE_FILE_NAME));
			for (User user : users.values()) {
				stream.writeObject(user);
			}
			stream.close();
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

	}

	public void loadFile() {

		/*
		 * Creating directory and file if needed
		 */
		try {
			createDirectoryAndFile();
		} catch (CannotCreateDirectoryException e1) {
			System.err.println(e1.getMessage());
		} catch (IOException e1) {
			System.err.println(e1.getMessage());
		}

		/*
		 * Loading
		 */
		ObjectInputStream stream;
		try {
			stream = new ObjectInputStream(new FileInputStream(
					USERS_SAVE_FILE_PATH + USERS_SAVE_FILE_NAME));
			User user = null;
			while ((user = (User) stream.readObject()) != null) {
				users.put(user.getName(), user);
			}
			stream.close();

		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}

	}

	public String toString() {
		String output = "";
		for (User user : users.values()) {
			output += "\n" + user.toString();
		}
		return output;
	}

	private void createDirectoryAndFile()
			throws CannotCreateDirectoryException, IOException {
		/*
		 * Creating directory and file if needed
		 */
		File directory = new File(USERS_SAVE_FILE_PATH);
		if (!directory.exists() && !directory.mkdirs()) {
			/*
			 * Here the directory cannot be created
			 */
			throw new CannotCreateDirectoryException(USERS_SAVE_FILE_PATH);
		}
		File usersSaveFile = new File(USERS_SAVE_FILE_PATH
				+ USERS_SAVE_FILE_NAME);
		if (!usersSaveFile.exists()) {
			usersSaveFile.createNewFile();
		}
	}
}

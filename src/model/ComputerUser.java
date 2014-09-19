package model;

import java.util.Random;

import engine.UserVisitor;

/**
 * User which is dedicated to IAs.
 * 
 * @author Team AFK
 * @version 1.0
 * 
 */

public class ComputerUser extends User {

	private static final long serialVersionUID = -925090543563077532L;

	/**
	 * Create a user with a randomly generated name.
	 * 
	 * @since 1.0
	 */

	public ComputerUser() {
		setName(generateComputerName());
	}

	/**
	 * Create a user with a randomly generated name, but different than given
	 * <code>name</code>.
	 * 
	 * @param name
	 */
	public ComputerUser(String name) {
		setName(generateComputerName());
		while (name.equals(getName())) {
			setName(generateComputerName());
		}
	}

	/**
	 * Generates the username randomly in a list of famous robot names. Only
	 * used in {@link ComputerUser#ComputerUser()}.
	 * 
	 * @return a random name
	 * @since 1.0
	 */
	private String generateComputerName() {
		String[] names = { "GLaDOS", "Pierre Toupir", "Marvin", "Wheatley",
				"Claptrap", "Astro", "Bishop", "Jarvis", "Siri", "HAL-9000",
				"K2000", "Guilty Spark", "Cortana", "Atlas", "P-Body" };
		int length = names.length;
		Random random = new Random(System.currentTimeMillis());

		int chosenName = getRandomInt(random, 0, length - 1);
		return names[chosenName];
	}

	/**
	 * Pick a random int between <code>min</code> and <code>max</code>. Only
	 * used in {@link ComputerUser#generateComputerName()}.
	 * 
	 * @param random
	 * @param min
	 * @param max
	 * @return a random int
	 * @since 1.0
	 */

	private static int getRandomInt(Random random, int min, int max) {
		return (Math.abs(random.nextInt()) % (max - min + 1)) + min;
	}

	@Override
	public String toString() {
		return "Computer " + super.toString();
	}

	@Override
	public <T> T accept(UserVisitor<T> visitor) {
		return visitor.visit(this);
	}

}

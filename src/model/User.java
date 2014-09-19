package model;

import java.io.Serializable;

import engine.UserVisitor;

/**
 * A user of the program. Saves user's scores & statistics.
 * 
 * @author Team AFK
 * @version 1.0
 */

public abstract class User implements Serializable, Comparable<User> {

	private static final long serialVersionUID = -7351729135012380019L;
	private String name;
	private Double highScore;

	/**
	 * Create a user with a default name.
	 */

	public User() {
		this("Unknown");
	}

	/**
	 * Create a user with specified name.
	 * 
	 * @param name
	 */

	public User(String name) {
		setName(name);
		highScore = 0.0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getHighScore() {
		return highScore;
	}

	public void setHighScore(double highScore) {
		if (highScore > this.highScore) {
			this.highScore = highScore;
		}
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", highScore=" + highScore + "]";
	}

	public abstract <T> T accept(UserVisitor<T> visitor);

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(User user) {
		return highScore.compareTo(user.getHighScore());
	}

}

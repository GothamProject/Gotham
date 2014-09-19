package model;

import engine.UserVisitor;

/**
 * A user which is actually a real player.
 * @author Team AFK
 * @version 1.0
 */

public class HumanUser extends User {

	private static final long serialVersionUID = -5836847481504286177L;

	public HumanUser() {
		this("Unknown");
	}

	public HumanUser(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "Human " + super.toString();
	}

	@Override
	public <T> T accept(UserVisitor<T> visitor) {
		return visitor.visit(this);
	}

}

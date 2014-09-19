package model;

import run.gui.GameClock;
import engine.PlayerVisitor;

public class BlackPlayer extends Player {

	private static final long serialVersionUID = 3549911431185366315L;

	public BlackPlayer(GameClock gameClock) {
		super(gameClock);
	}

	public BlackPlayer() {
		super();
	}

	@Override
	public String toString() {
		return "BlackPlayer";
	}

	@Override
	public <T> T accept(PlayerVisitor<T> visitor) {
		return visitor.visit(this);
	}

}

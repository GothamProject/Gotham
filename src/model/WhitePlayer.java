package model;

import run.gui.GameClock;
import engine.PlayerVisitor;

public class WhitePlayer extends Player {

	private static final long serialVersionUID = -3244905095361589641L;

	public WhitePlayer(GameClock gameClock) {
		super(gameClock);
	}
	
	public WhitePlayer(){
		super();
	}

	@Override
	public String toString() {
		return "WhitePlayer";
	}

	@Override
	public <T> T accept(PlayerVisitor<T> visitor) {
		return visitor.visit(this);
	}

}

package model;

import java.io.Serializable;

import run.gui.GameClock;

import engine.PlayerVisitor;

/**
 * A player from a game. Can be Black or White.
 * 
 * @author Team AFK
 * @version 1.0
 */

public abstract class Player implements Serializable {

	private static final long serialVersionUID = 6083351935128933935L;

	/**
	 * Number of points owned by the player at the end of the game.
	 */

	protected double captureCount;

	private GameClock gameClock;

	public Player(GameClock gameClock) {
		captureCount = 0;
		this.gameClock = gameClock;
	}

	public Player() {
		captureCount = 0;
	}

	public abstract <T> T accept(PlayerVisitor<T> visitor);

	public double getCaptureCount() {
		return captureCount;
	}

	public void setCaptureCount(int newCaptureCount) {
		captureCount = newCaptureCount;
	}

	public GameClock getGameClock() {
		return gameClock;
	}

	public void setGameClock(GameClock gameClock) {
		this.gameClock = gameClock;
	}

	public abstract String toString();

	/**
	 * 
	 * @param captureCountToAdd
	 * @since 1.0
	 */

	public void addToCaptureCount(int captureCountToAdd) {
		captureCount = captureCount + captureCountToAdd;
	}

	public void addToCaptureCount(double komi) {
		captureCount = captureCount + komi;
	}
}

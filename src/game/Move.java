package game;

import model.Intersection;
import model.Player;

public class Move {

	private Player player;

	private Intersection intersection;

	private int captureCount;

	public Move(Player player, Intersection intersection, int captureCount) {
		this.player = player;
		this.intersection = intersection;
		this.captureCount = captureCount;
	}

	public Player getPlayer() {
		return player;
	}

	public Intersection getIntersection() {
		return intersection;
	}

	public int getCaptureCount() {
		return captureCount;
	}

	@Override
	public String toString() {
		return "Move [player=" + player + ", intersection=" + intersection
				+ ", captureCount=" + captureCount + "]";
	}

}

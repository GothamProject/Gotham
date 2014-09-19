package run.gui;

import time.Countdown;

public class GameClock {

	private Countdown gameCountdown;
	private Countdown byoyomiCountdown;

	public GameClock(Countdown gameCountdown, Countdown byoyomiCountdown) {

		this.gameCountdown = gameCountdown;
		this.byoyomiCountdown = byoyomiCountdown;

	}

	public Countdown getGameCountdown() {
		return gameCountdown;
	}

	public void setGameCountdown(Countdown gameCountdown) {
		this.gameCountdown = gameCountdown;
	}

	public Countdown getByoyomiCountdown() {
		return byoyomiCountdown;
	}

	public void setByoyomiCountdown(Countdown byoyomiCountdown) {
		this.byoyomiCountdown = byoyomiCountdown;
	}

	public void decrement() {
		String gameCountdownResult = gameCountdown.toString();

		if (!gameCountdownResult.equals("00:00")) {
			gameCountdown.decrement();
		} else {
			byoyomiCountdown.decrement();
		}
	}

	public Boolean isEmpty() {
		if (gameCountdown.isEmpty() && byoyomiCountdown.isEmpty()) {
			return true;
		}
		return false;
	}

}

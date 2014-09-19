package time;

public class Countdown {

	private Timer minutes = new Timer(0, CountDownParameters.MAX_MINUTES, 0);
	private Timer seconds = new Timer(0, CountDownParameters.MAX_SECONDS, 0);

	public Countdown(int minutes, int seconds) {
		this.minutes = new Timer(minutes, CountDownParameters.MAX_MINUTES, 0);
		this.seconds = new Timer(seconds, CountDownParameters.MAX_SECONDS, 0);
	}

	public Countdown() {
		this(30, 0);
	}

	public void decrement() {
		seconds.decrement();
		if (seconds.getValue() == CountDownParameters.MAX_SECONDS) {
			minutes.decrement();
		}
	}

	public Timer getSeconds() {
		return seconds;
	}

	public Timer getMinutes() {
		return minutes;
	}

	public int convert() {
		int secondConvertion = 0;
		secondConvertion = seconds.getValue()
				+ (CountDownParameters.ONE_MINUTE_IN_SECONDS * minutes
						.getValue());
		return secondConvertion;
	}

	public Boolean isEmpty() {
		if (minutes.isEmpty() && seconds.isEmpty()) {
			return true;
		}
		return false;
	}

	public String toString() {
		return minutes.toString() + ":" + seconds.toString();
	}

}

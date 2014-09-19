package run.console;

import time.Countdown;
import time.CountDownParameters;

public class TestTimer {

	public static void main(String[] args) {

		Countdown timer = new Countdown(1, 0);
		System.out.println(timer.convert());

		for (int i = timer.convert(); i > 0; i--) {
			try {
				timer.decrement();
				System.out.print("time : " + timer.toString());
				Thread.sleep(CountDownParameters.SPEED);
				System.out.println();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}

	}

}

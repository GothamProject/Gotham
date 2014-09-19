package ia;

import engine.IsWhitePlayerVisitor;
import game.ComputerNotInGameException;
import game.Game;
import game.IntersectionAlreadyOccupiedException;
import game.Move;
import game.OutOfGobanException;
import game.SuicideException;

import java.util.Random;

import model.BlackIntersection;
import model.Intersection;
import model.Player;
import model.WhiteIntersection;

public class RandomMoveStrategy implements MoveStrategy {

	@Override
	public Move getMove(Game game) {

		int xCoordinate;
		int yCoordinate;
		Player computerPlayer;
		Random random = new Random(System.currentTimeMillis());
		Boolean isWhite;
		Intersection intersection;

		/*
		 * Generate random intersection
		 */
		random = new Random(System.currentTimeMillis());
		xCoordinate = getRandomInt(random, 1, game.getGoban().getSize());
		yCoordinate = getRandomInt(random, 1, game.getGoban().getSize());

		/*
		 * Get the current player, and detect its color
		 */
		computerPlayer = game.getCurrentPlayer();
		isWhite = computerPlayer.accept(new IsWhitePlayerVisitor());

		/*
		 * Affect color
		 */
		if (isWhite) {
			intersection = new WhiteIntersection(xCoordinate, yCoordinate);
		} else {
			intersection = new BlackIntersection(xCoordinate, yCoordinate);
		}

		return new Move(computerPlayer, intersection, 0);

	}

	/**
	 * 
	 * Generates a random number between <code>min</code> and <code>max</code>.
	 * 
	 * @param random
	 *            {@link Random} class to use
	 * @param min
	 *            the minimal value
	 * @param max
	 *            the maximal value
	 * @return a random int
	 */
	private int getRandomInt(Random random, int min, int max) {
		return (Math.abs(random.nextInt()) % (max - min + 1)) + min;
	}

	/**
	 * @deprecated
	 */
	@Override
	public Move playMove(Game game) {
		Boolean computerPlayed = false;
		int xCoordinate;
		int yCoordinate;
		Player computerPlayer;
		Boolean isWhite;

		while (!computerPlayed) {

			Random random = new Random(System.currentTimeMillis());
			xCoordinate = getRandomInt(random, 1, game.getGoban().getSize());
			yCoordinate = getRandomInt(random, 1, game.getGoban().getSize());
			try {

				/*
				 * Get the computer's player, and detect its color
				 */
				computerPlayer = game.getComputerPlayer();
				isWhite = computerPlayer.accept(new IsWhitePlayerVisitor());

				if (isWhite) {
					Intersection intersection = new WhiteIntersection(
							xCoordinate, yCoordinate);
					game.addMove(computerPlayer, intersection);
					return new Move(computerPlayer, intersection, 0);
				} else {
					Intersection intersection = new BlackIntersection(
							xCoordinate, yCoordinate);
					game.addMove(computerPlayer, intersection);
					return new Move(computerPlayer, intersection, 0);
				}
				// computerPlayed = true;
			} catch (IntersectionAlreadyOccupiedException e) {
				System.out.println("Let me think a bit more, "
						+ game.getHumanUser().getName());
				continue;

			} catch (ComputerNotInGameException e) {
				System.out
						.println("I've lost track of myself. It appears that I'm insane !");
			} catch (OutOfGobanException e) {
				System.out
						.println("Hey, what if I put a stone here... Huh, what? Oh, huh, sorry, I was... just... nothing.");
				computerPlayed = false;
			} catch (SuicideException e) {
				System.out
						.println("Oops I'm depressed, I wish I wasn't alive anymore. I'll think more about that for a while.");
				computerPlayed = false;
				// } catch (OffensiveSuicideException e) {
				// System.err
				// .println("Haha ! You thought I'd never think about that eh ?!");
			}
			System.out.println("Ok, I played on ("
					+ (char) ('a' + xCoordinate - 1) + "," + yCoordinate + ")");
		}
		return null;
	}

}

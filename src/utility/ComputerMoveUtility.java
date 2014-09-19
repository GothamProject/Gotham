package utility;

import engine.IsWhitePlayerVisitor;
import game.ComputerNotInGameException;
import game.Game;
import game.IntersectionAlreadyOccupiedException;
import game.OutOfGobanException;
import game.SuicideException;
import ia.MoveStrategy;

import java.util.Random;

import model.BlackIntersection;
import model.Intersection;
import model.Player;
import model.WhiteIntersection;

/**
 * 
 * An utility class to handle computer moves in console mode.
 * 
 * @author Team AFK
 * @version 1.0
 * @deprecated as of version 1.0, replaced by MoveStrategy.
 * @see {@link MoveStrategy}
 * 
 */

public class ComputerMoveUtility {

	/**
	 * makes a random move
	 * 
	 * @param game
	 *            the current game
	 */

	public static void randomMove(Game game) {
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
				} else {
					Intersection intersection = new BlackIntersection(
							xCoordinate, yCoordinate);
					game.addMove(computerPlayer, intersection);
				}
				computerPlayed = true;
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
	}

	private static int getRandomInt(Random random, int min, int max) {
		return (Math.abs(random.nextInt()) % (max - min + 1)) + min;
	}

}

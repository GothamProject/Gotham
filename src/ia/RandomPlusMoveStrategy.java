package ia;

import engine.IntersectionVisitor;
import engine.IsFreeIntersectionVisitor;
import engine.IsWhitePlayerVisitor;
import game.ComputerNotInGameException;
import game.Game;
import game.HistoryIsEmptyException;
import game.IntersectionAlreadyOccupiedException;
import game.Move;
import game.OutOfGobanException;
import game.SuicideException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.BlackIntersection;
import model.FreeIntersection;
import model.Intersection;
import model.Player;
import model.WhiteIntersection;

public class RandomPlusMoveStrategy implements MoveStrategy {

	@Override
	public Move getMove(Game game) {

		int xCoordinate;
		int yCoordinate;
		List<Intersection> freeIntersections;
		Player computerPlayer;
		Boolean isWhite;
		Intersection intersection;

		/*
		 * Generate intersection
		 */
		Random random = new Random(System.currentTimeMillis());

		try {
			Intersection lastMovePlayed = game.getLastMove().getIntersection();

			freeIntersections = new ArrayList<Intersection>();

			int lastXCoordinate = lastMovePlayed.getXCoordinate();
			int lastYCoordinate = lastMovePlayed.getYCoordinate();
			
			if (lastXCoordinate <= 3) {
				lastXCoordinate = 4;
			}
			
			if(lastXCoordinate > game.getGoban().getSize() - 3){
				lastXCoordinate = game.getGoban().getSize() - 3;
			}
			
			if (lastYCoordinate <= 3) {
				lastYCoordinate = 4;
			}
			
			if(lastYCoordinate > game.getGoban().getSize() - 3){
				lastYCoordinate = game.getGoban().getSize() - 3;
			}
					
			IntersectionVisitor<Boolean> visitor = new IsFreeIntersectionVisitor();
			for (int i = lastXCoordinate - 3; i <= lastXCoordinate + 3; i++) {
				for (int j = lastYCoordinate - 3; j <= lastYCoordinate + 3; j++) {
					Intersection fooIntersection = game.getGoban()
							.getIntersectionByCoordinates(i, j);
					if (fooIntersection.accept(visitor)) {
						freeIntersections.add(new FreeIntersection(
								fooIntersection.getXCoordinate(),
								fooIntersection.getYCoordinate()));
					}
				}
			}

			if (!freeIntersections.isEmpty()) {
				int min = 0;
				int max = freeIntersections.size() - 1;

				int rand = getRandomInt(random, min, max);

				intersection = freeIntersections.get(rand);

				xCoordinate = intersection.getXCoordinate();
				yCoordinate = intersection.getYCoordinate();
			} else {
				xCoordinate = getRandomInt(random, 3,
						game.getGoban().getSize() - 2);
				yCoordinate = getRandomInt(random, 3,
						game.getGoban().getSize() - 2);
			}

		} catch (HistoryIsEmptyException e) {
			xCoordinate = getRandomInt(random, 3, game.getGoban().getSize() - 2);
			yCoordinate = getRandomInt(random, 3, game.getGoban().getSize() - 2);
		} catch (OutOfGobanException e) {
			xCoordinate = getRandomInt(random, 3, game.getGoban().getSize() - 2);
			yCoordinate = getRandomInt(random, 3, game.getGoban().getSize() - 2);
		} 

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

			try {
				Intersection lastMovePlayed = game.getLastMove()
						.getIntersection();

				// TODO if all of these intersections are occupied? play
				// somewhere else
				// TODO conditions on the number : not on the 2 first lines
				int minX = lastMovePlayed.getXCoordinate() - 3;
				int maxX = lastMovePlayed.getXCoordinate() + 3;
				int minY = lastMovePlayed.getYCoordinate() - 3;
				int maxY = lastMovePlayed.getYCoordinate() + 3;
				xCoordinate = getRandomInt(random, minX, maxX);
				yCoordinate = getRandomInt(random, minY, maxY);
			} catch (HistoryIsEmptyException e) {
				xCoordinate = getRandomInt(random, 3,
						game.getGoban().getSize() - 2);
				yCoordinate = getRandomInt(random, 3,
						game.getGoban().getSize() - 2);
			}

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
		// TODO better
		return null;
	}

}

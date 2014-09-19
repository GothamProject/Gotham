package ia;

import engine.IntersectionVisitor;
import engine.IsFreeIntersectionVisitor;
import engine.IsWhitePlayerVisitor;
import game.Game;
import game.HistoryIsEmptyException;
import game.Move;
import game.OutOfGobanException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.BlackIntersection;
import model.FreeIntersection;
import model.Intersection;
import model.Player;
import model.WhiteIntersection;

public class RandomPlusPlusMoveStrategy implements MoveStrategy {

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

			if (lastXCoordinate <= 4) {
				lastXCoordinate = 5;
			}

			if (lastXCoordinate > game.getGoban().getSize() - 4) {
				lastXCoordinate = game.getGoban().getSize() - 4;
			}

			if (lastYCoordinate <= 4) {
				lastYCoordinate = 5;
			}

			if (lastYCoordinate > game.getGoban().getSize() - 4) {
				lastYCoordinate = game.getGoban().getSize() - 4;
			}

			IntersectionVisitor<Boolean> visitor = new IsFreeIntersectionVisitor();
			for (int i = lastXCoordinate - 4; i <= lastXCoordinate + 4; i++) {
				for (int j = lastYCoordinate - 4; j <= lastYCoordinate + 4; j++) {
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
		return null;
	}

}

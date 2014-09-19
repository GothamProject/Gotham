package ia;

import engine.IntersectionVisitor;
import engine.IsBlackPlayerVisitor;
import engine.IsWhiteIntersectionVisitor;
import engine.SearchBlackTerritoriesVisitor;
import engine.SearchWhiteTerritoriesVisitor;
import game.Game;
import game.IntersectionAlreadyOccupiedException;
import game.IntersectionNotOccupiedException;
import game.Move;
import game.OutOfGobanException;
import game.SuicideException;

import java.util.List;

import model.FreeIntersection;
import model.Goban;
import model.Intersection;
import model.IntersectionNotFoundInTerritoryException;
import model.Player;
import model.Territories;

public class MinMaxMoveStrategy implements MoveStrategy {

	private Intersection bestIntersection;

	@Override
	public Move getMove(Game game) {

		/*
		 * We get the current player
		 */
		Player currentPlayer = game.getCurrentPlayer();
		int color;
		if (currentPlayer.accept(new IsBlackPlayerVisitor())) {
			color = 1;
		} else {
			color = -1;
		}

		/*
		 * We find the best move
		 */
		int value = color
				* negaMax(game.getGoban().getClone(), 1, -9999, +9999, color,
						new Territories(), new FreeIntersection());

		/*
		 * We return the best move found
		 */
		System.out
				.println("========================================= negaMax result : "
						+ value);

		/*
		 * If there is no clear winner, we fallback on RandomPlus
		 */
		if (value == 0) {
			System.out.println("RandomPlus fallback");
			MoveStrategy ms = new RandomPlusMoveStrategy();
			return ms.getMove(game);
		} else {
			return new Move(game.getCurrentPlayer(), bestIntersection, 0);
		}

	}

	/**
	 * @deprecated
	 */
	@Override
	public Move playMove(Game game) {
		return null;
	}

	private int negaMax(Goban goban, int depth, int alpha, int beta, int color,
			Territories capturedTerritories, Intersection playedIntersection) {

		System.out.println("===== entering negamax");
		System.out.println("depth " + depth + " alpha " + alpha + " beta "
				+ beta + " color " + color);

		if (depth == 0 || goban.isFull()) {
			int grade = grade(goban, color, capturedTerritories,
					playedIntersection);
			System.out.println("grade = " + (color * grade));
			return color * grade;
		}
		int bestValue = -9999;

		List<Intersection> childNodes = goban.getPossibleMoves(color);
		Goban newGoban;

		for (Intersection intersection : childNodes) {

			newGoban = goban.getClone();
			try {

				Territories justCapturedTerritories = newGoban
						.updateIntersection(intersection);

				int val = -1
						* negaMax(newGoban, depth - 1, -beta, -alpha, -color,
								justCapturedTerritories, intersection);

				if (val > bestValue) {
					bestIntersection = intersection;
				}

				bestValue = Math.max(bestValue, val);
				alpha = Math.max(alpha, val);

				if (alpha > beta) {
					break;
				}

			} catch (OutOfGobanException e) {
				// System.err.println(e.getMessage());
			} catch (IntersectionAlreadyOccupiedException e) {
				// System.err.println(e.getMessage());
			} catch (SuicideException e) {
				// System.err.println(e.getMessage());
			}

		}

		return bestValue;
	}

	private int grade(Goban goban, int color, Territories capturedTerritories,
			Intersection playedIntersection) {

		int grade = 0;

		/*
		 * If captures occur : +50
		 */
		if (capturedTerritories.getIntersectionCount() > 0) {
			grade += 50 + capturedTerritories.getIntersectionCount();
		}

		/*
		 * If intersection is in a territory : +20
		 */
		IntersectionVisitor<Territories> visitor;
		IntersectionVisitor<Territories> previousVisitor;
		if (playedIntersection.accept(new IsWhiteIntersectionVisitor())) {
			visitor = new SearchWhiteTerritoriesVisitor();
			previousVisitor = new SearchWhiteTerritoriesVisitor();
		} else {
			visitor = new SearchBlackTerritoriesVisitor();
			previousVisitor = new SearchBlackTerritoriesVisitor();
		}
		Territories territories = goban.accept(visitor);
		int territoriesCount = territories.getTerritoriesCount();

		try {

			Goban clone = goban.getClone();
			clone.captureIntersection(playedIntersection);
			Territories previousTerritories = clone.accept(previousVisitor);

			int previousTerritoriesCount = previousTerritories
					.getTerritoriesCount();

			int intersectionCount = territories.getTerritoryByIntersection(
					playedIntersection).getIntersectionsCount();

			if (intersectionCount > 1
					&& territoriesCount < previousTerritoriesCount) {
				grade += 20 + intersectionCount;
			}
		} catch (IntersectionNotFoundInTerritoryException e) {
			// System.err.println(e.getMessage());
		} catch (OutOfGobanException e1) {
			// System.err.println(e1.getMessage());
		} catch (IntersectionNotOccupiedException e1) {
			// System.err.println(e1.getMessage());
		}

		return -color * grade;
	}

}

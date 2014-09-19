package model;

import engine.IntersectionOccupiedVisitor;
import engine.IntersectionVisitor;
import engine.IsBlackIntersectionVisitor;
import engine.IsWhiteIntersectionVisitor;
import engine.SearchBlackTerritoriesVisitor;
import engine.SearchFreeTerritoriesVisitor;
import engine.SearchWhiteTerritoriesVisitor;
import game.IntersectionAlreadyOccupiedException;
import game.IntersectionNotOccupiedException;
import game.OutOfGobanException;
import game.SuicideException;

import java.util.ArrayList;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

/**
 * Contains an array of {@link Intersection}s and manages the placement and
 * removal of stones.
 * 
 * @author Team AFK
 * @version 1.0
 */

public class Goban {

	private int size;

	private Intersection[][] intersections;

	private static Logger logger = LoggerUtility.getLogger(Goban.class);

	public Goban(int size) throws IncorrectGobanSizeException {
		if (size == ModelParameters.GOBAN_SIZE_SMALL
				|| size == ModelParameters.GOBAN_SIZE_MEDIUM
				|| size == ModelParameters.GOBAN_SIZE_LARGE) {
			this.size = size;
			createIntersections(size);
			logger.trace("new Goban");
		} else {
			throw new IncorrectGobanSizeException(size);
		}
	}

	/**
	 * 
	 * Fills the goban with {@link FreeIntersection}s. This method is only used
	 * in {@link Goban#Goban(int)}.
	 * 
	 * @param size
	 * @since 1.0
	 */

	private void createIntersections(int size) {
		intersections = new Intersection[size + 1][size + 1];
		for (int row = 1; row <= size; row++) {
			for (int column = 1; column <= size; column++) {
				intersections[column][row] = new FreeIntersection(column, row);
			}
		}
	}

	/**
	 * Places an <code>Intersection</code> on the goban verifying if the move is
	 * correct.
	 * 
	 * @param intersection
	 * @throws OutOfGobanException
	 *             when coordinates are too high or low
	 * @throws IntersectionAlreadyOccupiedException
	 *             when a stone was already positioned here
	 * @throws SuicideException
	 *             when playing here will cause the stone to be jailed
	 * @since 1.0
	 */

	public Territories updateIntersection(Intersection intersection)
			throws OutOfGobanException, IntersectionAlreadyOccupiedException,
			SuicideException {
		int xCoordinate = intersection.getXCoordinate();
		int yCoordinate = intersection.getYCoordinate();
		IntersectionOccupiedVisitor visitor = new IntersectionOccupiedVisitor();

		if (xCoordinate > size || yCoordinate > size || xCoordinate < 1
				|| yCoordinate < 1) {
			throw new OutOfGobanException(xCoordinate, yCoordinate);
		} else if (getIntersectionByCoordinates(xCoordinate, yCoordinate)
				.accept(visitor)) {
			throw new IntersectionAlreadyOccupiedException(xCoordinate,
					yCoordinate);
		} else {

			/*
			 * we add the intersection, then check if it's jailed. If it is, we
			 * delete it from goban
			 */
			intersections[xCoordinate][yCoordinate] = intersection;

			/*
			 * In case the intersection is white, we search for white jailed
			 * territories
			 */
			Territories whiteTerritories = accept(new SearchWhiteTerritoriesVisitor());
			Territories whiteJailedTerritories = whiteTerritories
					.getJailedTerritories(this);
			Territories blackTerritories = accept(new SearchBlackTerritoriesVisitor());
			Territories blackJailedTerritories = blackTerritories
					.getJailedTerritories(this);
			if (intersection.accept(new IsWhiteIntersectionVisitor())) {

				if (whiteJailedTerritories.containsIntersection(intersection)) {
					/*
					 * we need to check if it's not an offensive suicide
					 */
					if (blackJailedTerritories
							.containsIntersection(new FreeIntersection(
									xCoordinate, yCoordinate - 1))
							|| blackJailedTerritories
									.containsIntersection(new FreeIntersection(
											xCoordinate + 1, yCoordinate))
							|| blackJailedTerritories
									.containsIntersection(new FreeIntersection(
											xCoordinate, yCoordinate + 1))
							|| blackJailedTerritories
									.containsIntersection(new FreeIntersection(
											xCoordinate - 1, yCoordinate))) {
						// throw new OffensiveSuicideException(xCoordinate,
						// yCoordinate);
					} else {
						intersections[xCoordinate][yCoordinate] = new FreeIntersection(
								xCoordinate, yCoordinate);
						throw new SuicideException(xCoordinate, yCoordinate);
					}
				}

				/*
				 * In case the intersection is black, we search for black jailed
				 * territories
				 */
			} else if (intersection.accept(new IsBlackIntersectionVisitor())) {

				if (blackJailedTerritories.containsIntersection(intersection)) {
					/*
					 * we need to check if it's not an offensive suicide
					 */
					if (whiteJailedTerritories
							.containsIntersection(new FreeIntersection(
									xCoordinate, yCoordinate - 1))
							|| whiteJailedTerritories
									.containsIntersection(new FreeIntersection(
											xCoordinate + 1, yCoordinate))
							|| whiteJailedTerritories
									.containsIntersection(new FreeIntersection(
											xCoordinate, yCoordinate + 1))
							|| whiteJailedTerritories
									.containsIntersection(new FreeIntersection(
											xCoordinate - 1, yCoordinate))) {
						// throw new OffensiveSuicideException(xCoordinate,
						// yCoordinate);
					} else {
						intersections[xCoordinate][yCoordinate] = new FreeIntersection(
								xCoordinate, yCoordinate);
						throw new SuicideException(xCoordinate, yCoordinate);
					}
				}

			}

			/*
			 * Delete jailed territories and returns the territories captured.
			 */
			return deleteJailedTerritories(intersection);

		}

	}

	/**
	 * Removes an <code>Intersection</code>, black or white, from the
	 * <code>Goban</code>. Used when a stone is captured.
	 * 
	 * @param intersection
	 * @throws OutOfGobanException
	 *             when coordinates are too high or low
	 * @throws IntersectionNotOccupiedException
	 *             when position is already empty
	 * @since 1.0
	 */

	public void captureIntersection(Intersection intersection)
			throws OutOfGobanException, IntersectionNotOccupiedException {
		int xCoordinate = intersection.getXCoordinate();
		int yCoordinate = intersection.getYCoordinate();
		IntersectionOccupiedVisitor visitor = new IntersectionOccupiedVisitor();

		if (xCoordinate > size || yCoordinate > size || xCoordinate < 1
				|| yCoordinate < 1) {
			throw new OutOfGobanException(xCoordinate, yCoordinate);
		} else if (!getIntersectionByCoordinates(xCoordinate, yCoordinate)
				.accept(visitor)) {
			throw new IntersectionNotOccupiedException(xCoordinate, yCoordinate);
		} else {
			intersections[xCoordinate][yCoordinate] = new FreeIntersection(
					xCoordinate, yCoordinate);
		}
	}

	/**
	 * Remove an entire <code>Territory</code> of <code>Intersection</code>s
	 * from the <code>Goban</code>. Used when a territory is captured.
	 * 
	 * @see Goban#captureIntersection(Intersection)
	 * @param territories
	 * @throws OutOfGobanException
	 * @throws IntersectionNotOccupiedException
	 * @since 1.0
	 */

	public void captureIntersections(Territories territories)
			throws OutOfGobanException, IntersectionNotOccupiedException {
		int totalTerritories = territories.getTerritoriesCount();
		for (int i = 0; i < totalTerritories; i++) {
			Territory territory = territories.getTerritoryByIndex(i);
			int totalIntersections = territory.getIntersectionsCount();
			for (int j = 0; j < totalIntersections; j++) {
				Intersection intersection = territory.getIntersectionByIndex(j);
				captureIntersection(new FreeIntersection(
						intersection.getXCoordinate(),
						intersection.getYCoordinate()));
			}
		}
	}

	/**
	 * 
	 * Detects and suppresses jailed territories for the specified player.
	 * <p>
	 * A visitor finds all territories in the color, then
	 * <code>Territories</code> selects jailed territories. These jailed
	 * territories are passed to <code>goban.captureIntersections()</code> to be
	 * permanently removed.
	 * 
	 * @param goban
	 *            the current goban of the game being played
	 * @return the number of intersections captured
	 * @see {@link SearchWhiteTerritoriesVisitor},
	 *      {@link SearchBlackTerritoriesVisitor} and {@link Territories}
	 */
	public Territories deleteJailedTerritories(Intersection intersection) {

		if (intersection.accept(new IsBlackIntersectionVisitor())) {

			/*
			 * if the black player just played, we capture white
			 */

			IntersectionVisitor<Territories> visitorWhite = new SearchWhiteTerritoriesVisitor();
			int captureCount = 0;

			/*
			 * Get territories
			 */
			Territories resultWhite = this.accept(visitorWhite);

			Territories jailedWhite = resultWhite.getJailedTerritories(this);
			try {
				captureIntersections(jailedWhite);
				captureCount += jailedWhite.getIntersectionCount();
			} catch (OutOfGobanException e) {
				System.err.println(e.getMessage());
			} catch (IntersectionNotOccupiedException e) {
				System.err.println(e.getMessage());
			}

			if (captureCount > 0) {
				return jailedWhite;
			}

		} else {

			/*
			 * if the white player just played, we capture black
			 */

			IntersectionVisitor<Territories> visitorBlack = new SearchBlackTerritoriesVisitor();
			int captureCount = 0;

			/*
			 * Get territories
			 */
			Territories resultBlack = this.accept(visitorBlack);

			Territories jailedBlack = resultBlack.getJailedTerritories(this);
			try {
				captureIntersections(jailedBlack);
				captureCount += jailedBlack.getIntersectionCount();
			} catch (OutOfGobanException e) {
				System.err.println(e.getMessage());
			} catch (IntersectionNotOccupiedException e) {
				System.err.println(e.getMessage());
			}

			if (captureCount > 0) {
				return jailedBlack;
			}

		}

		return new Territories();

	}

	public int getSize() {
		return size;
	}

	/**
	 * 
	 * @param xCoordinate
	 * @param yCoordinate
	 * @return The <code>Intersection</code> at the position entered
	 * @throws OutOfGobanException
	 * @since 1.0
	 */

	public Intersection getIntersectionByCoordinates(int xCoordinate,
			int yCoordinate) throws OutOfGobanException {
		if (xCoordinate > 0 && xCoordinate <= getSize() && yCoordinate > 0
				&& yCoordinate <= getSize()) {
			return intersections[xCoordinate][yCoordinate];
		} else {
			throw new OutOfGobanException(xCoordinate, yCoordinate);
		}
	}

	public boolean isFull() {
		IntersectionVisitor<Territories> visitor = new SearchFreeTerritoriesVisitor();
		Territories freeTerritories = accept(visitor);
		if (freeTerritories.getIntersectionCount() < 1) {
			return true;
		}
		return false;
	}

	public ArrayList<Intersection> getPossibleMoves(int color) {
		ArrayList<Intersection> possibleMoves = new ArrayList<Intersection>();
		IntersectionVisitor<Boolean> visitor = new IntersectionOccupiedVisitor();

		for (int row = 1; row <= size; row++) {
			for (int column = 1; column <= size; column++) {

				/*
				 * If the intersection is occupied, discard
				 */
				if (intersections[column][row].accept(visitor)) {
					continue;
				}

				/*
				 * If free, then add
				 */
				Intersection intersection;
				if (color == 1) {
					intersection = new BlackIntersection(column, row);
				} else {
					intersection = new WhiteIntersection(column, row);
				}
				possibleMoves.add(intersection);
			}
		}

		return possibleMoves;
	}

	public Goban getClone() {
		try {
			Goban clone = new Goban(size);
			// clone.intersections = (Intersection[][]) intersections.clone();

			for (int row = 1; row <= size; row++) {
				for (int column = 1; column <= size; column++) {
					clone.updateIntersection(getIntersectionByCoordinates(
							column, row));
				}
			}

			return clone;

		} catch (IncorrectGobanSizeException e) {
			System.err.println(e.getMessage());
		} catch (OutOfGobanException e) {
			System.err.println(e.getMessage());
		} catch (IntersectionAlreadyOccupiedException e) {
			System.err.println(e.getMessage());
		} catch (SuicideException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	public String toString() {
		String output = "      ";
		String horizontalLine = "     ";
		char maxChar = (char) ('a' + size);

		for (char column = 'a'; column < maxChar; column++) {
			output += String.format("%c", column) + " ";
			horizontalLine += "--";
		}
		output += "\n" + horizontalLine + "\n";
		for (int row = 1; row <= size; row++) {
			for (int column = 1; column <= size; column++) {
				if (column == 1) {
					output += String.format("%2d", row) + " |  ";
				}
				Intersection intersection = null;
				try {
					intersection = getIntersectionByCoordinates(column, row);
				} catch (OutOfGobanException e) {

					System.err.println(e.getMessage());
				}
				if (intersection instanceof WhiteIntersection) {
					output += "O ";
				} else if (intersection instanceof BlackIntersection) {
					output += "X ";
				} else if (intersection instanceof FreeIntersection) {
					output += "+ ";
				}
			}
			output += "\n";
		}
		return output;
	}

	public <T> T accept(IntersectionVisitor<T> visitor) {
		for (int row = 1; row <= size; row++) {
			for (int column = 1; column <= size; column++) {
				intersections[column][row].accept(visitor);
			}
		}
		return visitor.getOutput();
	}

}

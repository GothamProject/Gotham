package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import engine.IntersectionVisitor;
import engine.IsBlackIntersectionVisitor;
import engine.IsBlackPlayerVisitor;
import engine.IsJailedVisitor;
import engine.IsWhiteIntersectionVisitor;
import game.OutOfGobanException;

public class Territories implements Serializable {

	private static final long serialVersionUID = -6215831953385426917L;
	private List<Territory> territories = new ArrayList<Territory>();

	public void add(Territory territory) {
		territories.add(territory);
		// mergeTerritories();
	}

	public void add(Intersection intersection)
			throws TerritoryAlreadyContainsIntersectionException {

		if (containsIntersection(intersection)) {
			throw new TerritoryAlreadyContainsIntersectionException(
					intersection.getXCoordinate(),
					intersection.getYCoordinate());
		}

		Boolean northExists = true;
		Boolean westExists = true;

		int xCoordinate = intersection.getXCoordinate();
		int yCoordinate = intersection.getYCoordinate();
		Intersection northIntersection = new FreeIntersection(xCoordinate,
				yCoordinate - 1);
		Intersection westIntersection = new FreeIntersection(xCoordinate - 1,
				yCoordinate);

		/*
		 * We try to get the adjacent territories. If succeed we merge.
		 */

		try {
			Territory northTerritory = getTerritoryByIntersection(northIntersection);
			northTerritory.add(intersection);
		} catch (IntersectionNotFoundInTerritoryException e) {
			northExists = false;
			try {
				Territory westTerritory = getTerritoryByIntersection(westIntersection);
				westTerritory.add(intersection);
			} catch (IntersectionNotFoundInTerritoryException e2) {
				westExists = false;
			} catch (TerritoryAlreadyContainsIntersectionException e2) {
				System.err.println(e.getMessage());
			} catch (NotAdjacentIntersectionException e2) {
				System.err.println(e.getMessage());
			}
		} catch (TerritoryAlreadyContainsIntersectionException e) {
			System.err.println(e.getMessage());
		} catch (NotAdjacentIntersectionException e) {
			System.err.println(e.getMessage());
		}

		if (northExists && westExists) {
			/*
			 * intersection was inserted in northTerritory. We merge the two,
			 * adjacent intersection by adjacent intersection.
			 */
			try {

				/*
				 * we find how many adjacent intersection are present in
				 * westTerritory
				 */
				Territory northTerritory = getTerritoryByIntersection(northIntersection);
				Territory westTerritory = getTerritoryByIntersection(westIntersection);
				int westCount = westTerritory.getIntersectionsCount();
				while (westCount > 0) {
					int adjacentIntersectionCount = westTerritory
							.getAdjacentIntersectionCount(intersection);

					/*
					 * we transfer all the adjacent intersections from west to
					 * north
					 */
					while (adjacentIntersectionCount > 0) {
						Intersection intersectionToTransfer = westTerritory
								.getAdjacentIntersection(intersection);
						northTerritory.add(intersectionToTransfer);
						westTerritory.remove(intersectionToTransfer);
						adjacentIntersectionCount--;
						westCount--;
						/*
						 * we update intersection for next round
						 */
						intersection = intersectionToTransfer;
					}

				}
				/*
				 * we delete westTerritory
				 */
				territories.remove(westTerritory);
			} catch (IntersectionNotFoundInTerritoryException e) {
			} catch (TerritoryAlreadyContainsIntersectionException e) {
			} catch (NotAdjacentIntersectionException e) {
				System.err.println(e.getMessage());
			}
		}

		if (!northExists && !westExists) {
			Territory newTerritory = new Territory(intersection);
			territories.add(newTerritory);
		}

	}

	public Boolean containsIntersection(Intersection intersection) {
		for (Territory territory : territories) {
			if (territory.containsIntersection(intersection)) {
				return true;
			}
		}
		return false;
	}

	public Boolean containsIntersectionByCoordinates(int xCoordinate,
			int yCoordinate) {
		Intersection intersection = new FreeIntersection(xCoordinate,
				yCoordinate);
		return containsIntersection(intersection);
	}

	public Territory getTerritoryByIndex(int index)
			throws NoSuchElementException {
		if (index < getTerritoriesCount()) {
			return territories.get(index);
		} else {
			throw new NoSuchElementException("There is no Territory by index "
					+ index + " in this Group of Territories.");
		}
	}

	public int getTerritoriesCount() {
		return territories.size();
	}

	public int getIntersectionCount() {
		int intersectionCount = 0;
		for (Territory territory : territories) {
			intersectionCount += territory.getIntersectionsCount();
		}
		return intersectionCount;
	}

	public void clear() {
		territories.clear();
	}

	public Territories getJailedTerritories(Goban goban) {
		Territories output = new Territories();
		for (Territory territory : territories) {
			/*
			 * Checking if jailed
			 */
			IntersectionVisitor<Boolean> visitor = new IsJailedVisitor(goban);
			if (territory.accept(visitor)) {
				output.add(territory);
			}
		}
		return output;
	}

	@Override
	public String toString() {
		if (getTerritoriesCount() == 0) {
			return "Territories group with no Territory";
		} else {
			String output = "Territories group with :\n";
			for (Territory territory : territories) {
				output += territory.toString() + "\n";
			}
			return output;
		}
	}

	public String toStringWithJailed(Goban goban) {
		if (getTerritoriesCount() == 0) {
			return "Territories group with no Territory";
		} else {
			String output = "Territories group with :\n";
			for (Territory territory : territories) {
				output += territory.toString();

				/*
				 * Checking if jailed
				 */
				IntersectionVisitor<Boolean> visitor = new IsJailedVisitor(
						goban);
				if (territory.accept(visitor)) {
					output += "This territory is jailed.\n";
				} else {
					output += "This territory is NOT jailed\n";
				}

			}
			return output;
		}
	}

	public Territory getTerritoryByIntersection(Intersection intersection)
			throws IntersectionNotFoundInTerritoryException {
		for (Territory territory : territories) {
			if (territory.containsIntersection(intersection)) {
				return territory;
			}
		}
		throw new IntersectionNotFoundInTerritoryException(intersection);
	}

	public Boolean isSurroundedByColor(Territory territory, Goban goban,
			Player player) {
		Boolean surrounded = false;
		int i = 0;
		int xCoordinate;
		int yCoordinate;
		Intersection intersection;
		Intersection fooIntersection;
		IntersectionVisitor<Boolean> visitor;
		if (player.accept(new IsBlackPlayerVisitor())) {
			visitor = new IsBlackIntersectionVisitor();
		} else {
			visitor = new IsWhiteIntersectionVisitor();
		}
		while (i < territory.getIntersectionsCount()) {
			intersection = territory.getIntersectionByIndex(i);
			xCoordinate = intersection.getXCoordinate();
			yCoordinate = intersection.getYCoordinate();
			try {
				// NORTH
				if (yCoordinate != 1) {
					fooIntersection = goban.getIntersectionByCoordinates(
							xCoordinate, yCoordinate - 1);
					surrounded = surrounded || fooIntersection.accept(visitor);
				}
				// EAST
				if (xCoordinate != goban.getSize()) {
					fooIntersection = goban.getIntersectionByCoordinates(
							xCoordinate + 1, yCoordinate);
					surrounded = surrounded || fooIntersection.accept(visitor);
				}
				// SOUTH
				if (yCoordinate != goban.getSize()) {
					fooIntersection = goban.getIntersectionByCoordinates(
							xCoordinate, yCoordinate + 1);
					surrounded = surrounded || fooIntersection.accept(visitor);
				}
				// WEST
				if (xCoordinate != 1) {
					fooIntersection = goban.getIntersectionByCoordinates(
							xCoordinate - 1, yCoordinate);
					surrounded = surrounded || fooIntersection.accept(visitor);
				}
			} catch (OutOfGobanException e) {
				System.err.println(e.getMessage());
			}
			i++;

		}
		return surrounded;
	}

}
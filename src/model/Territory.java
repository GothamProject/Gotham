package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import engine.IntersectionVisitor;

public class Territory implements Serializable {

	private static final long serialVersionUID = 1971892901370958635L;
	private List<Intersection> intersections;

	public Territory() {
		intersections = new ArrayList<Intersection>();
	}

	public Territory(Intersection intersection) {
		this();
		intersections.add(intersection);
	}

	public void add(Intersection intersection)
			throws TerritoryAlreadyContainsIntersectionException,
			NotAdjacentIntersectionException {

		/*
		 * check if territory is empty
		 */
		if (getIntersectionsCount() == 0) {
			intersections.add(intersection);
		} else {

			/*
			 * check if intersection already exists
			 */
			if (!intersections.contains(intersection)) {
				int xCoordinate = intersection.getXCoordinate();
				int yCoordinate = intersection.getYCoordinate();

				Intersection northIntersection = new FreeIntersection(
						xCoordinate, yCoordinate - 1);
				Intersection eastIntersection = new FreeIntersection(
						xCoordinate + 1, yCoordinate);
				Intersection southIntersection = new FreeIntersection(
						xCoordinate, yCoordinate + 1);
				Intersection westIntersection = new FreeIntersection(
						xCoordinate - 1, yCoordinate);

				/*
				 * check if the territory contains at least one adjacent
				 * intersection
				 */
				if (containsIntersection(northIntersection)
						|| containsIntersection(eastIntersection)
						|| containsIntersection(southIntersection)
						|| containsIntersection(westIntersection)) {
					intersections.add(intersection);
				} else {
					throw new NotAdjacentIntersectionException(xCoordinate,
							yCoordinate);
				}
			} else {
				throw new TerritoryAlreadyContainsIntersectionException(
						intersection.getXCoordinate(),
						intersection.getYCoordinate());
			}
		}
	}

	/**
	 * 
	 * @param intersection
	 * @return true if the {@link Intersection} given exists in the
	 *         <code>Territory</code>.
	 */
	public Boolean containsIntersection(Intersection intersection) {
		return intersections.contains(intersection);
	}

	/**
	 * 
	 * @param xCoordinate
	 * @param yCoordinate
	 * @return true if the {@link Intersection} with coordinates
	 *         <code>xCoordinate</code> and <code>yCoordinate</code> exists in
	 *         the <code>Territory</code>.
	 */
	public Boolean containsIntersectionByCoordinates(int xCoordinate,
			int yCoordinate) {
		Intersection intersection = new FreeIntersection(xCoordinate,
				yCoordinate);
		return containsIntersection(intersection);
	}

	public Intersection getAdjacentIntersection(Intersection intersection)
			throws IntersectionNotFoundInTerritoryException {
		int xCoordinate = intersection.getXCoordinate();
		int yCoordinate = intersection.getYCoordinate();
		Intersection northIntersection = new FreeIntersection(xCoordinate,
				yCoordinate - 1);
		Intersection eastIntersection = new FreeIntersection(xCoordinate + 1,
				yCoordinate);
		Intersection southIntersection = new FreeIntersection(xCoordinate,
				yCoordinate + 1);
		Intersection westIntersection = new FreeIntersection(xCoordinate - 1,
				yCoordinate);
		if (containsIntersection(northIntersection)) {
			return getIntersectionByCoordinates(
					northIntersection.getXCoordinate(),
					northIntersection.getYCoordinate());
		}
		if (containsIntersection(eastIntersection)) {
			return getIntersectionByCoordinates(
					eastIntersection.getXCoordinate(),
					eastIntersection.getYCoordinate());
		}
		if (containsIntersection(southIntersection)) {
			return getIntersectionByCoordinates(
					southIntersection.getXCoordinate(),
					southIntersection.getYCoordinate());
		}
		if (containsIntersection(westIntersection)) {
			return getIntersectionByCoordinates(
					westIntersection.getXCoordinate(),
					westIntersection.getYCoordinate());
		}
		throw new IntersectionNotFoundInTerritoryException(intersection);
	}

	public int getAdjacentIntersectionCount(Intersection intersection) {
		int count = 0;
		int xCoordinate = intersection.getXCoordinate();
		int yCoordinate = intersection.getYCoordinate();
		Intersection northIntersection = new FreeIntersection(xCoordinate,
				yCoordinate - 1);
		Intersection eastIntersection = new FreeIntersection(xCoordinate + 1,
				yCoordinate);
		Intersection southIntersection = new FreeIntersection(xCoordinate,
				yCoordinate + 1);
		Intersection westIntersection = new FreeIntersection(xCoordinate - 1,
				yCoordinate);
		if (containsIntersection(northIntersection)) {
			count++;
		}
		if (containsIntersection(eastIntersection)) {
			count++;
		}
		if (containsIntersection(southIntersection)) {
			count++;
		}
		if (containsIntersection(westIntersection)) {
			count++;
		}

		return count;
	}

	public Intersection getIntersectionByCoordinates(int xCoordinate,
			int yCoordinate) throws IntersectionNotFoundInTerritoryException {
		Intersection intersection = new FreeIntersection(xCoordinate,
				yCoordinate);
		for (Intersection other : intersections) {
			if (other.equals(intersection)) {
				return other;
			}
		}
		throw new IntersectionNotFoundInTerritoryException(
				new FreeIntersection(xCoordinate, yCoordinate));
	}

	public Intersection getIntersectionByIndex(int index)
			throws NoSuchElementException {
		if (index < getIntersectionsCount()) {
			return intersections.get(index);
		} else {
			throw new NoSuchElementException(
					"There is no Intersection by index " + index
							+ " in this Territory.");
		}
	}

	public int getIntersectionsCount() {
		return intersections.size();
	}
	
	public void remove(Intersection intersection) {
		intersections.remove(intersection);
	}

	public void clear() {
		intersections.clear();
	}

	@Override
	public String toString() {
		if (getIntersectionsCount() == 0) {
			return "Territory with no intersections";
		} else {
			String output = "Territory with :\n";
			for (int i = 0; i < intersections.size(); i++) {
				output += intersections.get(i).toString() + "\n";
			}
			return output;
		}
	}

	public <T> T accept(IntersectionVisitor<T> visitor) {
		for (Intersection intersection : intersections) {
			intersection.accept(visitor);
		}
		return visitor.getOutput();
	}

}

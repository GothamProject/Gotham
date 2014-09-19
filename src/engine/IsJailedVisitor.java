package engine;

import game.OutOfGobanException;
import model.BlackIntersection;
import model.FreeIntersection;
import model.Goban;
import model.Intersection;
import model.WhiteIntersection;

public class IsJailedVisitor implements IntersectionVisitor<Boolean> {

	/**
	 * output is different from 0 if territory is jailed
	 */
	private int output = 0;
	private Goban goban;

	public IsJailedVisitor(Goban goban) {
		super();
		this.goban = goban;
	}

	@Override
	public Boolean visit(FreeIntersection intersection) {
		return null;
	}

	@Override
	public Boolean visit(WhiteIntersection intersection) {
		isJailed(intersection);
		return null;
	}

	@Override
	public Boolean visit(BlackIntersection intersection) {
		isJailed(intersection);
		return null;
	}

	@Override
	public Boolean getOutput() {
		if (output != 0) {
			return false;
		} else {
			return true;
		}
	}

	private void isJailed(Intersection intersection) {
		int xCoordinate = intersection.getXCoordinate();
		int yCoordinate = intersection.getYCoordinate();

		/*
		 * See here for explanations
		 */
		Intersection northIntersection;
		try {
			/*
			 * We try to fetch the corresponding intersection
			 */
			northIntersection = goban.getIntersectionByCoordinates(xCoordinate,
					yCoordinate - 1);
		} catch (OutOfGobanException e) {
			/*
			 * Whatever, we just want this intersection not to be free (border
			 * effect)
			 */
			northIntersection = new BlackIntersection();
		}

		/*
		 * Below we do the same for east, south and west.
		 */
		Intersection eastIntersection;
		try {
			eastIntersection = goban.getIntersectionByCoordinates(
					xCoordinate + 1, yCoordinate);
		} catch (OutOfGobanException e) {
			eastIntersection = new BlackIntersection();
		}

		Intersection southIntersection;
		try {
			southIntersection = goban.getIntersectionByCoordinates(xCoordinate,
					yCoordinate + 1);
		} catch (OutOfGobanException e) {
			southIntersection = new BlackIntersection();
		}

		Intersection westIntersection;
		try {
			westIntersection = goban.getIntersectionByCoordinates(
					xCoordinate - 1, yCoordinate);
		} catch (OutOfGobanException e) {
			westIntersection = new BlackIntersection();
		}

		/*
		 * if intersection is free, add one degree of freedom in output
		 */

		IntersectionVisitor<Boolean> visitor = new IsFreeIntersectionVisitor();

		if (northIntersection.accept(visitor)) {
			output++;
		}
		if (eastIntersection.accept(visitor)) {
			output++;
		}
		if (southIntersection.accept(visitor)) {
			output++;
		}
		if (westIntersection.accept(visitor)) {
			output++;
		}

	}

}

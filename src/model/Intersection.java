package model;

import java.io.Serializable;

import engine.IntersectionVisitor;

/**
 * An element on the goban. Can be a black stone, a white stone or a free
 * position.
 * 
 * @author Team AFK
 * @version 1.0
 */

public abstract class Intersection implements Serializable {

	private static final long serialVersionUID = -6353952661998458785L;

	/**
	 * Position of the <code>Intersection</code> on the x-axis.
	 */

	private int xCoordinate;

	/**
	 * Position of the <code>Intersection</code> on the y-axis.
	 */

	private int yCoordinate;

	public Intersection() {
		this(1, 1);
	}

	/**
	 * Create an <code>Intersection</code>with the specified position.
	 * 
	 * @param xCoordinate
	 * @param yCoordinate
	 * @since 1.0
	 */

	public Intersection(int xCoordinate, int yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}

	public int getXCoordinate() {
		return xCoordinate;
	}

	public int getYCoordinate() {
		return yCoordinate;
	}

	public String toString() {
		return "Intersection with xCoordinate = " + xCoordinate
				+ ", yCoordinate = " + yCoordinate;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof Intersection) {
				Intersection other = (Intersection) obj;
				if (xCoordinate == other.getXCoordinate()
						&& yCoordinate == other.getYCoordinate()) {
					return true;
				}
			}
		}
		return false;
	}

	public abstract <T> T accept(IntersectionVisitor<T> visitor);

}

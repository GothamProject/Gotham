package model;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

import engine.IntersectionVisitor;

public class BlackIntersection extends Intersection {

	private static final long serialVersionUID = -6353952661998458785L;

	private static Logger logger = LoggerUtility
			.getLogger(BlackIntersection.class);

	public BlackIntersection() {
		this(1, 1);
	}

	public BlackIntersection(int xCoordinate, int yCoordinate) {
		super(xCoordinate, yCoordinate);
		logger.trace("Create black intersection : X");
	}

	public String toString() {
		return "Black " + super.toString();
	}

	public <T> T accept(IntersectionVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

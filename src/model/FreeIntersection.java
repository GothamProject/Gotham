package model;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

import engine.IntersectionVisitor;

public class FreeIntersection extends Intersection {

	private static final long serialVersionUID = -6353952661998458785L;

	private static Logger logger = LoggerUtility
			.getLogger(FreeIntersection.class);

	public FreeIntersection() {
		this(1, 1);
	}

	public FreeIntersection(int xCoordinate, int yCoordinate) {
		super(xCoordinate, yCoordinate);
		logger.trace("Create free intersection : +");
	}

	public String toString() {
		return super.toString();
	}

	public <T> T accept(IntersectionVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

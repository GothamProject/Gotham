package model;

import logger.LoggerUtility;

import org.apache.log4j.Logger;

import engine.IntersectionVisitor;

public class WhiteIntersection extends Intersection {

	private static final long serialVersionUID = -6353952661998458785L;

	private Logger logger = LoggerUtility.getLogger(WhiteIntersection.class);

	public WhiteIntersection() {
		this(1, 1);
	}

	public WhiteIntersection(int xCoordinate, int yCoordinate) {
		super(xCoordinate, yCoordinate);
		logger.trace("Create white intersection : O");
	}

	public String toString() {
		return "White " + super.toString();
	}

	public <T> T accept(IntersectionVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

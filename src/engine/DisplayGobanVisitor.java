package engine;

import model.BlackIntersection;
import model.FreeIntersection;
import model.Intersection;
import model.WhiteIntersection;

public class DisplayGobanVisitor implements IntersectionVisitor<String> {

	private String output = new String();

	@Override
	public String visit(FreeIntersection intersection) {
		displayHeaders(intersection);
		output += "+ ";
		return null;
	}

	@Override
	public String visit(WhiteIntersection intersection) {
		displayHeaders(intersection);
		output += "O ";
		return null;
	}

	@Override
	public String visit(BlackIntersection intersection) {
		displayHeaders(intersection);
		output += "X ";
		return null;
	}

	@Override
	public String getOutput() {
		return output;
	}

	private void displayHeaders(Intersection intersection) {
		int x = intersection.getXCoordinate();
		int y = intersection.getYCoordinate();

		if (x == 1) {
			output += "\n" + String.format("%2d", y) + " |  ";
		}
	}

}

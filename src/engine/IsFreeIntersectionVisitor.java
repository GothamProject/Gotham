package engine;

import model.BlackIntersection;
import model.FreeIntersection;
import model.WhiteIntersection;

public class IsFreeIntersectionVisitor implements IntersectionVisitor<Boolean> {

	@Override
	public Boolean visit(FreeIntersection intersection) {
		return true;
	}

	@Override
	public Boolean visit(WhiteIntersection intersection) {
		return false;
	}

	@Override
	public Boolean visit(BlackIntersection intersection) {
		return false;
	}

	@Override
	public Boolean getOutput() {
		return false;
	}

}

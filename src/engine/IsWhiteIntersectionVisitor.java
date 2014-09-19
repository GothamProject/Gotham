package engine;

import model.BlackIntersection;
import model.FreeIntersection;
import model.WhiteIntersection;

public class IsWhiteIntersectionVisitor implements IntersectionVisitor<Boolean>{

	@Override
	public Boolean visit(FreeIntersection intersection) {
		return false;
	}

	@Override
	public Boolean visit(WhiteIntersection intersection) {
		return true;
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

package engine;

import model.BlackIntersection;
import model.FreeIntersection;
import model.WhiteIntersection;

public interface IntersectionVisitor<T> {

	T visit(FreeIntersection intersection);

	T visit(WhiteIntersection intersection);

	T visit(BlackIntersection intersection);
	
	T getOutput();
}

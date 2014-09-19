package engine;

import model.BlackIntersection;
import model.FreeIntersection;
import model.Territories;
import model.TerritoryAlreadyContainsIntersectionException;
import model.WhiteIntersection;

public class SearchBlackTerritoriesVisitor implements IntersectionVisitor<Territories> {

	private Territories territories = new Territories();

	@Override
	public Territories visit(FreeIntersection intersection) {
		return null;
	}

	@Override
	public Territories visit(WhiteIntersection intersection) {
		return null;
	}

	@Override
	public Territories visit(BlackIntersection intersection) {
		try {
			territories.add(intersection);
		} catch (TerritoryAlreadyContainsIntersectionException e) {
		}
		return null;
	}

	@Override
	public Territories getOutput() {
		return territories;
	}

}

package engine;

import model.BlackPlayer;
import model.WhitePlayer;

public class IsBlackPlayerVisitor implements PlayerVisitor<Boolean> {

	@Override
	public Boolean visit(BlackPlayer blackPlayer) {
		return true;
	}

	@Override
	public Boolean visit(WhitePlayer whitePlayer) {
		return false;
	}

}

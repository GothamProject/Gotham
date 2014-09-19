package engine;

import model.BlackPlayer;
import model.WhitePlayer;

public class IsWhitePlayerVisitor implements PlayerVisitor<Boolean> {

	@Override
	public Boolean visit(BlackPlayer blackPlayer) {
		return false;
	}

	@Override
	public Boolean visit(WhitePlayer whitePlayer) {
		return true;
	}

}

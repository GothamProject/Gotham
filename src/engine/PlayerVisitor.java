package engine;

import model.BlackPlayer;
import model.WhitePlayer;

public interface PlayerVisitor<T> {

	T visit(BlackPlayer blackPlayer);

	T visit(WhitePlayer whitePlayer);
}

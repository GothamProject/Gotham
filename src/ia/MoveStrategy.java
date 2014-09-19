package ia;

import game.Game;
import game.Move;

/**
 * 
 * Generates computer moves.
 * 
 * @author Team AFK
 * @version 1.1
 * 
 */

public interface MoveStrategy {

	/**
	 * 
	 * Generates a move by a particular algorithm, without adding it to the
	 * goban. No particular checks are made on it. It could be a very good
	 * guess, or a terrible one.
	 * 
	 * @param game
	 *            the current game
	 * @return a proposed move
	 */
	Move getMove(Game game);

	/**
	 * 
	 * Generates a move by a particular algorithm, and adds it to the goban. It
	 * checks if the move is correct.
	 * 
	 * @deprecated as of version 1.1, in favor of {@link getMove}
	 * @param game
	 *            the current game
	 * @return the move made
	 */
	Move playMove(Game game);

}

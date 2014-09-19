package utility;

import model.Goban;
import model.Player;
import model.Territories;
import model.Territory;
import run.console.RunConsole;
import run.console.RunConsolePlayers;
import engine.DisplayGobanVisitor;
import engine.IntersectionVisitor;
import engine.IsWhitePlayerVisitor;
import engine.PlayerVisitor;
import engine.SearchBlackTerritoriesVisitor;
import engine.SearchWhiteTerritoriesVisitor;
import game.Game;
import game.IntersectionNotOccupiedException;
import game.OutOfGobanException;

/**
 * 
 * Utility providing ways to properly run in console mode.
 * 
 * @author Team AFK
 * @version 1.0
 * @see {@link RunConsole} and {@link RunConsolePlayers}
 * 
 */

public class RunConsoleUtility {

	/**
	 * 
	 * Checks if a <code>player</code> is white
	 * 
	 * @param player
	 *            the player to test
	 * @return true if <code>player</code> is a white player
	 */
	public static Boolean isWhitePlayer(Player player) {
		PlayerVisitor<Boolean> visitor = new IsWhitePlayerVisitor();
		return player.accept(visitor);
	}

	/**
	 * 
	 * Searches and displays territories attached to each player
	 * 
	 * @param game
	 *            the current game
	 * @see {@link Territory} and {@link Territories}
	 */
	public static void searchTerritoriesVisitor(Game game) {
		IntersectionVisitor<Territories> visitorFirstPlayer;
		IntersectionVisitor<Territories> visitorSecondPlayer;
		if (isWhitePlayer(game.getFirstPlayer())) {
			visitorFirstPlayer = new SearchWhiteTerritoriesVisitor();
			visitorSecondPlayer = new SearchBlackTerritoriesVisitor();
		} else {
			visitorFirstPlayer = new SearchBlackTerritoriesVisitor();
			visitorSecondPlayer = new SearchWhiteTerritoriesVisitor();
		}

		/*
		 * first player's territories
		 */
		Territories result = game.getGoban().accept(visitorFirstPlayer);
		System.out.println("\n==================== Detected "
				+ game.getFirstUser().getName() + "'s territories (Score : "
				+ game.getFirstPlayer().getCaptureCount()
				+ ") ====================");
		System.out.println(result.toStringWithJailed(game.getGoban()));
		System.out.println("\n");

		/*
		 * Second player's territories
		 */
		Territories resultComputer = game.getGoban()
				.accept(visitorSecondPlayer);
		System.out.println("==================== Detected "
				+ game.getSecondUser().getName() + "'s territories (Score : "
				+ game.getSecondPlayer().getCaptureCount()
				+ ") ====================");
		System.out.println(resultComputer.toStringWithJailed(game.getGoban()));
		System.out.println("\n");

	}

	/**
	 * 
	 * Detects and suppresses jailed territories for every player.
	 * <p>
	 * A visitor finds all territories in each color, then
	 * <code>Territories</code> selects jailed territories. These jailed
	 * territories are passed to <code>goban.captureIntersections()</code> to be
	 * permanently removed.
	 * 
	 * @param goban
	 *            the current goban of the game being played
	 * @return the number of intersections captured
	 * @see {@link SearchWhiteTerritoriesVisitor},
	 *      {@link SearchBlackTerritoriesVisitor} and {@link Territories}
	 */
	public static int deleteJailedTerritories(Goban goban) {
		IntersectionVisitor<Territories> visitorWhite = new SearchWhiteTerritoriesVisitor();
		IntersectionVisitor<Territories> visitorBlack = new SearchBlackTerritoriesVisitor();
		int captureCount = 0;

		/*
		 * Get territories
		 */
		Territories resultWhite = goban.accept(visitorWhite);
		Territories resultBlack = goban.accept(visitorBlack);

		/*
		 * White
		 */
		Territories jailedWhite = resultWhite.getJailedTerritories(goban);
		try {
			goban.captureIntersections(jailedWhite);
			captureCount += jailedWhite.getIntersectionCount();
		} catch (OutOfGobanException e) {
			System.err.println(e.getMessage());
		} catch (IntersectionNotOccupiedException e) {
			System.err.println(e.getMessage());
		}

		/*
		 * Black
		 */
		Territories jailedBlack = resultBlack.getJailedTerritories(goban);
		try {
			goban.captureIntersections(jailedBlack);
			captureCount += jailedBlack.getIntersectionCount();
		} catch (OutOfGobanException e) {
			System.err.println(e.getMessage());
		} catch (IntersectionNotOccupiedException e) {
			System.err.println(e.getMessage());
		}

		return captureCount;

	}

	/**
	 * 
	 * Displays the current goban using a visitor
	 * 
	 * @param game
	 *            the current game
	 * @see <code>{@link DisplayGobanVisitor}</code>
	 */
	public static void tryDisplayVisitor(Game game) {
		IntersectionVisitor<String> visitor = new DisplayGobanVisitor();
		game.getGoban().accept(visitor);
		String header = "      ";
		String horizontalLine = "      ";
		int size = game.getGoban().getSize();
		char maxChar = (char) ('a' + size);
		for (char column = 'a'; column < maxChar; column++) {
			header += String.format("%c", column) + " ";
			horizontalLine += "--";
		}
		System.out.println("\n" + header + "\n" + horizontalLine
				+ visitor.getOutput() + "\n");
	}
}
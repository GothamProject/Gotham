package run.console;

import game.Game;
import game.IntersectionAlreadyOccupiedException;
import game.OutOfGobanException;
import game.SuicideException;
import ia.MoveStrategy;
import ia.RandomPlusMoveStrategy;

import java.util.Scanner;

import model.BlackIntersection;
import model.BlackPlayer;
import model.ComputerUser;
import model.Goban;
import model.HumanUser;
import model.IncorrectGobanSizeException;
import model.Intersection;
import model.Player;
import model.User;
import model.WhiteIntersection;
import model.WhitePlayer;
import run.gui.GameClock;
import time.Countdown;
import utility.DecypherInputUtility;
import utility.DecypheringFailureException;
import utility.RunConsoleUtility;

/*
 * XXX Need to create a real main with a mini menu,
 * chosing what type of game we want (HvsC, HvsH, load a game)
 */

public class RunConsole {

	private static Player humanPlayer;
	private static Player computerPlayer;
	private static User human;
	private static User computer;
	private static Game game;
	//private static MoveStrategy moveStrategy = new RandomMoveStrategy();
	private static MoveStrategy moveStrategy = new RandomPlusMoveStrategy();
	
	private static Countdown blackGameTime = new Countdown();
	private static Countdown blackByoyomiTime = new Countdown();
	private static Countdown whiteGameTime = new Countdown();
	private static Countdown whiteByoyomiTime = new Countdown();
	private static GameClock blackClock = new GameClock(blackGameTime, blackByoyomiTime);
	private static GameClock whiteClock = new GameClock(whiteGameTime, whiteByoyomiTime);

	// TODO add choice of difficulty, when multiple IA are available

	public static void main(String[] args) {

		/*
		 * Initializing console entry system
		 */
		String input;
		Scanner scanIn = new Scanner(System.in);

		/*
		 * Creating computer and welcoming user
		 */
		computer = new ComputerUser();
		System.out.println("Welcome in Virtual Goban, I'm "
				+ computer.getName() + " and I'll be your host.");
		try {

			System.out.println("Please choose your name, user :");

			/*
			 * Creating user from given username
			 */
			input = scanIn.nextLine();
			human = new HumanUser(input);

			/*
			 * Initializing goban, players and game
			 */
			System.out.println("What's your color of choice " + human.getName()
					+ " ? (w, b)");
			input = scanIn.nextLine();
			while (!input.equalsIgnoreCase("w") && !input.equalsIgnoreCase("b")) {
				System.out
						.println("You are stupid, "
								+ human.getName()
								+ ". Enter w to play the white stones, or b to play the black stones.");
				input = scanIn.nextLine();
			}
			if (input.equals("b")) {
				humanPlayer = new BlackPlayer(blackClock);
				computerPlayer = new WhitePlayer(whiteClock);
			} else {
				humanPlayer = new WhitePlayer(whiteClock);
				computerPlayer = new BlackPlayer(blackClock);
			}
			System.out.println("Now, select the goban's size : ");
			input = scanIn.nextLine();
			Goban goban = new Goban(Integer.valueOf(input));
			game = new Game(computer, human, computerPlayer, humanPlayer, goban);

			// game=Game.loadFile("2014-02-25--23-24-35.sgf");
			// game=Game.loadFile("2014-05-15--11-13-45.sgf");

			System.out
					.println("Thank you for your cooperation "
							+ human.getName()
							+ ". You will be perfect for this.\nLet's begin, shall we ?");

			input = "";
			Boolean humanPlayed = false;
			Boolean firstRound = true;

			while (!input.equalsIgnoreCase("q")
					&& !input.equalsIgnoreCase("quit")) {

				humanPlayed = false;
				if (!input.isEmpty()) {
					if (input.equalsIgnoreCase("p")
							|| input.equalsIgnoreCase("pass")) {
						humanPlayed = true;
						firstRound = false;
						computerPlayer.addToCaptureCount(1);

					} else {
						/*
						 * Decyphering input into coordinates
						 */

						try {

							int xCoordinate = DecypherInputUtility.decypher(
									input, "x");
							int yCoordinate = DecypherInputUtility.decypher(
									input, "y");

							/*
							 * Adding a move
							 */
							try {

								if (RunConsoleUtility
										.isWhitePlayer(humanPlayer)) {
									Intersection intersection = new WhiteIntersection(
											xCoordinate, yCoordinate);
									game.addMove(humanPlayer, intersection);
								} else {
									Intersection intersection = new BlackIntersection(
											xCoordinate, yCoordinate);
									game.addMove(humanPlayer, intersection);
								}

								humanPlayed = true;
								firstRound = false;

							} catch (IntersectionAlreadyOccupiedException e) {
								System.err
										.println("Sorry, but this intersection ("
												+ xCoordinate
												+ ","
												+ yCoordinate
												+ ") is already occupied. Try again.");
								humanPlayed = false;
							} catch (OutOfGobanException e) {
								System.err
										.println("("
												+ xCoordinate
												+ ","
												+ yCoordinate
												+ ") is way too far from the goban. Please, "
												+ "try to do better now.");
								humanPlayed = false;
							} catch (SuicideException e) {
								System.err
										.println("You can't play on ("
												+ xCoordinate
												+ ","
												+ yCoordinate
												+ ") or you will lose your stone! Please try another move.");
								humanPlayed = false;
							}

						} catch (DecypheringFailureException e) {
							/*
							 * if decyphering fails
							 */
							System.err
									.println("These are not right coordinates. You can't fool me. Please enter valid coordinates : ");
							firstRound = false;
							humanPlayed = false;
						}
						/*
						 * Computer turn
						 */

					}
					if (humanPlayed) {
						moveStrategy.playMove(game);
					}
				}

				/*
				 * computer plays first if he is black
				 */
				if (firstRound && RunConsoleUtility.isWhitePlayer(humanPlayer)) {
					moveStrategy.playMove(game);
				}

				/*
				 * Searching territories / displaying result for debug purposes
				 */
				RunConsoleUtility.searchTerritoriesVisitor(game);

				/*
				 * Displaying goban
				 */
				RunConsoleUtility.tryDisplayVisitor(game);

				/*
				 * Prompting for new move
				 */
				System.out.println("Play your move, " + human.getName());
				input = scanIn.nextLine();
			}

			System.out.println("The game is over !");
			
			game.countPoints();

			// Territories resultFree = game.getGoban().accept(
			// new SearchFreeTerritoriesVisitor());
			// // System.out
			// //
			// .println("==================== Detected free territories ====================");
			// //
			// System.out.println(resultFree.toStringWithJailed(game.getGoban()));
			// int i = 0;
			// Territory territory;
			// Boolean surroundedByFirst;
			// Boolean surroundedBySecond;
			// while (i != resultFree.getTerritoriesCount()) {
			// territory = resultFree.getTerritoryByIndex(i);
			// surroundedByFirst = resultFree.isSurroundedByColor(territory,
			// goban, game.getFirstPlayer());
			// surroundedBySecond = resultFree.isSurroundedByColor(territory,
			// goban, game.getSecondPlayer());
			// if (surroundedByFirst && !surroundedBySecond) {
			// System.out.println("Territory captured by "
			// + game.getFirstUser().getName() + " : "
			// + territory.getIntersectionsCount());
			// } else if (!surroundedByFirst && surroundedBySecond) {
			// System.out.println("Territory captured by "
			// + game.getSecondUser().getName() + " : "
			// + territory.getIntersectionsCount());
			// }
			// i++;
			// }

			System.out.println(human.getName() + "'s score : "
					+ String.valueOf(humanPlayer.getCaptureCount()));
			System.out.println(computer.getName() + "'s score : "
					+ String.valueOf(computerPlayer.getCaptureCount()));
			System.out.println("Do you want to save the game?");
			input = scanIn.nextLine();
			if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
				String savedGame;
				savedGame = game.exportToSGF();
				System.out.println("The game was successfuly saved as a file "
						+ savedGame);
			}
			System.out.println("Goodbye, " + human.getName() + ".");

		} catch (IncorrectGobanSizeException e) {
			System.out.println(e.getMessage());
		}

		scanIn.close();

	}
}
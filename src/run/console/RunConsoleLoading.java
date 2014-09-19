package run.console;

import engine.IsHumanUserVisitor;
import game.Game;
import game.IntersectionAlreadyOccupiedException;
import game.OutOfGobanException;
import game.SuicideException;
import ia.MoveStrategy;
import ia.RandomPlusMoveStrategy;

import java.util.Scanner;

import model.BlackIntersection;
import model.ComputerUser;
import model.Intersection;
import model.Player;
import model.User;
import model.WhiteIntersection;
import utility.DecypherInputUtility;
import utility.DecypheringFailureException;
import utility.RunConsoleUtility;

public class RunConsoleLoading {

	private static Player humanPlayer;
	private static Player computerPlayer;
	private static User human;
	private static User computer;
	private static Game game;
	// private static MoveStrategy moveStrategy = new RandomMoveStrategy();
	private static MoveStrategy moveStrategy = new RandomPlusMoveStrategy();

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
		System.out.println("Welcome in Virtual Goban.\n"
				+ "Please enter the name of the file to be loaded.\n"
				+ "This must be a game between a Computer and a Human.");
		input = scanIn.nextLine();
		while (!input.contains(".sgf")) {
			// TODO file verification needed
			System.out
					.println("Sorry, this is not an SGF file. Please enter the name again :");
			input = scanIn.nextLine();
		}
		game = Game.loadFile(input);
		if (game.getFirstUser().accept(new IsHumanUserVisitor())) {
			human = game.getFirstUser();
			humanPlayer = game.getFirstPlayer();
			computer = game.getSecondUser();
			computerPlayer = game.getSecondPlayer();

		} else {
			human = game.getSecondUser();
			humanPlayer = game.getSecondPlayer();
			computer = game.getFirstUser();
			computerPlayer = game.getFirstPlayer();
		}

		System.out.println("Thank you for your cooperation " + human.getName()
				+ ". You will be perfect for this.\nLet's begin, shall we ?");

		input = "";
		Boolean humanPlayed = false;
		Boolean firstRound = true;

		while (!input.equalsIgnoreCase("q") && !input.equalsIgnoreCase("quit")) {

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

						int xCoordinate = DecypherInputUtility.decypher(input,
								"x");
						int yCoordinate = DecypherInputUtility.decypher(input,
								"y");

						/*
						 * Adding a move
						 */
						try {

							if (RunConsoleUtility.isWhitePlayer(humanPlayer)) {
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
							System.err.println("Sorry, but this intersection ("
									+ xCoordinate + "," + yCoordinate
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

		scanIn.close();

	}
}
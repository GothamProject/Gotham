package run.console;

import game.Game;
import game.IntersectionAlreadyOccupiedException;
import game.OutOfGobanException;
import game.PlayerNotInGameException;
import game.SuicideException;

import java.util.Scanner;

import model.BlackIntersection;
import model.Intersection;
import model.Player;
import model.User;
import model.WhiteIntersection;
import utility.DecypherInputUtility;
import utility.DecypheringFailureException;
import utility.RunConsoleUtility;

public class RunConsolePlayersLoading {

	private static Game game;

	public static void main(String[] args) {

		String input;
		Scanner scanIn = new Scanner(System.in);
		Player firstPlayer;
		Player secondPlayer;
		Player currentPlayer;

		System.out.println("Welcome to Virtual Goban.\n"
				+ "Please enter the name of the file to be loaded.");
		input = scanIn.nextLine();
		while (!input.contains(".sgf")) {
			// TODO file verification needed
			System.out
					.println("Sorry, this is not an SGF file. Please enter the name again :");
			input = scanIn.nextLine();
		}
		game = Game.loadFile(input);
		// if (game.getFirstUser().accept(new IsHumanUserVisitor())) {
		// human = game.getFirstUser();
		// humanPlayer = game.getFirstPlayer();
		// computer = game.getSecondUser();
		// computerPlayer = game.getSecondPlayer();
		//
		// } else {
		// human = game.getSecondUser();
		// humanPlayer = game.getSecondPlayer();
		// computer = game.getFirstUser();
		// computerPlayer = game.getFirstPlayer();
		// }

		User firstUser = game.getFirstUser();

		User secondUser = game.getSecondUser();

		try {
			firstPlayer = game.getFirstPlayer();
			secondPlayer = game.getSecondPlayer();

			System.out.println("Good, now let's begin the game.");

			input = "";
			Boolean firstPlayerTurn;

			if (RunConsoleUtility.isWhitePlayer(firstPlayer)) {
				firstPlayerTurn = false;
				currentPlayer = secondPlayer;
			} else {
				firstPlayerTurn = true;
				currentPlayer = firstPlayer;
			}

			while (!input.equalsIgnoreCase("q")
					&& !input.equalsIgnoreCase("quit")) {

				if (!input.isEmpty()) {

					if (input.equalsIgnoreCase("p")
							|| input.equalsIgnoreCase("pass")) {
						/*
						 * toggle user
						 */
						firstPlayerTurn = toggleBoolean(firstPlayerTurn);

						/*
						 * change current player
						 */

						if (firstPlayerTurn) {
							currentPlayer = game.getFirstPlayer();
							currentPlayer.addToCaptureCount(1);
						} else {
							currentPlayer = game.getSecondPlayer();
							currentPlayer.addToCaptureCount(1);
						}
					} else {

						try {

							int xCoordinate = DecypherInputUtility.decypher(
									input, "x");

							int yCoordinate = DecypherInputUtility.decypher(
									input, "y");

							/*
							 * adding move
							 */

							try {

								if (RunConsoleUtility
										.isWhitePlayer(currentPlayer)) {
									Intersection intersection = new WhiteIntersection(
											xCoordinate, yCoordinate);
									game.addMove(firstPlayer, intersection);
								} else {
									Intersection intersection = new BlackIntersection(
											xCoordinate, yCoordinate);
									game.addMove(firstPlayer, intersection);
								}

								System.out.println(game.getUserByPlayer(
										currentPlayer).getName()
										+ " played");

								/*
								 * toggle user
								 */
								firstPlayerTurn = toggleBoolean(firstPlayerTurn);

								/*
								 * change current player
								 */

								if (firstPlayerTurn) {
									currentPlayer = game.getFirstPlayer();
								} else {
									currentPlayer = game.getSecondPlayer();
								}

							} catch (IntersectionAlreadyOccupiedException e) {
								System.err
										.println("Sorry, but this intersection ("
												+ xCoordinate
												+ ","
												+ yCoordinate
												+ ") is already occupied. Try again.");
							} catch (OutOfGobanException e) {
								System.err
										.println("("
												+ xCoordinate
												+ ","
												+ yCoordinate
												+ ") is way too far from the goban. Please, "
												+ "try to do better now.");
							} catch (SuicideException e) {

								System.err
										.println("You can't play on ("
												+ xCoordinate
												+ ","
												+ yCoordinate
												+ ") or you will lose your stone! Please try another move.");
							} catch (PlayerNotInGameException e) {
								System.err.println(e.getMessage());
							}

						} catch (DecypheringFailureException e) {
							/*
							 * if decyphering fails
							 */
							System.err
									.println("These are not right coordinates. You can't fool me. Please enter valid coordinates : ");
						}
					}
				}

				RunConsoleUtility.searchTerritoriesVisitor(game);
				// RunConsoleUtility.deleteJailedTerritories(game);
				RunConsoleUtility.tryDisplayVisitor(game);

				System.out.println(game.getUserByPlayer(currentPlayer)
						.getName() + ", please enter coordinates.");

				input = scanIn.nextLine();
			}
			System.out.println("The game is over !");
			System.out.println(firstUser.getName() + "'s score : "
					+ String.valueOf(firstPlayer.getCaptureCount()));
			System.out.println(secondUser.getName() + "'s score : "
					+ String.valueOf(secondPlayer.getCaptureCount()));
			System.out.println("Do you want to save the game?");
			input = scanIn.nextLine();
			if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
				String savedGame;
				savedGame = game.exportToSGF();
				System.out.println("The game was successfuly saved as file "
						+ savedGame);

			}
			System.out.println("Goodbye, " + firstUser.getName() + " and "
					+ secondUser.getName() + ".");

		} catch (PlayerNotInGameException e) {
			System.err.println(e.getMessage());
		}

		scanIn.close();

	}

	private static Boolean toggleBoolean(Boolean bool) {
		if (bool == true) {
			return false;
		} else {
			return true;
		}
	}

}
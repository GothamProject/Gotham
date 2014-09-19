package run.console;

import game.Game;
import game.IntersectionAlreadyOccupiedException;
import game.OutOfGobanException;
import game.PlayerNotInGameException;
import game.SuicideException;

import java.util.Scanner;

import model.BlackIntersection;
import model.BlackPlayer;
import model.Goban;
import model.HumanUser;
import model.IncorrectGobanSizeException;
import model.Intersection;
import model.Player;
import model.User;
import model.WhiteIntersection;
import model.WhitePlayer;
import utility.DecypherInputUtility;
import utility.DecypheringFailureException;
import utility.RunConsoleUtility;

public class RunConsolePlayers {

	private static Game game;

	public static void main(String[] args) {

		String input;
		Scanner scanIn = new Scanner(System.in);
		Player firstPlayer;
		Player secondPlayer;
		Player currentPlayer;

		System.out.println("First user, please choose your name : ");

		input = scanIn.nextLine();

		User firstUser = new HumanUser(input);

		System.out.println("Second user, please choose your name : ");

		input = scanIn.nextLine();

		User secondUser = new HumanUser(input);

		try {

			System.out.println(firstUser.getName()
					+ ", what's your color of choice ? (w or b)");
			input = scanIn.nextLine();
			while (!input.equalsIgnoreCase("w") && !input.equalsIgnoreCase("b")) {
				System.out
						.println("You are stupid, "
								+ firstUser.getName()
								+ ". Enter w to play the white stones, or b to play the black stones.");
				input = scanIn.nextLine();
			}

			if (input.equals("w")) {
				firstPlayer = new WhitePlayer();
				secondPlayer = new BlackPlayer();
			} else {
				firstPlayer = new BlackPlayer();
				secondPlayer = new WhitePlayer();
			}

			System.out.println("Now, select the goban's size : ");
			input = scanIn.nextLine();
			Goban goban = new Goban(Integer.valueOf(input));

			/*
			 * Game creation
			 */

			game = new Game(firstUser, secondUser, firstPlayer, secondPlayer,
					goban);

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

		} catch (IncorrectGobanSizeException e) {
			System.err.println(e.getMessage());
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
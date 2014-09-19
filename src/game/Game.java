package game;

import ia.MoveStrategy;
import ia.RandomMoveStrategy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import logger.LoggerUtility;
import model.BlackIntersection;
import model.BlackPlayer;
import model.CannotCreateDirectoryException;
import model.ComputerUser;
import model.FreeIntersection;
import model.Goban;
import model.HumanUser;
import model.IncorrectGobanSizeException;
import model.Intersection;
import model.Player;
import model.Territories;
import model.Territory;
import model.User;
import model.WhiteIntersection;
import model.WhitePlayer;

import org.apache.log4j.Logger;

import run.gui.GameClock;
import run.gui.GamePanel;
import run.gui.MainMenu;
import time.Countdown;
import utility.DecypherInputUtility;
import utility.DecypheringFailureException;
import engine.IsBlackIntersectionVisitor;
import engine.IsBlackPlayerVisitor;
import engine.IsComputerUserVisitor;
import engine.IsHumanUserVisitor;
import engine.IsWhitePlayerVisitor;
import engine.SearchFreeTerritoriesVisitor;
import engine.UserVisitor;

/**
 * A Go game with players, users, timer, komi and goban. Manages all the objects
 * needed for a game to be played.
 * 
 * @author Team AFK
 * @version 1.0
 */

public class Game {

	private static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd--HH-mm-ss";

	private static final String SIMPLE_DATE_FORMAT_SGF = "yyyy-MM-dd";

	private static final String GAME_SAVES_FOLDER = "./program_datas/";

	// private static final String KOMI_FIVE = "5.5";
	//
	// private static final String KOMI_SIX = "6.5";
	//
	// private static final String KOMI_SEVEN = "7.5";

	private User firstUser;

	private User secondUser;

	private Player firstPlayer;

	private Player secondPlayer;

	private Boolean firstPlayerTurn;

	private Boolean previousMoveWasAPass;

	private int computerMoveCount;

	private Goban goban;

	private List<Move> history;

	private MoveStrategy firstMoveStrategy;

	private MoveStrategy secondMoveStrategy;

	private double komi;

	private static Logger logger = LoggerUtility.getLogger(Game.class);

	public Game(User firstUser, User secondUser, Player firstPlayer,
			Player secondPlayer, Goban goban, double komi) {

		this.firstUser = firstUser;
		this.secondUser = secondUser;
		this.firstPlayer = firstPlayer;
		this.secondPlayer = secondPlayer;
		history = new ArrayList<Move>();
		this.goban = goban;
		this.komi = komi;

		/*
		 * firstPlayerTurn is initialized with right color
		 */
		if (firstPlayer.accept(new IsBlackPlayerVisitor())) {
			firstPlayerTurn = true;
		} else {
			firstPlayerTurn = false;
		}
		previousMoveWasAPass = false;
		computerMoveCount = 0;

		/*
		 * MoveStrategy is initialized by default;
		 */
		firstMoveStrategy = new RandomMoveStrategy();
		secondMoveStrategy = new RandomMoveStrategy();

	}

	public Game(User firstUser, User secondUser, Player firstPlayer,
			Player secondPlayer, Goban goban) {

		this(firstUser, secondUser, firstPlayer, secondPlayer, goban, 5.5);

	}

	public Game(User firstUser, User secondUser, Player firstPlayer,
			Player secondPlayer, Goban goban, double komi,
			MoveStrategy firstMoveStrategy, MoveStrategy secondMoveStrategy) {

		this(firstUser, secondUser, firstPlayer, secondPlayer, goban, komi);

		this.firstMoveStrategy = firstMoveStrategy;
		this.secondMoveStrategy = secondMoveStrategy;

	}

	public Goban getGoban() {
		return goban;
	}

	public List<Move> getHistory() {
		return history;
	}

	public MoveStrategy getFirstMoveStrategy() {
		return firstMoveStrategy;
	}

	public MoveStrategy getSecondMoveStrategy() {
		return secondMoveStrategy;
	}

	public MoveStrategy getCurrentMoveStrategy() {
		if (firstPlayerTurn) {
			return firstMoveStrategy;
		} else {
			return secondMoveStrategy;
		}
	}

	public User getFirstUser() {
		return firstUser;
	}

	public User getSecondUser() {
		return secondUser;
	}

	public User getCurrentUser() {
		if (firstPlayerTurn) {
			return firstUser;
		} else {
			return secondUser;
		}
	}

	public User getNotCurrentUser() {
		if (firstPlayerTurn) {
			return secondUser;
		} else {
			return firstUser;
		}
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public Player getCurrentPlayer() {
		if (firstPlayerTurn) {
			return firstPlayer;
		} else {
			return secondPlayer;
		}
	}

	public void setCurrentPlayerTurn(Player player) {
		if (firstPlayerTurn) {
			if (player == firstPlayer) {
				toggleCurrentPlayer();
			}
		} else {
			if (player == secondPlayer) {
				toggleCurrentPlayer();
			}
		}
	}

	public int getComputerMoveCount() {
		return computerMoveCount;
	}

	public void setComputerMoveCount(int count) {
		computerMoveCount = count;
	}

	/**
	 * Gets the player which is not currently playing. Used to handle
	 * {@link OffensiveSuicideException} in graphical mode.
	 * 
	 * @return the player not playing
	 */
	public Player getNotCurrentPlayer() {
		if (firstPlayerTurn) {
			return secondPlayer;
		} else {
			return firstPlayer;
		}
	}

	public double getKomi() {
		return komi;
	}

	// public Countdown getGameCountdown() {
	// return gameCountdown;
	// }
	//
	// public void decrementGameCountdown() {
	// gameCountdown.decrement();
	// }
	//
	// public Countdown getMoveCountdown() {
	// return moveCountdown;
	// }
	//
	// public Countdown getBlackMoveCountdown() {
	// return blackMoveCountdown;
	// }
	//
	// public Countdown getWhiteMoveCountdown() {
	// return whiteMoveCountdown;
	// }
	//
	// public void decrementMoveCountdown() {
	// moveCountdown.decrement();
	// }

	public Player getPlayerByUser(User user) throws UserNotInGameException {
		if (firstUser.equals(user)) {
			return getFirstPlayer();
		} else if (secondUser.equals(user)) {
			return getSecondPlayer();
		} else {
			throw new UserNotInGameException(user);
		}
	}

	public User getUserByPlayer(Player player) throws PlayerNotInGameException {
		if (firstPlayer.equals(player)) {
			return getFirstUser();
		} else if (secondPlayer.equals(player)) {
			return getSecondUser();
		} else {
			throw new PlayerNotInGameException("error 256");
		}
	}

	public Player getComputerPlayer() throws ComputerNotInGameException {
		UserVisitor<Boolean> visitor = new IsComputerUserVisitor();
		if (firstUser.accept(visitor)) {
			return getFirstPlayer();
		} else if (secondUser.accept(visitor)) {
			return getSecondPlayer();
		} else {
			throw new ComputerNotInGameException();
		}
	}

	public Player getHumanPlayer() throws UserNotInGameException {
		UserVisitor<Boolean> visitor = new IsHumanUserVisitor();
		if (firstUser.accept(visitor)) {
			return getFirstPlayer();
		} else if (secondUser.accept(visitor)) {
			return getSecondPlayer();
		} else {
			throw new UserNotInGameException(new HumanUser("human"));
		}
	}

	public User getHumanUser() {
		UserVisitor<Boolean> visitor = new IsHumanUserVisitor();
		if (firstUser.accept(visitor)) {
			return getFirstUser();
		} else if (secondUser.accept(visitor)) {
			return getSecondUser();
		} else {
			/*
			 * TODO better solution to be found
			 */
			return new HumanUser("Unknown");
		}
	}

	public Move getLastMove() throws HistoryIsEmptyException {
		if (history.size() != 0) {
			return history.get(history.size() - 1);
		} else {
			throw new HistoryIsEmptyException();
		}
	}

	/**
	 * If the <code>Move</code> is valid, add a stone to the <code>Goban</code>
	 * and to the history of the game.
	 * 
	 * @param player
	 *            - The <code>Player</code> who adds the stone
	 * @param intersection
	 *            - The <code>Intersection</code> to be added
	 * @throws IntersectionAlreadyOccupiedException
	 *             when a stone was already positioned here
	 * @throws SuicideException
	 *             when playing here will cause the stone to be jailed
	 * @throws OutOfGobanException
	 *             when coordinates are too high or low
	 * @throws OffensiveSuicideException
	 *             when a suicide is helpful to the player
	 * @since 1.0
	 */

	public Territories addMove(Player player, Intersection intersection)
			throws IntersectionAlreadyOccupiedException, SuicideException,
			OutOfGobanException {

		/*
		 * We try to add the intersection, and get in return the captured
		 * territories
		 */
		Territories capturedTerritories = goban
				.updateIntersection(intersection);
		int captureCount = capturedTerritories.getIntersectionCount();

		/*
		 * Creating a move and adding it to history
		 */
		Move move = new Move(player, intersection, captureCount);
		history.add(move);

		/*
		 * Player update
		 */
		player.addToCaptureCount(captureCount);
		toggleCurrentPlayer();

		return capturedTerritories;
	}

	/**
	 * If the <code>Move</code> is valid, add a stone to the <code>Goban</code>
	 * and to the history of the game.
	 * <p>
	 * Used only in {@link MainMenu} when the load is made.
	 * 
	 * @param player
	 *            - The <code>Player</code> who adds the stone
	 * @param intersection
	 *            - The <code>Intersection</code> to be added
	 * @throws IntersectionAlreadyOccupiedException
	 *             when a stone was already positioned here
	 * @throws SuicideException
	 *             when playing here will cause the stone to be jailed
	 * @throws OutOfGobanException
	 *             when coordinates are too high or low
	 * @throws OffensiveSuicideException
	 *             when a suicide is helpful to the player
	 * @since 1.0
	 */

	public Territories addMoveFromHistory(Player player,
			Intersection intersection)
			throws IntersectionAlreadyOccupiedException, SuicideException,
			OutOfGobanException {

		/*
		 * We try to add the intersection, and get in return the captured
		 * territories
		 */
		Territories capturedTerritories = goban
				.updateIntersection(intersection);
		int captureCount = capturedTerritories.getIntersectionCount();

		/*
		 * Creating a move and adding it to history
		 */
		Move move = new Move(player, intersection, captureCount);
		history.add(move);

		/*
		 * Player update
		 */
		toggleCurrentPlayer();

		return capturedTerritories;
	}

	/**
	 * Used when a player wish not to play.
	 */
	public void passMove() {
		/*
		 * We check that the previous move was not already a pass
		 */
		if (previousMoveWasAPass) {
			GamePanel.endGame();
			System.out.println("End of game");
		} else {
			toggleCurrentPlayer();
			getCurrentPlayer().addToCaptureCount(1);
			previousMoveWasAPass = true;
		}
	}

	/**
	 * Cancels the last <code>Move</code>.
	 * 
	 * @throws IntersectionNotOccupiedException
	 * @throws OutOfGobanException
	 * 
	 * @since 1.0
	 */

	public void cancelMove() throws OutOfGobanException,
			IntersectionNotOccupiedException {

		Intersection intersection = history.get(history.size() - 1)
				.getIntersection();
		history.remove(history.size() - 1);
		goban.captureIntersection(new FreeIntersection(intersection
				.getXCoordinate(), intersection.getYCoordinate()));

	}

	public int getFinalScoreByPlayer(Player player) {
		// TODO final score mechanics
		return 0;
	}

	public void countPoints() {
		// System.out
		// .println("==================== Detected free territories ====================");
		// System.out.println(toStringWithJailed(game.getGoban()));
		int i = 0;
		Territories freeTerritories = goban
				.accept(new SearchFreeTerritoriesVisitor());
		Territory territory;
		Boolean surroundedByFirst;
		Boolean surroundedBySecond;
		while (i != freeTerritories.getTerritoriesCount()) {
			territory = freeTerritories.getTerritoryByIndex(i);
			surroundedByFirst = freeTerritories.isSurroundedByColor(territory,
					goban, firstPlayer);
			surroundedBySecond = freeTerritories.isSurroundedByColor(territory,
					goban, secondPlayer);
			if (surroundedByFirst && !surroundedBySecond) {
				System.out.println("Territory captured by "
						+ firstUser.getName() + " : "
						+ territory.getIntersectionsCount() + "stones");
				firstPlayer
						.addToCaptureCount(territory.getIntersectionsCount());
			} else if (!surroundedByFirst && surroundedBySecond) {
				System.out.println("Territory captured by "
						+ secondUser.getName() + " : "
						+ territory.getIntersectionsCount() + "stones");
				secondPlayer.addToCaptureCount(territory
						.getIntersectionsCount());
			}
			i++;
		}

		if (firstPlayer.accept(new IsWhitePlayerVisitor())) {
			firstPlayer.addToCaptureCount(komi);
		} else {
			secondPlayer.addToCaptureCount(komi);
		}
	}

	@Override
	public String toString() {
		return "Game [firstUser=" + firstUser + ", secondUser=" + secondUser
				+ ", firstPlayer=" + firstPlayer + ", secondPlayer="
				+ secondPlayer + ", firstPlayerTurn=" + firstPlayerTurn
				+ ", goban=" + goban + ", history=" + history + ", komi="
				+ komi + "]";
	}

	/**
	 * Save a game in a SGF file using current date as a filename.
	 * 
	 * @return The name of the file created
	 * @see Game#exportToSGF(String)
	 * @since 1.0
	 */

	public String exportToSGF() {
		DateFormat dateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
		Date date = new Date();
		String fileName = dateFormat.format(date) + ".sgf";
		String output = "";
		// FIXME this (up) doesn't look good
		output = exportToSGF(fileName);
		return output;
	}

	/**
	 * Save a game in a SGF file - Smart Game Format - which is a standard
	 * format for games like Go or Chess. The information saved are, for
	 * example, the players' name, the komi, the time, the history of the game,
	 * etc.
	 * 
	 * @param fileName
	 *            - Name of the save file to be created
	 * @return The name of the file created
	 * @since 1.0
	 */

	public String exportToSGF(String fileName) {
		try {
			File directory = new File(GAME_SAVES_FOLDER);
			if (!directory.exists() && !directory.mkdirs()) {
				throw new CannotCreateDirectoryException(GAME_SAVES_FOLDER); // here
																				// the
																				// directory
																				// cannot
																				// be
																				// created
			}
			// directory.mkdirs();
			File saveFile = new File(GAME_SAVES_FOLDER + fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					saveFile));
			bufferedWriter.write("(;FF[4]GM[1]SZ["
					+ String.valueOf(goban.getSize()) + "]");
			bufferedWriter.newLine();
			if (firstPlayer.accept(new IsBlackPlayerVisitor())) {
				if (firstUser.accept(new IsComputerUserVisitor())) {
					bufferedWriter.write("PB[" + firstUser.getName()
							+ "]C[Computer]");
				} else {
					bufferedWriter.write("PB[" + firstUser.getName()
							+ "]C[Human]");
				}

				bufferedWriter.newLine();
				if (secondUser.accept(new IsComputerUserVisitor())) {
					bufferedWriter.write("PW[" + secondUser.getName()
							+ "]C[Computer]");
				} else {
					bufferedWriter.write("PW[" + secondUser.getName()
							+ "]C[Human]");
				}
			} else {
				if (firstUser.accept(new IsComputerUserVisitor())) {
					bufferedWriter.write("PW[" + firstUser.getName()
							+ "]C[Computer]");
				} else {
					bufferedWriter.write("PW[" + firstUser.getName()
							+ "]C[Human]");
				}

				bufferedWriter.newLine();
				if (secondUser.accept(new IsComputerUserVisitor())) {
					bufferedWriter.write("PB[" + secondUser.getName()
							+ "]C[Computer]");
				} else {
					bufferedWriter.write("PB[" + secondUser.getName()
							+ "]C[Human]");
				}
			}
			bufferedWriter.newLine();
			bufferedWriter.write("KM[" + String.valueOf(komi) + "]");
			// TODO UnauthorizedKomiException? Or sthg like that. Use
			// constants.
			bufferedWriter.newLine();
			DateFormat dateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT_SGF);
			Date date = new Date();
			bufferedWriter.write("DT[" + dateFormat.format(date) + "]");
			bufferedWriter.newLine();
			// TODO verify if time spent, time left or global time
			// bufferedWriter.write("TM["
			// + String.valueOf(countdown.convert()) + "]");
			// bufferedWriter.newLine();
			// TODO add rules
			bufferedWriter.write("RU[Japanese]");
			bufferedWriter.newLine();
			Player blackPlayer;
			Player whitePlayer;

			/*
			 * Check first and second players' color
			 */
			if (firstPlayer.accept(new IsBlackPlayerVisitor())) {
				blackPlayer = firstPlayer;
				whitePlayer = secondPlayer;
			} else {
				blackPlayer = secondPlayer;
				whitePlayer = firstPlayer;
			}

			bufferedWriter.write("TGB["
					+ blackPlayer.getGameClock().getGameCountdown().toString()
					+ "]");
			bufferedWriter.newLine();
			bufferedWriter.write("TGW["
					+ whitePlayer.getGameClock().getGameCountdown().toString()
					+ "]");
			bufferedWriter.newLine();
			bufferedWriter.write("TBB["
					+ blackPlayer.getGameClock().getByoyomiCountdown()
							.toString() + "]");
			bufferedWriter.newLine();
			bufferedWriter.write("TBW["
					+ whitePlayer.getGameClock().getByoyomiCountdown()
							.toString() + "]");
			bufferedWriter.newLine();
			bufferedWriter.newLine();

			// TODO Add White & Add Black - see SGF standard format

			IsBlackIntersectionVisitor blackVisitor = new IsBlackIntersectionVisitor();
			Move move;
			Intersection intersection;
			String output;
			for (Iterator<Move> iterator = history.iterator(); iterator
					.hasNext();) {
				move = iterator.next();
				intersection = move.getIntersection();
				if (intersection.accept(blackVisitor)) {
					output = ";B";
				} else {
					output = ";W";
				}
				output += "["
						+ convertCoordinate(intersection.getXCoordinate())
						+ convertCoordinate(intersection.getYCoordinate())
						+ "]";
				bufferedWriter.write(output);
			}
			bufferedWriter.write(")");
			bufferedWriter.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return fileName;
	}

	/**
	 * Loads a SGF file.
	 * 
	 * @param filename
	 *            - the file to be loaded
	 * @return The game retrieved from SGF datas
	 * @since 1.0
	 */

	public static Game loadFile(String filename) {
		File file;
		// First way - obsolete
		// if (filename.matches("(/)?.*")) {
		// file = new File(filename);
		// } else {
		// file = new File(GAME_SAVES_FOLDER + filename);
		// }

		// Second way - obsolete
		// File filefoo = new File(filename);
		// if (filefoo.isAbsolute()) {
		// file = filefoo;
		// } else {
		// file = new File(GAME_SAVES_FOLDER + filename);
		// }

		// Working with abstract path, has to be in the "./program_datas/"
		// folder
		file = new File(GAME_SAVES_FOLDER + filename);
		String input = "";
		Game game;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			Goban goban = null;
			User firstUser = null;
			User secondUser = null;

			Countdown blackGameTime = new Countdown();
			Countdown blackByoyomiTime = new Countdown(1, 0);

			Countdown whiteGameTime = new Countdown();
			Countdown whiteByoyomiTime = new Countdown(1, 0);

			GameClock blackClock = new GameClock(blackGameTime,
					blackByoyomiTime);
			GameClock whiteClock = new GameClock(whiteGameTime,
					whiteByoyomiTime);

			Player firstPlayer = new BlackPlayer(blackClock);
			Player secondPlayer = new WhitePlayer(whiteClock);

			double komi = 0;

			input = reader.readLine();
			while (!input.isEmpty()) {
				String tab[] = input.split("\\]");
				logger.debug("Input = " + input);
				for (int i = 0; i < tab.length; i++) {
					logger.debug("tab[" + i + "] = " + tab[i]);
					tab[i] = String.valueOf(tab[i]);

					// Boolean foo = tab[i].contains("SZ");
					// Boolean foo = tab[i].matches("SZ\\[(.*)");
					// logger.debug(foo.toString());
					if (tab[i].matches("(?i)(^SZ(.*))")) {
						logger.debug("Entering SZ verification");
						String[] fooString = tab[i].split("\\[");
						int size = Integer.valueOf(fooString[1]);
						logger.debug("value = " + fooString[1]);
						try {
							goban = new Goban(size);
						} catch (IncorrectGobanSizeException e) {
							System.err.println(e.getMessage());
						}
					}

					if (tab[i].contains("PB")) {
						logger.debug("Entering PB verification");
						String name;
						String playerChoice;
						String[] tabName = tab[i].split("\\[");
						name = tabName[1];
						logger.debug("value = " + tabName[1]);
						playerChoice = tab[1];

						if (playerChoice.contains("Human")) {
							firstUser = new HumanUser(name);
						} else if (playerChoice.contains("Computer")) {
							firstUser = new ComputerUser();
						}
					}

					if (tab[i].contains("PW")) {
						logger.debug("Entering PW verification");
						String name;
						String playerChoice;
						String[] tabName = tab[i].split("\\[");
						name = tabName[1];
						playerChoice = tab[1];
						logger.debug("value = " + tab[1]);
						if (playerChoice.contains("Human")) {
							secondUser = new HumanUser(name);
						} else if (playerChoice.contains("Computer")) {
							secondUser = new ComputerUser();
						}
					}

					if (tab[i].contains("KM")) {
						logger.debug("Entering KM verification");
						String[] fooTable = tab[i].split("\\[");
						String fooKomi = fooTable[1];
						komi = Double.valueOf(fooKomi);
						logger.debug("value = " + fooTable[1]);
					}

					if (tab[i].contains("TGB")) {
						logger.debug("Entering TGB verification");
						String[] fooTable = tab[i].split("\\[");
						String fooTime = fooTable[1];
						String[] fooTimeSplitted = fooTime.split(":");
						int min = Integer.valueOf(fooTimeSplitted[0]);
						int sec = Integer.valueOf(fooTimeSplitted[1]);
						blackGameTime = new Countdown(min, sec);
						logger.debug("TGB value = " + fooTable[1]);
					}

					if (tab[i].contains("TGW")) {
						logger.debug("Entering TGW verification");
						String[] fooTable = tab[i].split("\\[");
						String fooTime = fooTable[1];
						String[] fooTimeSplitted = fooTime.split(":");
						int min = Integer.valueOf(fooTimeSplitted[0]);
						int sec = Integer.valueOf(fooTimeSplitted[1]);
						whiteGameTime = new Countdown(min, sec);
						logger.debug("TGW value = " + fooTable[1]);
					}

					if (tab[i].contains("TBB")) {
						logger.debug("Entering TBB verification");
						String[] fooTable = tab[i].split("\\[");
						String fooTime = fooTable[1];
						String[] fooTimeSplitted = fooTime.split(":");
						int min = Integer.valueOf(fooTimeSplitted[0]);
						int sec = Integer.valueOf(fooTimeSplitted[1]);
						blackByoyomiTime = new Countdown(min, sec);
						logger.debug("TBB value = " + fooTable[1]);
					}

					if (tab[i].contains("TBW")) {
						logger.debug("Entering TBW verification");
						String[] fooTable = tab[i].split("\\[");
						String fooTime = fooTable[1];
						String[] fooTimeSplitted = fooTime.split(":");
						int min = Integer.valueOf(fooTimeSplitted[0]);
						int sec = Integer.valueOf(fooTimeSplitted[1]);
						whiteByoyomiTime = new Countdown(min, sec);
						logger.debug("TBW value = " + fooTable[1]);
					}

					// if (tab[i].contains("RU")) {
					// TODO rules
					// }

				}
				input = reader.readLine();

			}
			logger.debug("First user = " + firstUser.toString());
			logger.debug("Second user = " + secondUser.toString());
			logger.debug("First player = " + firstPlayer.toString());
			logger.debug("Second player = " + secondPlayer.toString());
			logger.debug("Goban = " + goban.toString());
			logger.debug("Komi = " + komi);
			// game = new Game(firstUser, secondUser, firstPlayer, secondPlayer,
			// goban, komi);

			blackClock.setGameCountdown(blackGameTime);
			blackClock.setByoyomiCountdown(blackByoyomiTime);

			whiteClock.setGameCountdown(whiteGameTime);
			whiteClock.setByoyomiCountdown(whiteByoyomiTime);

			firstPlayer.setGameClock(blackClock);
			secondPlayer.setGameClock(whiteClock);

			game = new Game(firstUser, secondUser, firstPlayer, secondPlayer,
					goban, komi);
			reader.close();
			logger.debug("BufferedReader closed");

			game = readMovesFromSGF(filename, game);

		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			game = null;
		} catch (IOException e) {
			System.err.println(e.getMessage());
			game = null;
		}
		return game;
	}

	private static Game readMovesFromSGF(String filename, Game game) {
		File file = new File(GAME_SAVES_FOLDER + filename);
		String input = "";

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			input = reader.readLine();
			while ((!input.contains("W") || input.contains("PW"))
					|| (!input.contains("B") || input.contains("PB"))) {
				input = reader.readLine();
				logger.debug("Input = " + input);
			}
			while (input != null) {
				String tab[] = input.split("\\]");
				logger.debug("Input = " + input);
				for (int i = 0; i < tab.length; i++) {
					logger.debug("tab[" + i + "] = " + tab[i]);

					if (tab[i].contains(";W") && !tab[i].contains("PW")) {
						logger.debug("Entering W verification");
						String[] tabCoordinates = tab[i].split("\\[");
						String coordinates = tabCoordinates[1];
						logger.debug("value = " + coordinates);
						String[] letterCoordinates = coordinates.split("");
						String almostCoordinates = letterCoordinates[1] + ","
								+ letterCoordinates[2];
						logger.debug("value = " + almostCoordinates);
						try {
							int xValue = DecypherInputUtility.decypher(
									almostCoordinates, "x");
							int yValue = DecypherInputUtility.decypher(
									almostCoordinates, "y");
							Intersection intersection = new WhiteIntersection(
									xValue, yValue);
							game.addMoveFromHistory(game.getSecondPlayer(),
									intersection);
						} catch (DecypheringFailureException e) {
							System.err.println(e.getMessage());
						} catch (IntersectionAlreadyOccupiedException e) {
							System.err.println(e.getMessage());
						} catch (SuicideException e) {
							System.err.println(e.getMessage());
						} catch (OutOfGobanException e) {
							System.err.println(e.getMessage());
						}

					}

					if (tab[i].contains(";B") && !tab[i].contains("PB")) {
						logger.debug("Entering B verification");
						String[] tabCoordinates = tab[i].split("\\[");
						String coordinates = tabCoordinates[1];
						logger.debug("value = " + coordinates);
						String[] letterCoordinates = coordinates.split("");
						String almostCoordinates = letterCoordinates[1] + ","
								+ letterCoordinates[2];
						logger.debug("value = " + almostCoordinates);
						try {
							int xValue = DecypherInputUtility.decypher(
									almostCoordinates, "x");
							int yValue = DecypherInputUtility.decypher(
									almostCoordinates, "y");
							Intersection intersection = new BlackIntersection(
									xValue, yValue);
							game.addMoveFromHistory(game.getFirstPlayer(),
									intersection);
						} catch (DecypheringFailureException e) {
							System.err.println(e.getMessage());
						} catch (IntersectionAlreadyOccupiedException e) {
							System.err.println(e.getMessage());
						} catch (SuicideException e) {
							System.err.println(e.getMessage());
						} catch (OutOfGobanException e) {
							System.err.println(e.getMessage());
						}
					}
				}
				input = reader.readLine();
			}
			reader.close();
			logger.debug("BufferedReader closed");

		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return game;
	}

	/**
	 * Convert integer value into the character equivalent (example : 'a' for 1,
	 * 'b' for 2, etc.). Used exclusively in {@link Game#exportToSGF(String)}.
	 * 
	 * @param coordinate
	 * @return char equivalent for the int coordinate
	 * @since 1.0
	 */

	private char convertCoordinate(int coordinate) {
		char[] letters = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
				'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's' };
		return letters[coordinate - 1];
	}

	private void toggleCurrentPlayer() {
		if (firstPlayerTurn == true) {
			firstPlayerTurn = false;
		} else {
			firstPlayerTurn = true;
		}
		previousMoveWasAPass = false;
	}
}
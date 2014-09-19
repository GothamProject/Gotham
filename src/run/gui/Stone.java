package run.gui;

import engine.IsBlackPlayerVisitor;
import engine.IsComputerUserVisitor;
import engine.IsWhitePlayerVisitor;
import engine.PlayerVisitor;
import game.Game;
import game.IntersectionAlreadyOccupiedException;
import game.Move;
import game.OutOfGobanException;
import game.SuicideException;
import ia.MoveStrategy;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import logger.LoggerUtility;
import model.BlackIntersection;
import model.Intersection;
import model.ModelParameters;
import model.Player;
import model.Territories;
import model.Territory;
import model.User;
import model.WhiteIntersection;

import org.apache.log4j.Logger;

/**
 * 
 * Represents the clickable stones on the graphical goban.
 * 
 * @author Team AFK
 * @version 1.0
 * 
 */

public class Stone extends JButton implements ActionListener {

	private static final long serialVersionUID = 3591284658131038321L;
	private Game game;
	private int xCoordinate;
	private int yCoordinate;
	private String color;
	private Boolean stonePlayed;
	private static Logger logger = LoggerUtility.getLogger(Stone.class);

	public Stone(Game game, int xCoordinate, int yCoordinate) {
		super();
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.game = game;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		stonePlayed = false;

		setFocusPainted(false);
		setBorderPainted(false);
		setContentAreaFilled(false);

	}

	public int getXCoordinate() {
		return xCoordinate;
	}

	public int getYCoordinate() {
		return yCoordinate;
	}

	public String getColor() {
		return color;
	}

	public Boolean isStonePlayed() {
		return stonePlayed;
	}

	public void setStonePlayed(Boolean bool) {
		stonePlayed = bool;
	}

	/**
	 * 
	 * Essential mechanics of the graphical goban. Handles the display of played
	 * stones and triggers the computer moves.
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (!stonePlayed) {

			/*
			 * The stone was never played before, so we fetch color and set icon
			 */
			Player currentPlayer = game.getCurrentPlayer();
			// User currentUser = game.getCurrentUser();
			Territories capturedTerritories = new Territories();

			try {

				if (currentPlayer.accept(new IsWhitePlayerVisitor())) {

					capturedTerritories = game.addMove(currentPlayer,
							new WhiteIntersection(xCoordinate, yCoordinate));

				} else {

					capturedTerritories = game.addMove(currentPlayer,
							new BlackIntersection(xCoordinate, yCoordinate));

				}

				triggerPlay(currentPlayer, capturedTerritories);

			} catch (IntersectionAlreadyOccupiedException e1) {
				System.err.println(e1.getMessage());
			} catch (SuicideException e1) {
				System.err.println(e1.getMessage());
				/*
				 * If it's a computer playing
				 */
				if (game.getCurrentUser().accept(new IsComputerUserVisitor())) {
					logger.debug("Computer tried to suicide on stone ("
							+ xCoordinate + ", " + yCoordinate + ")");
					triggerComputerMove();
				} else {
					logger.debug("User tried to suicide on stone ("
							+ xCoordinate + ", " + yCoordinate + ")");
					triggerSuicide();
					GamePanel
							.setInfoGameLabel("Suicide is forbidden !!");
				}
				stonePlayed = false;
			} catch (OutOfGobanException e1) {
				System.err.println(e1.getMessage());
			}
			
//			/*
//			 * if player is computer, reset move count
//			 */
//			if (currentUser.accept(new IsComputerUserVisitor())) {
//				game.setComputerMoveCount(0);
//			}

			/*
			 * if opponent is computer, trigger computer move
			 */
			if (stonePlayed
					&& game.getCurrentUser()
							.accept(new IsComputerUserVisitor())) {
				triggerComputerMove();
			}

		} else {

			/*
			 * The stone was already played before, so we transfer the game on a
			 * new one. If the new one has been played before, it will transfer
			 * to a new one, and so on...
			 */
			if (game.getComputerMoveCount() < 10) {
				if (game.getCurrentUser().accept(new IsComputerUserVisitor())) {
					triggerComputerMove();
				}
			} else {
				game.setComputerMoveCount(0);
				game.passMove();
				PlayerVisitor<Boolean> blackVisitor = new IsBlackPlayerVisitor();
				Player currentPlayer = game.getCurrentPlayer();
				if (currentPlayer.accept(blackVisitor)) {
					GamePanel.setBlackScore(currentPlayer.getCaptureCount());
					GamePanel.setBlackNamePanelVisible();
				} else {
					GamePanel.setWhiteScore(currentPlayer.getCaptureCount());
					GamePanel.setWhiteNamePanelVisible();
				}
//				triggerComputerMove();

			}

		}

		/*
		 * Debug
		 */
		// System.out.println(game.getGoban().toString());

	}

	/**
	 * Generates a move by the computer and selects the right stone to fire it
	 * on.
	 */
	public void triggerComputerMove() {

		logger.debug("Computer move triggered on stone (" + xCoordinate + ", "
				+ yCoordinate + ")");

		game.setComputerMoveCount(game.getComputerMoveCount() + 1);

		User currentUser = game.getCurrentUser();

		/*
		 * we test if the current player is a computer. If yes, it means that we
		 * can generate a move.
		 */
		if (currentUser.accept(new IsComputerUserVisitor())) {

			/*
			 * Get moveStrategy
			 */
			MoveStrategy moveStrategy = game.getCurrentMoveStrategy();
			Move computerMove = moveStrategy.getMove(game);
			Intersection intersection = computerMove.getIntersection();

			/*
			 * we select the right stone, and fire click on it
			 */
			for (Stone stone : GobanInnerPanel.stones) {
				if (stone.getXCoordinate() == intersection.getXCoordinate()
						&& stone.getYCoordinate() == intersection
								.getYCoordinate()) {
					stone.actionPerformed(new ActionEvent(this,
							ActionEvent.ACTION_PERFORMED, null));
				}
			}
		}
	}

	/**
	 * Triggers the capture of a stone, by reseting it to normal.
	 */
	public void triggerCapture() {
		logger.debug("Capture triggered on stone (" + xCoordinate + ", "
				+ yCoordinate + ")");
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setIcon(null);
		stonePlayed = false;
	}

	public void triggerPlay(Player currentPlayer,
			Territories capturedTerritories) {

		logger.info("Playing move triggered on stone (" + xCoordinate + ", "
				+ yCoordinate + ")");
		game.setComputerMoveCount(0);

		/*
		 * Hide previous suggestion if applicable
		 */
		if (GobanInnerPanel.suggestionStone != null) {
			GobanInnerPanel.suggestionStone.hideSuggestion();
			GobanInnerPanel.suggestionStone = null;
		}
		
		/*
		 * Hide previous suicide if applicable
		 */
		if (GobanInnerPanel.suicideStone != null) {
			GobanInnerPanel.suicideStone.hideSuicide();
			GobanInnerPanel.suicideStone = null;
		}

		/*
		 * we update the icon and change panel visibility
		 */
		int size = game.getGoban().getSize();
		if (currentPlayer.accept(new IsWhitePlayerVisitor())) {

			if (size == ModelParameters.GOBAN_SIZE_SMALL) {
				setIcon(new ImageIcon(
						GraphicalParameters.LARGE_WHITE_STONE_PATH_NAME));
			} else if (size == ModelParameters.GOBAN_SIZE_MEDIUM) {
				setIcon(new ImageIcon(
						GraphicalParameters.MEDIUM_WHITE_STONE_PATH_NAME));
			} else {
				setIcon(new ImageIcon(
						GraphicalParameters.SMALL_WHITE_STONE_PATH_NAME));
			}
			color = "white";
			GamePanel.setBlackNamePanelVisible();

		} else {

			if (size == ModelParameters.GOBAN_SIZE_SMALL) {
				setIcon(new ImageIcon(
						GraphicalParameters.LARGE_BLACK_STONE_PATH_NAME));
			} else if (size == ModelParameters.GOBAN_SIZE_MEDIUM) {
				setIcon(new ImageIcon(
						GraphicalParameters.MEDIUM_BLACK_STONE_PATH_NAME));
			} else {
				setIcon(new ImageIcon(
						GraphicalParameters.SMALL_BLACK_STONE_PATH_NAME));
			}
			color = "black";
			GamePanel.setWhiteNamePanelVisible();

		}

		/*
		 * we update stone status
		 */
		stonePlayed = true;

		/*
		 * change button design
		 */
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

		/*
		 * Now we check for captures. If there was one, we update the board
		 */
		if (capturedTerritories.getIntersectionCount() > 0) {
			for (int i = 0; i < capturedTerritories.getTerritoriesCount(); i++) {
				Territory territory = capturedTerritories
						.getTerritoryByIndex(i);
				for (int j = 0; j < territory.getIntersectionsCount(); j++) {
					Intersection intersection = territory
							.getIntersectionByIndex(j);
					for (Stone stone : GobanInnerPanel.stones) {
						if (stone.getXCoordinate() == intersection
								.getXCoordinate()
								&& stone.getYCoordinate() == intersection
										.getYCoordinate()) {
							/*
							 * we trigger capture
							 */
							stone.triggerCapture();
						}
					}
				}
			}

			/*
			 * We update the player's score on the screen
			 */
			if (currentPlayer.accept(new IsWhitePlayerVisitor())) {
				GamePanel.setWhiteScore(currentPlayer.getCaptureCount());
			} else {
				GamePanel.setBlackScore(currentPlayer.getCaptureCount());
			}
		}
	}

	/**
	 * Triggers the suggestion state of a stone.
	 */
	public void triggerSuggestion() {

		logger.debug("Suggestion triggered on stone (" + xCoordinate + ", "
				+ yCoordinate + ")");
		if (!stonePlayed) {

			/*
			 * Hide previous suggestion if applicable
			 */
			if (GobanInnerPanel.suggestionStone != null) {
				GobanInnerPanel.suggestionStone.hideSuggestion();
				GobanInnerPanel.suggestionStone = null;
			}

			/*
			 * Set stone as suggestion
			 */
			GobanInnerPanel.suggestionStone = this;

			/*
			 * display hint stone
			 */
			Player currentPlayer = game.getCurrentPlayer();
			int size = game.getGoban().getSize();
			if (currentPlayer.accept(new IsWhitePlayerVisitor())) {

				if (size == ModelParameters.GOBAN_SIZE_LARGE) {
					setIcon(new ImageIcon(
							GraphicalParameters.LARGE_WHITE_STONE_PATH_NAME));
				} else {
					setIcon(new ImageIcon(
							GraphicalParameters.SMALL_WHITE_STONE_PATH_NAME));
				}

			} else {

				if (size == ModelParameters.GOBAN_SIZE_LARGE) {
					setIcon(new ImageIcon(
							GraphicalParameters.LARGE_BLACK_STONE_PATH_NAME));
				} else {
					setIcon(new ImageIcon(
							GraphicalParameters.SMALL_BLACK_STONE_PATH_NAME));
				}

			}

		} else {

			/*
			 * If this stone can't be suggested, we suggest a second one.
			 */
			logger.debug("Suggestion impossible, we transfer");
			System.out.println("transfert");

			MoveStrategy moveStrategy = game.getCurrentMoveStrategy();
			Move move = moveStrategy.getMove(game);
			Intersection intersection = move.getIntersection();

			for (Stone stone : GobanInnerPanel.stones) {
				if (stone.getXCoordinate() == intersection.getXCoordinate()
						&& stone.getYCoordinate() == intersection
								.getYCoordinate()) {
					/*
					 * we trigger suggestion
					 */
					stone.triggerSuggestion();
				}
			}
		}
	}

	/**
	 * Hides a stone when it's suggested
	 */
	public void hideSuggestion() {
		logger.debug("Suggestion disabled on stone (" + xCoordinate + ", "
				+ yCoordinate + ")");
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setIcon(null);
		stonePlayed = false;
	}
	
	/**
	 * Shows a stone in "suicide" state when it's clicked
	 */
	public void triggerSuicide() {
		logger.debug("Suicide triggered on stone (" + xCoordinate + ", "
				+ yCoordinate + ")");
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
		/*
		 * Hide previous suicide if applicable
		 */
		if (GobanInnerPanel.suicideStone != null) {
			GobanInnerPanel.suicideStone.hideSuicide();
			GobanInnerPanel.suicideStone = null;
		}
		
		GobanInnerPanel.suicideStone = this;
		
		int size = game.getGoban().getSize();
		if (size == ModelParameters.GOBAN_SIZE_LARGE) {
			setIcon(new ImageIcon(
					GraphicalParameters.SMALL_CROSS_STONE_PATH_NAME));
		} else if (size == ModelParameters.GOBAN_SIZE_MEDIUM) {
			setIcon(new ImageIcon(
					GraphicalParameters.MEDIUM_CROSS_STONE_PATH_NAME));
		} else {
			setIcon(new ImageIcon(
					GraphicalParameters.LARGE_CROSS_STONE_PATH_NAME));
		}
		stonePlayed = false;
	}
	
	/**
	 * Hides a stone when it's suggested
	 */
	public void hideSuicide() {
		logger.debug("Suicide disabled on stone (" + xCoordinate + ", "
				+ yCoordinate + ")");
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setIcon(null);
		stonePlayed = false;
	}
}

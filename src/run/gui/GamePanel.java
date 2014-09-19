package run.gui;

import engine.IsBlackPlayerVisitor;
import engine.IsComputerUserVisitor;
import engine.IsWhitePlayerVisitor;
import engine.PlayerVisitor;
import game.Game;
import game.Move;
import ia.MoveStrategy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Intersection;
import model.ModelParameters;
import model.Player;
import model.UnregisteredUserException;
import model.User;
import model.UsersRegister;
import time.CountDownParameters;

public class GamePanel extends JPanel implements Runnable {

	/**
     * 
     */
	private static final long serialVersionUID = 412385282617237362L;

	protected GamePanel thisGamePanel = this;

	private JPanel playingPanel;
	private JPanel infoPanel;
	private JPanel gobanInnerPanel;
	private JPanel gobanOuterPanel;

	private JPanel blackInfoPanel;
	private JPanel whiteInfoPanel;
	private JPanel infoTimePanel;
	private JPanel buttonpPanel;
	private JPanel infoGamePanel;

	private JLabel blackTimeInfoLabel;
	private JLabel whiteTimeInfoLabel;

	private static JPanel blackNamePanel;
	private static JPanel whiteNamePanel;

	private JLabel blackName;
	private JLabel whiteName;

	private JLabel blackTimeLabel;
	private JLabel whiteTimeLabel;

	private static JLabel blackScoreLabel;
	private JLabel blackScoreInfoLabel;

	private static JLabel whiteScoreLabel;
	private JLabel whiteScoreInfoLabel;

	private JLabel timeLabel;

	private static JLabel infoGameLabel;

	private JButton suggestionButton;
	private JButton passButton;
	private JButton abandonButton;
	private JButton saveGameButton;
	private JButton quitButton;

	private static Game game;

	public GamePanel(Game game) {
		super();
		GamePanel.game = game;

		init();

		/*
		 * Thread initialization
		 */

		Thread thread = new Thread(this);
		thread.start();

		initComponentPosition();

		suggestionButton.addActionListener(new SuggestionAction());
		passButton.addActionListener(new PassAction());
		abandonButton.addActionListener(new AbandonAction());
		saveGameButton.addActionListener(new SaveAction());
		quitButton.addActionListener(new QuitAction());

		blackNamePanel.setBackground(new Color(235, 209, 183));
		whiteNamePanel.setBackground(new Color(235, 209, 183));

	}

	private void init() {
		playingPanel = new JPanel();
		infoPanel = new JPanel();

		switch (game.getGoban().getSize()) {
		case ModelParameters.GOBAN_SIZE_SMALL:
			gobanInnerPanel = new SmallGobanInnerPanel(game, this);
			break;
		case ModelParameters.GOBAN_SIZE_MEDIUM:
			gobanInnerPanel = new MediumGobanInnerPanel(game, this);
			break;
		default:
			gobanInnerPanel = new LargeGobanInnerPanel(game, this);
			break;
		}

		initGameMoveTime();

		gobanOuterPanel = new JPanel();

		blackInfoPanel = new JPanel();
		whiteInfoPanel = new JPanel();
		infoTimePanel = new JPanel();
		buttonpPanel = new JPanel();
		infoGamePanel = new JPanel();

		blackNamePanel = new BackgroundImagePanel(
				GraphicalParameters.LARGE_BLACK_STONE_PATH_NAME);
		whiteNamePanel = new BackgroundImagePanel(
				GraphicalParameters.LARGE_WHITE_STONE_PATH_NAME);

		blackNamePanel.setPreferredSize(new Dimension(
				GraphicalParameters.SMALL_STONE_SIZE,
				GraphicalParameters.SMALL_STONE_SIZE));
		whiteNamePanel.setPreferredSize(new Dimension(
				GraphicalParameters.SMALL_STONE_SIZE,
				GraphicalParameters.SMALL_STONE_SIZE));

		initNames();

		timeLabel = new JLabel("Game time left : ");

		blackTimeLabel = new JLabel("Time left : ");
		whiteTimeLabel = new JLabel("Time left : ");

		blackScoreInfoLabel = new JLabel("Score : ");
		whiteScoreInfoLabel = new JLabel("Score : ");

		blackScoreLabel = new JLabel("0");
		whiteScoreLabel = new JLabel("0");

		suggestionButton = new JButton("Suggestion");
		passButton = new JButton("Pass");
		abandonButton = new JButton("Abandon");
		saveGameButton = new JButton("Save Game");
		quitButton = new JButton("Quit Game");

		//infoGameLabel = new JLabel("Game informations will be diplayed here !");
		infoGameLabel = new JLabel("Game informations");

	}

	private void initNames() {

		Player tmpFirstPlayer = game.getFirstPlayer();

		if (tmpFirstPlayer.accept(new IsWhitePlayerVisitor())) {
			blackName = new JLabel(game.getSecondUser().getName());
			whiteName = new JLabel(game.getFirstUser().getName());
		} else {
			blackName = new JLabel(game.getFirstUser().getName());
			whiteName = new JLabel(game.getSecondUser().getName());
		}

	}

	private void initGameMoveTime() {
		String moveCountdown = game.getFirstPlayer().getGameClock()
				.getGameCountdown().toString();

		blackTimeInfoLabel = new JLabel(moveCountdown);

		moveCountdown = game.getSecondPlayer().getGameClock()
				.getGameCountdown().toString();
		whiteTimeInfoLabel = new JLabel(moveCountdown);
	}

	private void initComponentPosition() {

		/*
		 * Initialize gamePanel
		 */

		BorderLayout borderLayout = new BorderLayout();
		setLayout(borderLayout);

		add(playingPanel/* , BorderLayout.CENTER */);
		playingPanel.setBackground(new Color(255, 239, 213));

		/*
		 * Initialize infoPanel
		 */

		add(infoPanel, BorderLayout.WEST);
		infoPanel.setBackground(new Color(235, 209, 183));
		infoPanel.setPreferredSize(new Dimension(
				GraphicalParameters.INFO_PANEL_WIDTH,
				GraphicalParameters.INFO_PANEL_HEIGHT));

		/*
		 * Positioning 5 panels in infoPanel
		 */

		GridLayout infoPanelGridLayout = new GridLayout(4, 1);

		infoPanelGridLayout.setVgap(15);//

		infoPanel.setLayout(infoPanelGridLayout);

		infoPanel.add(blackInfoPanel);
		infoPanel.add(whiteInfoPanel);
		infoPanel.add(buttonpPanel);
		infoPanel.add(infoGamePanel);

		GridBagLayout gbl = new GridBagLayout();

		blackInfoPanel.setLayout(gbl);
		blackInfoPanel.setBackground(new Color(235, 209, 183));

		whiteInfoPanel.setLayout(gbl);
		whiteInfoPanel.setBackground(new Color(235, 209, 183));

		infoTimePanel.setLayout(gbl);
		infoTimePanel.setBackground(new Color(235, 209, 183));

		buttonpPanel.setLayout(gbl);
		buttonpPanel.setBackground(new Color(235, 209, 183));

		infoGamePanel.setBackground(new Color(235, 209, 183));

		GridBagConstraints gbc = new GridBagConstraints();

		/*
		 * initialize blackInfoPanel
		 */

		gbc.gridx = gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbl.setConstraints(blackNamePanel, gbc);

		blackInfoPanel.add(blackNamePanel);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbl.setConstraints(blackName, gbc);

		blackInfoPanel.add(blackName);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(10, 0, 0, 0);
		gbc.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(blackTimeLabel, gbc);

		blackInfoPanel.add(blackTimeLabel);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(10, 0, 0, 0);
		gbl.setConstraints(blackTimeInfoLabel, gbc);
		gbc.anchor = GridBagConstraints.CENTER;
		blackInfoPanel.add(blackTimeInfoLabel);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(4, 0, 0, 0);
		gbc.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(blackScoreInfoLabel, gbc);

		blackInfoPanel.add(blackScoreInfoLabel);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(4, 6, 0, 0);
		gbl.setConstraints(blackScoreLabel, gbc);

		blackInfoPanel.add(blackScoreLabel);

		/*
		 * Initialize whiteInfoPanel
		 */

		gbc.gridx = gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbl.setConstraints(whiteNamePanel, gbc);

		whiteInfoPanel.add(whiteNamePanel);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbl.setConstraints(whiteName, gbc);

		whiteInfoPanel.add(whiteName);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(10, 0, 0, 0);
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbl.setConstraints(whiteTimeLabel, gbc);

		whiteInfoPanel.add(whiteTimeLabel);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(10, 0, 0, 0);
		gbc.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(whiteTimeInfoLabel, gbc);

		whiteInfoPanel.add(whiteTimeInfoLabel);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(4, 6, 0, 0);
		gbl.setConstraints(whiteScoreInfoLabel, gbc);

		whiteInfoPanel.add(whiteScoreInfoLabel);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(4, 6, 0, 0);
		gbl.setConstraints(whiteScoreLabel, gbc);

		whiteInfoPanel.add(whiteScoreLabel);

		/*
		 * Initialize time
		 */
		gbc.gridx = gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbl.setConstraints(timeLabel, gbc);

		infoTimePanel.add(timeLabel);

		/*
		 * Initialize buttons
		 */

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 0, 0, 0);
		gbl.setConstraints(suggestionButton, gbc);
		suggestionButton.setPreferredSize(new Dimension(130, 25));

		buttonpPanel.add(suggestionButton);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 0, 0, 0);
		gbl.setConstraints(passButton, gbc);
		passButton.setPreferredSize(new Dimension(130, 25));

		buttonpPanel.add(passButton);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 0, 0, 0);
		gbl.setConstraints(abandonButton, gbc);
		abandonButton.setPreferredSize(new Dimension(130, 25));

		buttonpPanel.add(abandonButton);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(10, 0, 0, 0);
		gbl.setConstraints(saveGameButton, gbc);
		saveGameButton.setPreferredSize(new Dimension(130, 25));

		buttonpPanel.add(saveGameButton);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 0, 0, 0);
		quitButton.setPreferredSize(new Dimension(130, 25));
		gbl.setConstraints(quitButton, gbc);

		buttonpPanel.add(quitButton);

		/*
		 * Initialize Game Informations
		 */

		infoGamePanel.add(infoGameLabel);

		playingPanel.setPreferredSize(new Dimension(
				GraphicalParameters.GAME_PANEL_WIDTH,
				GraphicalParameters.GAME_PANEL_HEIGHT));

		/*
		 * Positioning Goban
		 */

		playingPanel.setLayout(gbl);

		gbc.insets = new Insets(0, 0, 60, 0);
		gbl.setConstraints(gobanOuterPanel, gbc);

		playingPanel.add(gobanOuterPanel);
		gobanOuterPanel.setBackground(new Color(222, 184, 135));
		gobanOuterPanel.add(gobanInnerPanel);
	}

	public static void setBlackNamePanelVisible() {
		blackNamePanel.setVisible(true);
		whiteNamePanel.setVisible(false);

	}

	public static void setWhiteNamePanelVisible() {
		whiteNamePanel.setVisible(true);
		blackNamePanel.setVisible(false);
	}

	public static void setWhiteScore(double score) {
		whiteScoreLabel.setText("" + score);
	}

	public static void setBlackScore(double score) {
		blackScoreLabel.setText("" + score);
	}

	public static void setInfoGameLabel(String text) {
		infoGameLabel.setText(text);
	}

	public static void endGame() {
		game.countPoints();
		double firstPlayerScore = game.getFirstPlayer().getCaptureCount();
		double secondPlayerScore = game.getSecondPlayer().getCaptureCount();
		System.out.println("first " + firstPlayerScore);
		System.out.println("second " + secondPlayerScore);

		User winner = game.getFirstUser();
		User looser = game.getSecondUser();
		double winnerScore = firstPlayerScore;
		double looserScore = secondPlayerScore;
		if (secondPlayerScore > firstPlayerScore) {
			winner = game.getSecondUser();
			looser = game.getFirstUser();
			winnerScore = secondPlayerScore;
			looserScore = firstPlayerScore;
		}

		/*
		 * Store highscores
		 */
		winner.setHighScore(winnerScore);
		looser.setHighScore(looserScore);

		UsersRegister usersRegister = UsersRegister.getInstance();
		try {
			User user1 = usersRegister.getUser(winner.getName());
			User user2 = usersRegister.getUser(looser.getName());
			user1.setHighScore(winnerScore);
			user2.setHighScore(looserScore);
			usersRegister.saveFile();
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (UnregisteredUserException e) {
			System.err.println(e.getMessage());
		}

		int result = JOptionPane.showOptionDialog(null,
				winner.getName() + " is the winner with " + winnerScore
						+ " points ! " + looser.getName()
						+ " looses with only " + looserScore + " points.",
				"Quit Go", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, null, null);
		if (result == JOptionPane.OK_CANCEL_OPTION) {
			// game.exportToSGF();
			System.exit(0);
		} else {
			System.exit(0);
		}
	}

	private class SuggestionAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

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

			infoGameLabel.setText("(" + intersection.getXCoordinate() + ", "
					+ intersection.getYCoordinate() + ")");
			infoGameLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
		}

	}

	private class PassAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			game.passMove();
			PlayerVisitor<Boolean> blackVisitor = new IsBlackPlayerVisitor();
			Player currentPlayer = game.getCurrentPlayer();
			if (currentPlayer.accept(blackVisitor)) {
				setBlackScore(currentPlayer.getCaptureCount());
				setBlackNamePanelVisible();
			} else {
				setWhiteScore(currentPlayer.getCaptureCount());
				setWhiteNamePanelVisible();
			}

			infoGameLabel.setText("You passed your turn.");
			infoGameLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
			
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
	}

	private class AbandonAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			game.countPoints();

			User winner = game.getNotCurrentUser();
			User looser = game.getCurrentUser();
			double winnerScore = game.getNotCurrentPlayer().getCaptureCount();
			double looserScore = game.getCurrentPlayer().getCaptureCount();

			/*
			 * Store highscores
			 */
			winner.setHighScore(winnerScore);
			looser.setHighScore(looserScore);

			UsersRegister usersRegister = UsersRegister.getInstance();
			try {
				User user1 = usersRegister.getUser(winner.getName());
				User user2 = usersRegister.getUser(looser.getName());
				user1.setHighScore(winnerScore);
				user2.setHighScore(looserScore);
				usersRegister.saveFile();
			} catch (FileNotFoundException e1) {
				System.err.println(e1.getMessage());
			} catch (IOException e1) {
				System.err.println(e1.getMessage());
			} catch (UnregisteredUserException e1) {
				System.err.println(e1.getMessage());
			}

			int result = JOptionPane.showOptionDialog(null, winner.getName()
					+ " is the winner with " + winnerScore + " points ! "
					+ looser.getName() + " looses with only " + looserScore
					+ " points.", "Quit Go", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, null, null);
			if (result == JOptionPane.OK_CANCEL_OPTION) {
				// game.exportToSGF();
				System.exit(0);
			} else {
				System.exit(0);
			}

		}
	}

	private class SaveAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			game.exportToSGF();

			infoGameLabel.setText("The game was succesfully saved.");
			infoGameLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
		}

	}

	private class QuitAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			int resultat = JOptionPane.showConfirmDialog(null,
					"Do you want to save ?", "Quit Go",
					JOptionPane.YES_NO_OPTION);
			if (resultat == JOptionPane.YES_OPTION) {
				game.exportToSGF();
				System.exit(0);
			} else {
				System.exit(0);
			}

		}

	}

	@Override
	public void run() {

		Player currentPlayer = game.getCurrentPlayer();

		while (!whiteTimeInfoLabel.getText().equals("00:00")
				&& !blackTimeInfoLabel.getText().equals("00:00")) {
			try {
				Thread.sleep(CountDownParameters.SPEED);
				currentPlayer = game.getCurrentPlayer();

				currentPlayer.getGameClock().decrement();

				if (currentPlayer.accept(new IsWhitePlayerVisitor())) {
					whiteTimeInfoLabel.setText(game.getCurrentPlayer()
							.getGameClock().getGameCountdown().toString());

				} else {
					blackTimeInfoLabel.setText(game.getCurrentPlayer()
							.getGameClock().getGameCountdown().toString());
				}

			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}

		}

		/*
		 * Boyoyomi
		 */
		whiteTimeInfoLabel.setText(game.getCurrentPlayer().getGameClock()
				.getByoyomiCountdown().toString());
		blackTimeInfoLabel.setText(game.getCurrentPlayer().getGameClock()
				.getByoyomiCountdown().toString());

		while (!whiteTimeInfoLabel.getText().equals("00:00")
				&& !blackTimeInfoLabel.getText().equals("00:00")) {
			try {
				Thread.sleep(CountDownParameters.SPEED);
				currentPlayer = game.getCurrentPlayer();

				currentPlayer.getGameClock().decrement();

				if (currentPlayer.accept(new IsWhitePlayerVisitor())) {
					whiteTimeInfoLabel.setText(game.getCurrentPlayer()
							.getGameClock().getByoyomiCountdown().toString());
				} else {
					blackTimeInfoLabel.setText(game.getCurrentPlayer()
							.getGameClock().getByoyomiCountdown().toString());
				}

			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
		}
		
		/*
		 * test if timer is empty
		 */
		if (currentPlayer.getGameClock().isEmpty() || game.getNotCurrentPlayer().getGameClock().isEmpty()) {
			endGame();
		}
		
	}
}

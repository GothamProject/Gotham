package run.gui;

import engine.IsBlackPlayerVisitor;
import engine.IsFreeIntersectionVisitor;
import engine.IsWhitePlayerVisitor;
import game.Game;
import game.Move;
import game.OutOfGobanException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Intersection;
import model.Player;
import model.Territories;
import model.UsersRegister;

public class MainMenu extends JPanel {

	/**
     * 
     */
	private static final long serialVersionUID = -6905330761419798160L;

	private JPanel headPanel;
	private JPanel bodyPanel;

	private NewGame newGame;

	private GamePanel gamePanel;

	private JLabel titleLabel;

	private JButton newGameButton;
	private JButton loadGameButton;
	private JButton scoresButton;
	private JButton helpButton;
	private JButton exitButton;

	private JFileChooser chooser = new JFileChooser();

	private Dimension buttonDimension;

	private Insets insets;

	private Game game;

	public MainMenu() {
		super();
		init();
		initComponentPosition();

		newGameButton.addActionListener(new NewGameAction());
		loadGameButton.addActionListener(new LoadGameAction());
		scoresButton.addActionListener(new ScoresAction());
		helpButton.addActionListener(new HelpAction());
		exitButton.addActionListener(new QuitAction());
	}

	private void init() {

		headPanel = new JPanel();
		bodyPanel = new JPanel();

		headPanel.setBackground(new Color(0, 0, 0, 0));
		bodyPanel.setBackground(new Color(0, 0, 0, 0));

		titleLabel = new JLabel("Go");

		newGameButton = new JButton("New Game");
		loadGameButton = new JButton("Load Game");
		scoresButton = new JButton("Scores");
		helpButton = new JButton("Help");
		exitButton = new JButton("Exit");

		buttonDimension = new Dimension(150, 50);
		insets = new Insets(20, 0, 0, 0);

	}

	private void initComponentPosition() {

		/*
		 * Initialize Componenent
		 */

		BorderLayout borderLayout = new BorderLayout();

		setLayout(borderLayout);

		add(headPanel, BorderLayout.NORTH);
		add(bodyPanel, BorderLayout.CENTER);

		/*
		 * Initialize headPanel
		 */

		titleLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 60));
		headPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		headPanel.add(titleLabel);

		/*
		 * Initialize button size
		 */

		initButtonSize();

		/*
		 * Initialize bodyPanel
		 */

		GridBagLayout gbl = new GridBagLayout();

		bodyPanel.setLayout(gbl);

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbl.setConstraints(newGameButton, gbc);

		bodyPanel.add(newGameButton);

		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = insets;
		gbl.setConstraints(loadGameButton, gbc);

		bodyPanel.add(loadGameButton);

		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = insets;
		gbl.setConstraints(scoresButton, gbc);

		bodyPanel.add(scoresButton);

		gbc.gridy = 3;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = insets;
		gbl.setConstraints(helpButton, gbc);

		bodyPanel.add(helpButton);

		gbc.gridy = 4;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = insets;
		gbl.setConstraints(exitButton, gbc);

		bodyPanel.add(exitButton);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			BufferedImage image = ImageIO.read(new File(
					GraphicalParameters.BACKGROUND_IMAGE_PATH_NAME));
			g.drawImage(image, 0, 0, null);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initButtonSize() {
		newGameButton.setPreferredSize(buttonDimension);
		loadGameButton.setPreferredSize(buttonDimension);
		scoresButton.setPreferredSize(buttonDimension);
		helpButton.setPreferredSize(buttonDimension);
		exitButton.setPreferredSize(buttonDimension);
	}

	private class NewGameAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			Random random = new Random(System.currentTimeMillis());
			int min = GraphicalParameters.MIN_RAND;
			int max = GraphicalParameters.MAX_RAND;

			int randNumber;

			randNumber = getRandomInt(random, min, max);

			headPanel.setVisible(false);
			bodyPanel.setVisible(false);

			newGame = new NewGame(randNumber);

			add(newGame);

			System.out.println("Random number = " + randNumber);
		}

		private int getRandomInt(Random random, int min, int max) {
			return (Math.abs(random.nextInt()) % (max - min + 1)) + min;
		}

	}

	private class LoadGameAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			File defaultLocation = new File("./");
			chooser.setCurrentDirectory(defaultLocation);
			int returnVal = chooser.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				String fileName = file.getName();
				// if loadSGF is working with absolute path
				// String fileName = file.getAbsolutePath();
				if (fileName.matches("^(.*).sgf$")) {
					/*
					 * Launch game
					 */
					// System.out.println("x "+game.getFirstPlayer().getCaptureCount());
					game = Game.loadFile(fileName);

					gamePanel = new GamePanel(game);

					headPanel.setVisible(false);
					bodyPanel.setVisible(false);

					add(gamePanel);

					/*
					 * Display stones from the history
					 */
					// TODO improve
					List<Move> history = game.getHistory();
					Player currentPlayer = game.getFirstPlayer();
					for (Move move : history) {
						Intersection intersection = move.getIntersection();
						currentPlayer = move.getPlayer();
						Territories capturedTerritories = new Territories();
						for (Stone stone : GobanInnerPanel.stones) {
							if (stone.getXCoordinate() == intersection
									.getXCoordinate()
									&& stone.getYCoordinate() == intersection
											.getYCoordinate()) {
								/*
								 * we trigger display
								 */
								stone.triggerPlay(currentPlayer,
										capturedTerritories);
							}
						}
					}
					game.setCurrentPlayerTurn(currentPlayer);

					/*
					 * Hide captured stones
					 */
					for (Stone stone : GobanInnerPanel.stones) {
						if (stone.isStonePlayed()) {
							/*
							 * we check if it's a FreeIntersection
							 */
							try {
								if (game.getGoban()
										.getIntersectionByCoordinates(
												stone.getXCoordinate(),
												stone.getYCoordinate())
										.accept(new IsFreeIntersectionVisitor())) {
									/*
									 * we check color
									 */
									System.out.println(stone.getColor());
									if (stone.getColor().equals("black")) {
										Player player = game.getFirstPlayer();
										if (player
												.accept(new IsBlackPlayerVisitor())) {
											player.addToCaptureCount(1);
											GamePanel.setBlackScore(player
													.getCaptureCount());
										} else {
											Player player2 = game
													.getSecondPlayer();
											player2.addToCaptureCount(1);
											GamePanel.setBlackScore(player2
													.getCaptureCount());
										}
									} else {
										Player player = game.getFirstPlayer();
										if (player
												.accept(new IsWhitePlayerVisitor())) {
											player.addToCaptureCount(1);
											GamePanel.setWhiteScore(player
													.getCaptureCount());
										} else {
											Player player2 = game
													.getSecondPlayer();
											player2.addToCaptureCount(1);
											GamePanel.setWhiteScore(player2
													.getCaptureCount());
										}
									}

									/*
									 * we trigger capture
									 */
									stone.triggerCapture();
								}
							} catch (OutOfGobanException e1) {
							}
						}
					}

				} else {
					/*
					 * TODO inform of error
					 */
					int result = JOptionPane.showConfirmDialog(null,
							"This is not a compatible file !",
							"Load Impossible", JOptionPane.WARNING_MESSAGE);
					System.out.println(file.getName());

					if (result == JOptionPane.OK_OPTION) {
						chooser.showOpenDialog(null);
					}

				}
			} else {
				/*
				 * TODO inform of error
				 */
			}

		}

	}

	private class ScoresAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			UsersRegister usersRegister = UsersRegister.getInstance();
			String top = usersRegister.getTopFive();

			String[] tab = top.split(":");

			JPanel globalPanel = new JPanel();
			JPanel northPanel = new JPanel();
			JPanel southPanel = new JPanel();

			globalPanel.setLayout(new BorderLayout());
			northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			southPanel.setLayout(new GridLayout(5, 1));

			JLabel messageLabel = new JLabel(
					"Scores are supposed to be here : ");

			for (int i = 0; i < tab.length; i++) {
				JLabel label = new JLabel(tab[i]);
				JPanel panel = new JPanel();
				panel.add(label);
				southPanel.add(panel);
			}

			globalPanel.add(northPanel, BorderLayout.NORTH);
			globalPanel.add(southPanel, BorderLayout.SOUTH);

			northPanel.add(messageLabel);

			JOptionPane.showMessageDialog(null, globalPanel);

		}

	}

	private class HelpAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
//			new HelpFrame();
		}

	}

	private class QuitAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			System.exit(0);
		}

	}

}

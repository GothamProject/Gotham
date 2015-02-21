package run.gui;

import engine.IsComputerUserVisitor;
import game.Game;
import game.Move;
import ia.MinMaxMoveStrategy;
import ia.MoveStrategy;
import ia.RandomMoveStrategy;
import ia.RandomPlusMoveStrategy;
import ia.RandomPlusPlusMoveStrategy;

import java.awt.BorderLayout;
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.BlackPlayer;
import model.CannotCreateDirectoryException;
import model.ComputerUser;
import model.Goban;
import model.HumanUser;
import model.IncorrectGobanSizeException;
import model.Intersection;
import model.ModelParameters;
import model.Player;
import model.User;
import model.UserAlreadyRegisteredException;
import model.UsersRegister;
import model.WhitePlayer;
import time.Countdown;

public class NewGame extends JPanel {

	/**
     * 
     */
	private static final long serialVersionUID = 4653583704732299108L;

	private JPanel headPanel;
	private JPanel infoPlayersPanel;
	private JPanel firstPlayerPanel;
	private JPanel secondPlayerPanel;
	private JPanel colorPanel;
	private JPanel gameParametersPanel;

	private JPanel optionsPanel;
	private JPanel validationPanel;

	private JPanel gameParmetersLeftPanel;
	private JPanel gameParametersRightPanel;

	private JPanel gameParametersGobanTitlePanel;
	private JPanel gameParametersGobanPanel;

	private JPanel gameParametersOptionsTitlePanel;
	private JPanel gameParametersOptionsPanel;

	private JPanel smallGobanPanel;
	private JPanel mediumGobanPanel;
	private JPanel largeGobanPanel;

	private JPanel gamePanel;

	private JLabel titleLabel;
	private JLabel firstPlayerLabel;
	private JLabel seconPlayerLabel;
	private JLabel firstPlayerNameLabel;
	private JLabel secondPlayerNameLabel;

	private JLabel colorLabel;

	private JLabel gameParametersGoabanTitleLabel;

	private JLabel gameParametersOptionsTitleLabel;

	private JLabel firstPlayerDifficultyLabel;
	private JLabel secondPlayerDifficultyLabel;
	private JLabel komiLabel;
	private JLabel timeLabel;
	private JLabel byoyomiLabel;

	private JLabel firstPlayerColorChoiceLabel;

	private JRadioButton smallKomiButton;
	private JRadioButton mediumKomiButton;
	private JRadioButton largeKomiButton;

	private ButtonGroup komiButtonGroup;

	private JRadioButton smallGobanButton;
	private JRadioButton mediumGobanButton;
	private JRadioButton largeGobanButton;

	private ButtonGroup sizeButtonGroup;

	private JRadioButton blackButton;
	private JRadioButton whiteButton;
	private JRadioButton randombButton;

	private ButtonGroup colorButtonGroup;

	private JTextField firstPlayerNameField;
	private JTextField secondPlayerNameField;

	private JComboBox firstPlayerDifficultyBox;
	private JComboBox secondPlayerDifficultyBox;
	private JComboBox timeBox;
	private JComboBox byoyomiBox;

	private String[] difficultyLevel = { "Crazy", "Easy", "Medium", "Hard" };

	private JList firstPlayerList;
	private JList secondPlayerList;

	private UsersRegister usersRegister = UsersRegister.getInstance();

	private JButton startButton;

	private User firstUser;
	private User secondUser;

	private Player firstPlayer;
	private Player secondPlayer;

	private GameClock whiteGameClock;
	private GameClock blackGameClock;

	private Goban goban;

	private Game game;

	private int randNumber;

	public NewGame(int randNumber) {
		super();

		this.randNumber = randNumber;

		init();
		colorPanel();
		initStyle();
		initComponent();

		startButton.addActionListener(new StartGame());

		firstPlayerList
				.addListSelectionListener(new firstListSelectionDisplay());
		secondPlayerList
				.addListSelectionListener(new secondListSelectionDisplay());

		firstPlayerNameField
				.addKeyListener(new firstPlayerNameFieldCompletion());
		secondPlayerNameField
				.addKeyListener(new secondPlayerNameFieldCompletion());
	}

	private void init() {

		headPanel = new JPanel();
		infoPlayersPanel = new JPanel();
		colorPanel = new JPanel();
		firstPlayerPanel = new JPanel();
		secondPlayerPanel = new JPanel();
		gameParametersPanel = new JPanel();
		optionsPanel = new JPanel();
		validationPanel = new JPanel();

		gameParmetersLeftPanel = new JPanel();
		gameParametersRightPanel = new JPanel();

		gameParametersGobanTitlePanel = new JPanel();
		gameParametersGobanPanel = new JPanel();

		gameParametersOptionsTitlePanel = new JPanel();
		gameParametersOptionsPanel = new JPanel();

		smallGobanPanel = new BackgroundImagePanel(
				GraphicalParameters.SMALL_GOBAN);
		mediumGobanPanel = new BackgroundImagePanel(
				GraphicalParameters.MEDIUM_GOBAN);
		largeGobanPanel = new BackgroundImagePanel(
				GraphicalParameters.LARGE_GOBAN);

		titleLabel = new JLabel("New Game");
		firstPlayerLabel = new JLabel("First Player : ");
		seconPlayerLabel = new JLabel("Second Player : ");
		firstPlayerNameLabel = new JLabel("Name : ");
		secondPlayerNameLabel = new JLabel("Name : ");
		firstPlayerDifficultyLabel = new JLabel("Difficulty : ");
		secondPlayerDifficultyLabel = new JLabel("Difficulty : ");
		colorLabel = new JLabel("First player color : ");

		gameParametersGoabanTitleLabel = new JLabel("Goban : ");
		gameParametersOptionsTitleLabel = new JLabel("Parameters : ");

		komiLabel = new JLabel("Komi : ");
		timeLabel = new JLabel("Time : ");
		byoyomiLabel = new JLabel("Byoyomi : ");

		firstPlayerColorChoiceLabel = new JLabel(
				"Would you like to choose your color ?");

		firstPlayerNameField = new JTextField(15);
		secondPlayerNameField = new JTextField(15);
		;

		timeBox = new JComboBox();
		byoyomiBox = new JComboBox();
		firstPlayerDifficultyBox = new JComboBox(difficultyLevel);
		secondPlayerDifficultyBox = new JComboBox(difficultyLevel);

		initComboBox();

		initRadioButtons();

		firstPlayerList = new JList(usersRegister.getUsersList(""));
		secondPlayerList = new JList(usersRegister.getUsersList(""));

		startButton = new JButton("Start Game");

		startButton.setPreferredSize(new Dimension(150, 50));

	}

	private void colorPanel() {

		headPanel.setBackground(GraphicalParameters.TRANSPARENT_COLOR);
		infoPlayersPanel.setBackground(GraphicalParameters.TRANSPARENT_COLOR);
		colorPanel.setBackground(GraphicalParameters.TRANSPARENT_COLOR);
		firstPlayerPanel.setBackground(GraphicalParameters.TRANSPARENT_COLOR);
		secondPlayerPanel.setBackground(GraphicalParameters.TRANSPARENT_COLOR);
		gameParametersPanel.setBackground(GraphicalParameters.TRANSPARENT_COLOR);
		optionsPanel.setBackground(GraphicalParameters.TRANSPARENT_COLOR);
		validationPanel.setBackground(GraphicalParameters.TRANSPARENT_COLOR);
		gameParmetersLeftPanel.setBackground(GraphicalParameters.TRANSPARENT_COLOR);
		gameParametersRightPanel.setBackground(GraphicalParameters.TRANSPARENT_COLOR);
		gameParametersGobanTitlePanel.setBackground(GraphicalParameters.TRANSPARENT_COLOR);
		gameParametersGobanPanel.setBackground(GraphicalParameters.TRANSPARENT_COLOR);

		gameParametersOptionsTitlePanel.setBackground(GraphicalParameters.TRANSPARENT_COLOR);
		gameParametersOptionsPanel.setBackground(GraphicalParameters.TRANSPARENT_COLOR);
	}

	/*
	 * Draw the background
	 */
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

	private void initComboBox() {

		for (int i = 10; i <= 60; i += 10) {

			timeBox.addItem(i + " minutes");
		}

		for (int i = 1; i <= 5; i++) {
			if (i == 1) {
				byoyomiBox.addItem(i + " minute");
			} else {

				byoyomiBox.addItem(i + " minutes");
			}
		}

	}

	private void initRadioButtons() {

		/*
		 * Initialize komi Buttons
		 */
		komiButtonGroup = new ButtonGroup();

		smallKomiButton = new JRadioButton("5.5");
		mediumKomiButton = new JRadioButton("6.5");
		largeKomiButton = new JRadioButton("7.5");

//		smallKomiButton.setOpaque(false);
//		mediumKomiButton.setOpaque(false);
//		largeKomiButton.setOpaque(false);

		komiButtonGroup.add(smallKomiButton);
		komiButtonGroup.add(mediumKomiButton);
		komiButtonGroup.add(largeKomiButton);

		smallKomiButton.setSelected(true);

		/*
		 * Initialize goban' size Buttons
		 */
		sizeButtonGroup = new ButtonGroup();

		smallGobanButton = new JRadioButton();
		mediumGobanButton = new JRadioButton();
		largeGobanButton = new JRadioButton();

//		smallGobanButton.setOpaque(false);
//		mediumGobanButton.setOpaque(false);
//		largeGobanButton.setOpaque(false);

		sizeButtonGroup.add(smallGobanButton);
		sizeButtonGroup.add(mediumGobanButton);
		sizeButtonGroup.add(largeGobanButton);

		smallGobanButton.setSelected(true);

		/*
		 * Initialize colors button
		 */
		colorButtonGroup = new ButtonGroup();

		blackButton = new JRadioButton("Black");
		whiteButton = new JRadioButton("White");
		randombButton = new JRadioButton("Nigiri  ");

//		blackButton.setOpaque(false);
//		whiteButton.setOpaque(false);
//		randombButton.setOpaque(false);

		colorButtonGroup.add(blackButton);
		colorButtonGroup.add(whiteButton);
		colorButtonGroup.add(randombButton);

		blackButton.setSelected(true);

	}

	private void initStyle() {

		titleLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 45));
		firstPlayerLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		firstPlayerNameLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 17));
		seconPlayerLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		secondPlayerNameLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 17));
		firstPlayerColorChoiceLabel.setFont(new Font(Font.MONOSPACED,
				Font.BOLD, 15));
		gameParametersGoabanTitleLabel.setFont(new Font(Font.MONOSPACED,
				Font.BOLD, 20));
		gameParametersOptionsTitleLabel.setFont(new Font(Font.MONOSPACED,
				Font.BOLD, 20));
	}

	private void initComponent() {

		/*
		 * Initialize Panels
		 */

		BorderLayout borderLayout = new BorderLayout();
		setLayout(borderLayout);

		add(headPanel, BorderLayout.NORTH);
		add(infoPlayersPanel, BorderLayout.CENTER);
		add(gameParametersPanel, BorderLayout.SOUTH);

		/*
		 * Initialize headPanel
		 */

		headPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		headPanel.add(titleLabel);

		/*
		 * Initialize infoPlayerPanel
		 */

		infoPlayersPanel.setLayout(new GridLayout(1, 3));

		infoPlayersPanel.add(firstPlayerPanel);
		infoPlayersPanel.add(colorPanel);
		infoPlayersPanel.add(secondPlayerPanel);

		/*
		 * Initialize firstPlayerPanel
		 */

		GridBagLayout gbl = new GridBagLayout();

		Insets insets = new Insets(10, 0, 0, 0);

		firstPlayerPanel.setLayout(gbl);

		GridBagConstraints firstBagConstraints = new GridBagConstraints();

		firstBagConstraints.gridx = 0;
		firstBagConstraints.gridy = 0;
		firstBagConstraints.gridwidth = 1;
		firstBagConstraints.gridheight = 1;
		gbl.setConstraints(firstPlayerLabel, firstBagConstraints);

		firstPlayerPanel.add(firstPlayerLabel);

		firstBagConstraints.gridx = 0;
		firstBagConstraints.gridy = 1;
		firstBagConstraints.gridwidth = 1;
		firstBagConstraints.gridheight = 1;
		firstBagConstraints.insets = insets;
		firstBagConstraints.anchor = GridBagConstraints.CENTER;

		gbl.setConstraints(firstPlayerNameField, firstBagConstraints);

		firstPlayerPanel.add(firstPlayerNameField);

		firstBagConstraints.gridx = 0;
		firstBagConstraints.gridy = 2;
		firstBagConstraints.gridwidth = 1;
		firstBagConstraints.gridheight = 3;
		firstBagConstraints.insets = insets;
		gbl.setConstraints(firstPlayerList, firstBagConstraints);

		firstPlayerPanel.add(firstPlayerList);

		firstPlayerList.setPreferredSize(new Dimension(170, 170));

		firstBagConstraints.gridx = 0;
		firstBagConstraints.gridy = 5;
		firstBagConstraints.gridwidth = 1;
		firstBagConstraints.gridheight = 1;
		firstBagConstraints.insets = insets;
		firstBagConstraints.anchor = GridBagConstraints.LINE_START;
		gbl.setConstraints(firstPlayerDifficultyLabel, firstBagConstraints);

		firstPlayerPanel.add(firstPlayerDifficultyLabel);

		firstBagConstraints.gridx = 0;
		firstBagConstraints.gridy = 5;
		firstBagConstraints.gridwidth = 1;
		firstBagConstraints.gridheight = 1;
		firstBagConstraints.insets = insets;
		firstBagConstraints.anchor = GridBagConstraints.LINE_END;
		gbl.setConstraints(firstPlayerDifficultyBox, firstBagConstraints);

		firstPlayerPanel.add(firstPlayerDifficultyBox);

		firstPlayerDifficultyBox.setPreferredSize(new Dimension(100, 20));

		/*
		 * Initialize colorPanel
		 */
		colorPanel.setLayout(gbl);

		GridBagConstraints colorBagConstraints = new GridBagConstraints();

		colorBagConstraints.gridx = 0;
		colorBagConstraints.gridy = 0;
		colorBagConstraints.gridwidth = 1;
		colorBagConstraints.gridheight = 1;
		colorBagConstraints.insets = insets;
		colorBagConstraints.anchor = GridBagConstraints.LINE_START;
		gbl.setConstraints(colorLabel, colorBagConstraints);

		colorPanel.add(colorLabel);

		colorBagConstraints.gridx = 1;
		colorBagConstraints.gridy = 0;
		colorBagConstraints.gridwidth = 1;
		colorBagConstraints.gridheight = 1;
		colorBagConstraints.insets = insets;
		colorBagConstraints.anchor = GridBagConstraints.LINE_START;
		gbl.setConstraints(blackButton, colorBagConstraints);

		colorPanel.add(blackButton);

		colorBagConstraints.gridx = 1;
		colorBagConstraints.gridy = 1;
		colorBagConstraints.gridwidth = 1;
		colorBagConstraints.gridheight = 1;
		colorBagConstraints.insets = insets;
		colorBagConstraints.anchor = GridBagConstraints.LINE_START;
		gbl.setConstraints(whiteButton, colorBagConstraints);

		colorPanel.add(whiteButton);

		colorBagConstraints.gridx = 1;
		colorBagConstraints.gridy = 2;
		colorBagConstraints.gridwidth = 1;
		colorBagConstraints.gridheight = 1;
		colorBagConstraints.insets = insets;
		colorBagConstraints.anchor = GridBagConstraints.LINE_END;
		gbl.setConstraints(randombButton, colorBagConstraints);

		colorPanel.add(randombButton);

		/*
		 * Initialize secondPlayerPanel
		 */

		secondPlayerPanel.setLayout(gbl);

		GridBagConstraints secondBagConstraints = new GridBagConstraints();

		secondBagConstraints.gridx = 0;
		secondBagConstraints.gridy = 0;
		secondBagConstraints.gridwidth = 1;
		secondBagConstraints.gridheight = 1;
		gbl.setConstraints(seconPlayerLabel, secondBagConstraints);

		secondPlayerPanel.add(seconPlayerLabel);

		secondBagConstraints.gridx = 0;
		secondBagConstraints.gridy = 1;
		secondBagConstraints.gridwidth = 1;
		secondBagConstraints.gridheight = 1;
		secondBagConstraints.insets = insets;
		secondBagConstraints.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(secondPlayerNameField, secondBagConstraints);

		secondPlayerPanel.add(secondPlayerNameField);

		secondBagConstraints.gridx = 0;
		secondBagConstraints.gridy = 2;
		secondBagConstraints.gridwidth = 1;
		secondBagConstraints.gridheight = 3;
		secondBagConstraints.insets = insets;
		gbl.setConstraints(secondPlayerList, secondBagConstraints);

		secondPlayerPanel.add(secondPlayerList);

		secondPlayerList.setPreferredSize(new Dimension(170, 170));

		secondBagConstraints.gridx = 0;
		secondBagConstraints.gridy = 5;
		secondBagConstraints.gridwidth = 1;
		secondBagConstraints.gridheight = 1;
		secondBagConstraints.insets = insets;
		secondBagConstraints.anchor = GridBagConstraints.LINE_START;
		gbl.setConstraints(secondPlayerDifficultyLabel, secondBagConstraints);

		secondPlayerPanel.add(secondPlayerDifficultyLabel);

		secondBagConstraints.gridx = 0;
		secondBagConstraints.gridy = 5;
		secondBagConstraints.gridwidth = 1;
		secondBagConstraints.gridheight = 1;
		secondBagConstraints.insets = insets;
		secondBagConstraints.anchor = GridBagConstraints.LINE_END;
		gbl.setConstraints(secondPlayerDifficultyBox, secondBagConstraints);

		secondPlayerPanel.add(secondPlayerDifficultyBox);

		secondPlayerDifficultyBox.setPreferredSize(new Dimension(100, 20));

		/*
		 * Initialize gameParametersPanel
		 */

		gameParametersPanel.setLayout(new BorderLayout());

		gameParametersPanel.add(optionsPanel, BorderLayout.NORTH);
		gameParametersPanel.add(validationPanel, BorderLayout.SOUTH);

		/*
		 * Initiamize optionPanel
		 */
		optionsPanel.setLayout(new GridLayout(1, 2));
		optionsPanel.add(gameParmetersLeftPanel);
		optionsPanel.add(gameParametersRightPanel);

		/*
		 * Initialize gameParametersLeftPanel
		 */

		gameParmetersLeftPanel.setLayout(new BorderLayout());

		gameParmetersLeftPanel.add(gameParametersGobanTitlePanel,
				BorderLayout.NORTH);
		gameParmetersLeftPanel
				.add(gameParametersGobanPanel, BorderLayout.SOUTH);

		/*
		 * Initialize gameParametersGobanTitlePanel
		 */
		gameParametersGobanTitlePanel.setLayout(new FlowLayout(
				FlowLayout.CENTER));

		gameParametersGobanTitlePanel.add(gameParametersGoabanTitleLabel);

		/*
		 * Initialize gameParmetersLeftPanel
		 */
		gameParametersGobanPanel.setLayout(gbl);

		GridBagConstraints gameParametersLeftConstraints = new GridBagConstraints();

		Insets insetsGoban = new Insets(5, 0, 0, 0);

		gameParametersLeftConstraints.gridx = 0;
		gameParametersLeftConstraints.gridy = 0;
		gameParametersLeftConstraints.gridwidth = 1;
		gameParametersLeftConstraints.gridheight = 1;
		gbl.setConstraints(smallGobanButton, gameParametersLeftConstraints);

		gameParametersGobanPanel.add(smallGobanButton);

		gameParametersLeftConstraints.gridx = 1;
		gameParametersLeftConstraints.gridy = 0;
		gameParametersLeftConstraints.gridwidth = 1;
		gameParametersLeftConstraints.gridheight = 1;
		gbl.setConstraints(smallGobanPanel, gameParametersLeftConstraints);
		smallGobanPanel.setPreferredSize(new Dimension(50, 50));

		gameParametersGobanPanel.add(smallGobanPanel);

		gameParametersLeftConstraints.gridx = 0;
		gameParametersLeftConstraints.gridy = 2;
		gameParametersLeftConstraints.gridwidth = 1;
		gameParametersLeftConstraints.gridheight = 1;
		gameParametersLeftConstraints.insets = insetsGoban;
		gbl.setConstraints(mediumGobanButton, gameParametersLeftConstraints);

		gameParametersGobanPanel.add(mediumGobanButton);

		gameParametersLeftConstraints.gridx = 1;
		gameParametersLeftConstraints.gridy = 2;
		gameParametersLeftConstraints.gridwidth = 1;
		gameParametersLeftConstraints.gridheight = 1;
		gameParametersLeftConstraints.insets = insetsGoban;
		gbl.setConstraints(mediumGobanPanel, gameParametersLeftConstraints);
		mediumGobanPanel.setPreferredSize(new Dimension(75, 75));

		gameParametersGobanPanel.add(mediumGobanPanel);

		gameParametersLeftConstraints.gridx = 0;
		gameParametersLeftConstraints.gridy = 3;
		gameParametersLeftConstraints.gridwidth = 1;
		gameParametersLeftConstraints.gridheight = 1;
		gameParametersLeftConstraints.insets = insetsGoban;
		gbl.setConstraints(largeGobanButton, gameParametersLeftConstraints);

		gameParametersGobanPanel.add(largeGobanButton);

		gameParametersLeftConstraints.gridx = 1;
		gameParametersLeftConstraints.gridy = 3;
		gameParametersLeftConstraints.gridwidth = 1;
		gameParametersLeftConstraints.gridheight = 1;
		gameParametersLeftConstraints.insets = new Insets(5, 0, 10, 0);
		gbl.setConstraints(largeGobanPanel, gameParametersLeftConstraints);
		largeGobanPanel.setPreferredSize(new Dimension(100, 100));

		gameParametersGobanPanel.add(largeGobanPanel);

		/*
		 * Initialize gameParametersRightPanel
		 */
		gameParametersRightPanel.setLayout(new BorderLayout());

		gameParametersRightPanel.add(gameParametersOptionsTitlePanel,
				BorderLayout.NORTH);
		gameParametersRightPanel.add(gameParametersOptionsPanel,
				BorderLayout.SOUTH);

		/*
		 * Initialize gameParametersOptionsTitlePanel
		 */
		gameParametersOptionsTitlePanel.setLayout(new FlowLayout(
				FlowLayout.CENTER));

		gameParametersOptionsTitlePanel.add(gameParametersOptionsTitleLabel);

		/*
		 * Initalize gameParametersOptionsPanel
		 */
		gameParametersOptionsPanel.setLayout(gbl);

		GridBagConstraints gameParametersRightConstraints = new GridBagConstraints();

		Insets rightPaneliInsets = new Insets(0, 0, 20, 0);

		gameParametersRightConstraints.gridx = 0;
		gameParametersRightConstraints.gridy = 0;
		gameParametersRightConstraints.gridwidth = 1;
		gameParametersRightConstraints.gridheight = 1;
		gameParametersRightConstraints.insets = rightPaneliInsets;
		gbl.setConstraints(komiLabel, gameParametersRightConstraints);

		gameParametersOptionsPanel.add(komiLabel);

		gameParametersRightConstraints.gridx = 1;
		gameParametersRightConstraints.gridy = 0;
		gameParametersRightConstraints.gridwidth = 1;
		gameParametersRightConstraints.gridheight = 1;
		gbl.setConstraints(smallKomiButton, gameParametersRightConstraints);

		gameParametersOptionsPanel.add(smallKomiButton);

		gameParametersRightConstraints.gridx = 1;
		gameParametersRightConstraints.gridy = 1;
		gameParametersRightConstraints.gridwidth = 1;
		gameParametersRightConstraints.gridheight = 1;
		gbl.setConstraints(mediumKomiButton, gameParametersRightConstraints);

		gameParametersOptionsPanel.add(mediumKomiButton);

		gameParametersRightConstraints.gridx = 1;
		gameParametersRightConstraints.gridy = 2;
		gameParametersRightConstraints.gridwidth = 1;
		gameParametersRightConstraints.gridheight = 1;
		gameParametersRightConstraints.insets = rightPaneliInsets;
		gbl.setConstraints(largeKomiButton, gameParametersRightConstraints);

		gameParametersOptionsPanel.add(largeKomiButton);

		gameParametersRightConstraints.gridx = 0;
		gameParametersRightConstraints.gridy = 3;
		gameParametersRightConstraints.gridwidth = 1;
		gameParametersRightConstraints.gridheight = 1;
		gameParametersRightConstraints.insets = rightPaneliInsets;
		gbl.setConstraints(timeLabel, gameParametersRightConstraints);

		gameParametersOptionsPanel.add(timeLabel);

		gameParametersRightConstraints.gridx = 1;
		gameParametersRightConstraints.gridy = 3;
		gameParametersRightConstraints.gridwidth = 1;
		gameParametersRightConstraints.gridheight = 1;
		gameParametersRightConstraints.insets = rightPaneliInsets;
		gbl.setConstraints(timeBox, gameParametersRightConstraints);

		gameParametersOptionsPanel.add(timeBox);

		gameParametersRightConstraints.gridx = 0;
		gameParametersRightConstraints.gridy = 4;
		gameParametersRightConstraints.gridwidth = 1;
		gameParametersRightConstraints.gridheight = 1;
		gameParametersRightConstraints.insets = rightPaneliInsets;
		gbl.setConstraints(byoyomiLabel, gameParametersRightConstraints);

		gameParametersOptionsPanel.add(byoyomiLabel);

		gameParametersRightConstraints.gridx = 1;
		gameParametersRightConstraints.gridy = 4;
		gameParametersRightConstraints.gridwidth = 1;
		gameParametersRightConstraints.gridheight = 1;
		gameParametersRightConstraints.insets = rightPaneliInsets;
		gbl.setConstraints(byoyomiBox, gameParametersRightConstraints);

		gameParametersOptionsPanel.add(byoyomiBox);

		/*
		 * Initialize validationPanel
		 */
		validationPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		validationPanel.add(startButton);

	}

	private class StartGame implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String firstPlayerName = firstPlayerNameField.getText();
			String secondPlayerName = secondPlayerNameField.getText();

			Boolean goToGame = false;

			/*
			 * If nothing is selected for first player, it's a computer.
			 * Otherwise it's human.
			 */
			if (firstPlayerName.length() == 0
					|| firstPlayerName
							.equals(GraphicalParameters.COMPUTER_NAME_FIELD)) {
				firstUser = new ComputerUser(secondPlayerName);
			} else {
				if (!usersRegister.contains(firstPlayerName)) {
					try {
						usersRegister.addUser(firstPlayerName);
					} catch (UserAlreadyRegisteredException e1) {
					} catch (CannotCreateDirectoryException e1) {
						System.err.println(e1.getMessage());
					} catch (FileNotFoundException e1) {
						System.err.println(e1.getMessage());
					} catch (IOException e1) {
						System.err.println(e1.getMessage());
					}
				}
				firstUser = new HumanUser(firstPlayerName);
			}

			/*
			 * If nothing is selected for second player, it's a computer.
			 * Otherwise it's human.
			 */
			if (secondPlayerName.length() == 0
					|| secondPlayerName
							.equals(GraphicalParameters.COMPUTER_NAME_FIELD)) {
				secondUser = new ComputerUser(firstUser.getName());
			} else {
				if (!usersRegister.contains(secondPlayerName)) {
					try {
						usersRegister.addUser(secondPlayerName);
					} catch (UserAlreadyRegisteredException e1) {
					} catch (CannotCreateDirectoryException e1) {
						System.err.println(e1.getMessage());
					} catch (FileNotFoundException e1) {
						System.err.println(e1.getMessage());
					} catch (IOException e1) {
						System.err.println(e1.getMessage());
					}
				}
				secondUser = new HumanUser(secondPlayerName);
			}

			goToGame = true;

			/*
			 * selecting move duration
			 */
			int moveTime;
			String valueMoveTime = byoyomiBox.getSelectedItem().toString();
			String[] tableValueMoveTime = valueMoveTime.split(" ");
			moveTime = Integer.valueOf(tableValueMoveTime[0]);

			Countdown blackByoyomiCountdown = new Countdown(moveTime, 0);
			Countdown whiteByoyomiCountdown = new Countdown(moveTime, 0);

			/*
			 * selecting game duration
			 */
			int gameTime;
			String valueGameTime = timeBox.getSelectedItem().toString();
			String[] tableValueGameTime = valueGameTime.split(" ");
			gameTime = Integer.valueOf(tableValueGameTime[0]);

			Countdown blackGameCountdown = new Countdown(gameTime, 0);
			Countdown whiteGameCountdown = new Countdown(gameTime, 0);

			/*
			 * selecting difficulty
			 */
			int difficulty = firstPlayerDifficultyBox.getSelectedIndex();
			MoveStrategy firstMoveStrategy = new RandomMoveStrategy();
			if (difficulty == 3) {
				firstMoveStrategy = new MinMaxMoveStrategy();
			} else if (difficulty == 2) {
				firstMoveStrategy = new RandomPlusPlusMoveStrategy();
			} else if (difficulty == 1) {
				firstMoveStrategy = new RandomPlusMoveStrategy();
			}

			difficulty = secondPlayerDifficultyBox.getSelectedIndex();
			MoveStrategy secondMoveStrategy = new RandomMoveStrategy();
			if (difficulty == 3) {
				secondMoveStrategy = new MinMaxMoveStrategy();
				System.out.println("ok");
			} else if (difficulty == 2) {
				secondMoveStrategy = new RandomPlusPlusMoveStrategy();
			} else if (difficulty == 1) {
				secondMoveStrategy = new RandomPlusMoveStrategy();
			}

			/*
			 * Initialize Black or White player choice
			 */

			whiteGameClock = new GameClock(whiteGameCountdown,
					whiteByoyomiCountdown);
			blackGameClock = new GameClock(blackGameCountdown,
					blackByoyomiCountdown);

			if (blackButton.isSelected()) {
				firstPlayer = new BlackPlayer(blackGameClock);
				secondPlayer = new WhitePlayer(whiteGameClock);
			} else if (whiteButton.isSelected()) {
				firstPlayer = new WhitePlayer(whiteGameClock);
				secondPlayer = new BlackPlayer(blackGameClock);
			} else {
				JPanel globalPanel = new JPanel();
				JPanel northPanel = new JPanel();
				JPanel southPanel = new JPanel();

				globalPanel.setLayout(new GridLayout(2, 1));
				northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				southPanel.setLayout(new GridLayout(1, 2));

				JLabel messageLabel = new JLabel(firstUser.getName()
						+ ", please guess if our number is odd or even.");

				globalPanel.add(northPanel);
				globalPanel.add(southPanel);

				northPanel.add(messageLabel);

				JRadioButton evenButton = new JRadioButton("Even");
				JRadioButton oddButton = new JRadioButton("Odd");

				ButtonGroup peerOddButtonGroup = new ButtonGroup();

				evenButton.setSelected(true);

				peerOddButtonGroup.add(evenButton);
				peerOddButtonGroup.add(oddButton);

				southPanel.add(evenButton);
				southPanel.add(oddButton);

				JOptionPane.showMessageDialog(null, globalPanel);

				int result = randNumber % 2;

				System.out.println("result " + result);

				if (evenButton.isSelected()) {
					if (result == 0) {
						firstPlayer = new BlackPlayer(blackGameClock);
						secondPlayer = new WhitePlayer(whiteGameClock);
					} else {
						firstPlayer = new WhitePlayer(whiteGameClock);
						secondPlayer = new BlackPlayer(blackGameClock);
					}
				} else {
					if (result != 0) {
						firstPlayer = new BlackPlayer(blackGameClock);
						secondPlayer = new WhitePlayer(whiteGameClock);
					} else {
						firstPlayer = new WhitePlayer(whiteGameClock);
						secondPlayer = new BlackPlayer(blackGameClock);
					}
				}
			}

			/*
			 * selecting goban size
			 */
			int size;

			if (smallGobanButton.isSelected()) {
				size = ModelParameters.GOBAN_SIZE_SMALL;
			} else if (mediumGobanButton.isSelected()) {
				size = ModelParameters.GOBAN_SIZE_MEDIUM;
			} else {
				size = ModelParameters.GOBAN_SIZE_LARGE;
			}
			/*
			 * selecting komi
			 */
			double komi = 5.5;
			if (mediumKomiButton.isSelected()) {
				komi = 6.5;
			} else if (largeKomiButton.isSelected()) {
				komi = 7.5;
			}

			if (goToGame) {

				try {

					goban = new Goban(size);
					game = new Game(firstUser, secondUser, firstPlayer,
							secondPlayer, goban, komi, firstMoveStrategy,
							secondMoveStrategy);

					/*
					 * hiding setup panel
					 */

					headPanel.setVisible(false);
					infoPlayersPanel.setVisible(false);
					firstPlayerPanel.setVisible(false);
					secondPlayerPanel.setVisible(false);
					gameParametersPanel.setVisible(false);

					/*
					 * creating game panel
					 */

					gamePanel = new GamePanel(game);
					add(gamePanel);

					GamePanel.setBlackNamePanelVisible();

					/*
					 * If both users are computers, we inform the user
					 */
					if (game.getFirstUser().accept(new IsComputerUserVisitor())
							&& game.getSecondUser().accept(
									new IsComputerUserVisitor())) {
						JOptionPane
								.showMessageDialog(null,
										"Computer VS Computer. Click anywhere on the goban to launch the game.");
					}
					
					/*
					 * If current player is computer, we trigger
					 */
					if (game.getCurrentUser().accept(new IsComputerUserVisitor())) {
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

				} catch (IncorrectGobanSizeException ex) {
					System.err.println(ex.getMessage());
				}

			}

		}

	}

	private class firstListSelectionDisplay implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (firstPlayerList.getSelectedIndex() != -1) {
				String name = String
						.valueOf(firstPlayerList.getSelectedValue());
				if (!name.equals("null") && name.length() > 0) {
					firstPlayerNameField.setText(name);
				}
			}
		}
	}

	private class secondListSelectionDisplay implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (secondPlayerList.getSelectedIndex() != -1) {
				String name = String.valueOf(secondPlayerList
						.getSelectedValue());
				if (!name.equals("null") && name.length() > 0) {
					secondPlayerNameField.setText(name);
				}
			}
		}
	}

	private class firstPlayerNameFieldCompletion implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			String text = firstPlayerNameField.getText();
			firstPlayerList.removeAll();
			firstPlayerList.setListData(usersRegister.getUsersList(text));
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}

	}

	private class secondPlayerNameFieldCompletion implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			String text = secondPlayerNameField.getText();
			secondPlayerList.removeAll();
			secondPlayerList.setListData(usersRegister.getUsersList(text));
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}

	}

}
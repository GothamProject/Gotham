package run.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class HelpFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3426815280406677822L;
	private static String firstPath = "img/newgame.png";
	private static String secondPath = "img/capture.png";
	private static String thirdPath = "img/eyes.png";
	private static String fourthPath = "img/eye.png";
	private static String fifthPath = "img/ia.png";
	private static String sixthPath = "img/load.png";

	private static String firstContent = "Two players, Black and White, take turns placing a stone (game piece) of their own color on a vacant point (intersection) of the grid on a Go board. Black moves first.\n\n       If there is a large difference in skill between the players, Black is typically allowed to place two or more stones on the board to compensate for the difference (see Go handicaps).\n\n      The official grid comprises 19×19 lines, though the rules can be applied to any grid size. 13×13 and 9×9 boards are popular choices to teach beginners. Once placed, a stone may not be moved to a different point.";
	private static String secondContent = "There are several tactical constructs aimed at capturing stones.\n\n      These are among the first things a player learns after understanding the rules.\n\n      Recognizing the possibility that stones can be captured using these techniques is an important step forward.\n\n      The most basic technique is the ladder.\n\n      To capture stones in a ladder, a player uses a constant series of capture threats—called atari—to force the opponent into a zigzag pattern as shown in the diagram to the right. Unless the pattern runs into friendly stones along the way, the stones in the ladder cannot avoid capture.\n\n      Experienced players recognize the futility of continuing the pattern and play elsewhere.\n\n      The presence of a ladder on the board does give a player the option to play a stone in the path of the ladder, thereby threatening to rescue their stones, forcing a response.\n\n      Such a move is called a ladder breaker and may be a powerful strategic move. In the diagram, Black has the option of playing a ladder breaker.";
	private static String thirdContent = "-Connection : Keeping one's own stones connected means that fewer groups need to make living shape, and one has fewer groups to defend.\n\n-Cut : Keeping opposing stones disconnected means that the opponent needs to defend and make living shape for more groups\n\n-Stay alive : The simplest way to stay alive is to establish a foothold in the corner or along one of the sides. At a minimum, a group must have two eyes (separate open points) to be alive. An opponent cannot fill in either eye, as any such move is suicidal and prohibited in the rules.";
	private static String fourthContent = "-Mutual life (seki) is better than dying : A situation in which neither player can play on a particular point without then allowing the other player to play at another point to capture.\n\n     The most common example is that of adjacent groups that share their last few liberties—if either player plays in the shared liberties, they can reduce their own group to a single liberty (putting themselves in atari), allowing their opponent to capture it on the next move.\n\n      -Sacrifice: Allowing a group to die in order to carry out a play, or plan, in a more important area.\n\n      -Sente: A play that forces one's opponent to respond (gote). A player who can regularly play sente has the initiative and can control the flow of the game.\n\n       Reduction: Placing a stone far enough into the opponent's area of influence to reduce the amount of territory they eventually get, but not so far in that it can be cut off from friendly stones outside.";
	private static String fifthContent = "We have three level of IA : \n\n      -Crazy(easy),\n\n      -Medium,\n\n      -Hard.";
	private static String sixthContent = "We can save the game. When one plays he can use the save boutton, and he can save when he quits the game.\n\n      We can load a game by selecting a file from the computer.\n\n      The file format is a Strandard Game Format (SGF).";

	private CardLayout card = new CardLayout();
	
	public HelpFrame() {
		init();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void init() {
		JPanel globalPanel = new JPanel();

		final JPanel northPanel = new JPanel();
		JPanel southPanel = new JPanel();

		JPanel firstNorthPanel = new JPanel();
		JPanel secondNorthPanel = new JPanel();
		JPanel thirdNorthPanel = new JPanel();
		JPanel fourthNorthPanel = new JPanel();
		JPanel fifthNorthPanel = new JPanel();
		JPanel sixthNorthPanel = new JPanel();

		JPanel firstNorthInnerPanel = new JPanel();
		JPanel secondNorthInnerPanel = new JPanel();
		JPanel thirdNorthInnerPanel = new JPanel();
		JPanel fourthNorthInnerPanel = new JPanel();
		JPanel fifthNorthInnerPanel = new JPanel();
		JPanel sixthNorthInnerPanel = new JPanel();

		JPanel firstSouthInnerPanel = new JPanel();
		JPanel secondSouthInnerPanel = new JPanel();
		JPanel thirdSouthInnerPanel = new JPanel();
		JPanel fourthSouthInnerPanel = new JPanel();
		JPanel fifthSouthInnerPanel = new JPanel();
		JPanel sixthSouthInnerPanel = new JPanel();

		JLabel firstLabel = new JLabel("Basic rules :");
		JLabel secondLabel = new JLabel("How to Capture :");
		JLabel thirdLabel = new JLabel("Basic Concepts :");
		JLabel fourthLabel = new JLabel("Special Move :");
		JLabel fifthLabel = new JLabel("IA Level :");
		JLabel sixthLabel = new JLabel("Save and Load :");

		JTextArea firstTextArea = new JTextArea(firstContent);
		Border firstBorder = BorderFactory.createLineBorder(Color.BLACK);
		firstTextArea.setBorder(BorderFactory.createCompoundBorder(firstBorder,
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		JTextArea secondTextArea = new JTextArea(secondContent);
		Border secondBorder = BorderFactory.createLineBorder(Color.BLACK);
		secondTextArea.setBorder(BorderFactory.createCompoundBorder(
				secondBorder, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		JTextArea thirdTextArea = new JTextArea(thirdContent);
		Border thirdBorder = BorderFactory.createLineBorder(Color.BLACK);
		thirdTextArea.setBorder(BorderFactory.createCompoundBorder(thirdBorder,
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		JTextArea fourthTextArea = new JTextArea(fourthContent);
		Border fourthBorder = BorderFactory.createLineBorder(Color.BLACK);
		fourthTextArea.setBorder(BorderFactory.createCompoundBorder(
				fourthBorder, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		JTextArea fifthTextArea = new JTextArea(fifthContent);
		Border fifthBorder = BorderFactory.createLineBorder(Color.BLACK);
		fifthTextArea.setBorder(BorderFactory.createCompoundBorder(fifthBorder,
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		JTextArea sixthTextArea = new JTextArea(sixthContent);
		Border sixthBorder = BorderFactory.createLineBorder(Color.BLACK);
		sixthTextArea.setBorder(BorderFactory.createCompoundBorder(sixthBorder,
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		/*
		 * Initialize Text Areas' dimension
		 */

		Dimension dimension = new Dimension(400, 300);

		JScrollPane firstScrollPane = new JScrollPane(firstTextArea);
		JScrollPane secondScrollPane = new JScrollPane(secondTextArea);
		JScrollPane thirdScrollPane = new JScrollPane(thirdTextArea);
		JScrollPane fourthScrollPane = new JScrollPane(fourthTextArea);
		JScrollPane fifthScrollPane = new JScrollPane(fifthTextArea);
		JScrollPane sixthScrollPane = new JScrollPane(sixthTextArea);

		firstTextArea.setLineWrap(true);
		firstTextArea.setEditable(false);

		secondTextArea.setLineWrap(true);
		secondTextArea.setEditable(false);

		thirdTextArea.setLineWrap(true);
		thirdTextArea.setEditable(false);

		fourthTextArea.setLineWrap(true);
		fourthTextArea.setEditable(false);

		fifthTextArea.setLineWrap(true);
		fifthTextArea.setEditable(false);

		sixthTextArea.setLineWrap(true);
		sixthTextArea.setEditable(false);

		firstScrollPane.setPreferredSize(dimension);
		secondScrollPane.setPreferredSize(dimension);
		thirdScrollPane.setPreferredSize(dimension);
		fourthScrollPane.setPreferredSize(dimension);
		fifthScrollPane.setPreferredSize(dimension);
		sixthScrollPane.setPreferredSize(dimension);

		JPanel firstImagePanel = new BackgroundImagePanel(firstPath);
		JPanel secondImagePanel = new BackgroundImagePanel(secondPath);
		JPanel thirdImagePanel = new BackgroundImagePanel(thirdPath);
		JPanel fourthImagePanel = new BackgroundImagePanel(fourthPath);
		JPanel fifthImagePanel = new BackgroundImagePanel(fifthPath);
		JPanel sixthImagePanel = new BackgroundImagePanel(sixthPath);

		JButton nextButton = new JButton("Next");
		JButton previousButton = new JButton("Previous");

		globalPanel.setLayout(new BorderLayout());

		globalPanel.add(northPanel, BorderLayout.NORTH);
		globalPanel.add(southPanel, BorderLayout.SOUTH);

		/*
		 * Initialize firstNorthPanel
		 */
		firstNorthPanel.setLayout(new BorderLayout());

		firstNorthPanel.add(firstNorthInnerPanel, BorderLayout.NORTH);
		firstNorthPanel.add(firstSouthInnerPanel, BorderLayout.SOUTH);

		firstNorthInnerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		firstNorthInnerPanel.add(firstLabel);

		firstSouthInnerPanel.setLayout(new GridLayout(1, 2));

		firstSouthInnerPanel.add(firstScrollPane);
		firstSouthInnerPanel.add(firstImagePanel);

		/*
		 * Initialize secondNorthPanel
		 */
		secondNorthPanel.setLayout(new BorderLayout());

		secondNorthPanel.add(secondNorthInnerPanel, BorderLayout.NORTH);
		secondNorthPanel.add(secondSouthInnerPanel, BorderLayout.SOUTH);

		secondNorthInnerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		secondNorthInnerPanel.add(secondLabel);

		secondSouthInnerPanel.setLayout(new GridLayout(1, 2));

		secondSouthInnerPanel.add(secondScrollPane);
		secondSouthInnerPanel.add(secondImagePanel);

		/*
		 * Initialize thirdNorthPanel
		 */
		thirdNorthPanel.setLayout(new BorderLayout());

		thirdNorthPanel.add(thirdNorthInnerPanel, BorderLayout.NORTH);
		thirdNorthPanel.add(thirdSouthInnerPanel, BorderLayout.SOUTH);

		thirdNorthInnerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		thirdNorthInnerPanel.add(thirdLabel);

		thirdSouthInnerPanel.setLayout(new GridLayout(1, 2));

		thirdSouthInnerPanel.add(thirdScrollPane);
		thirdSouthInnerPanel.add(thirdImagePanel);

		/*
		 * Initialize fourthNorthPanel
		 */
		fourthNorthPanel.setLayout(new BorderLayout());

		fourthNorthPanel.add(fourthNorthInnerPanel, BorderLayout.NORTH);
		fourthNorthPanel.add(fourthSouthInnerPanel, BorderLayout.SOUTH);

		fourthNorthInnerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		fourthNorthInnerPanel.add(fourthLabel);

		fourthSouthInnerPanel.setLayout(new GridLayout(1, 2));

		fourthSouthInnerPanel.add(fourthScrollPane);
		fourthSouthInnerPanel.add(fourthImagePanel);

		/*
		 * Initialize fifthNorthPanel
		 */
		fifthNorthPanel.setLayout(new BorderLayout());

		fifthNorthPanel.add(fifthNorthInnerPanel, BorderLayout.NORTH);
		fifthNorthPanel.add(fifthSouthInnerPanel, BorderLayout.SOUTH);

		fifthNorthInnerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		fifthNorthInnerPanel.add(fifthLabel);

		fifthSouthInnerPanel.setLayout(new GridLayout(1, 2));

		fifthSouthInnerPanel.add(fifthScrollPane);
		fifthSouthInnerPanel.add(fifthImagePanel);
		/*
		 * Initialize sixthNorthPanel
		 */
		sixthNorthPanel.setLayout(new BorderLayout());

		sixthNorthPanel.add(sixthNorthInnerPanel, BorderLayout.NORTH);
		sixthNorthPanel.add(sixthSouthInnerPanel, BorderLayout.SOUTH);

		sixthNorthInnerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		sixthNorthInnerPanel.add(sixthLabel);

		sixthSouthInnerPanel.setLayout(new GridLayout(1, 2));

		sixthSouthInnerPanel.add(sixthScrollPane);
		sixthSouthInnerPanel.add(sixthImagePanel);

		northPanel.setLayout(card);

		northPanel.add(firstNorthPanel, "CARD_1");
		northPanel.add(secondNorthPanel, "CARD_2");
		northPanel.add(thirdNorthPanel, "CARD_3");
		northPanel.add(fourthNorthPanel, "CARD_4");
		northPanel.add(fifthNorthPanel, "CARD_5");
		northPanel.add(sixthNorthPanel, "CARD_6");

		/*
		 * Initialize southPanel
		 */
		southPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		southPanel.add(previousButton);
		southPanel.add(nextButton);

		nextButton.setPreferredSize(new Dimension(100, 25));
		previousButton.setPreferredSize(new Dimension(100, 25));

		nextButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				card.next(northPanel);

			}
		});

		previousButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				card.previous(northPanel);

			}
		});

		JOptionPane.showMessageDialog(null, globalPanel);
	}
}

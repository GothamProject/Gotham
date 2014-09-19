package run.gui;

import game.Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class MediumGobanInnerPanel extends GobanInnerPanel {

	private static final long serialVersionUID = -7411111498957934646L;

	public MediumGobanInnerPanel(Game game, GamePanel gamePanel) {
		super(game);
		setPreferredSize(new Dimension(
				GraphicalParameters.GRAPHICAL_GOBAN_SIZE_MEDIUM - 10,
				GraphicalParameters.GRAPHICAL_GOBAN_SIZE_MEDIUM - 10));
		setBackground(new Color(222, 184, 135));

		setLayout(null);

		int xCoordinate = 0;
		int yCoordinate = 0;

		for (int i = 20; i < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_MEDIUM; i += GraphicalParameters.SCALE_MEDIUM) {
			xCoordinate++;
			yCoordinate = 0;
			for (int j = 20; j < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_MEDIUM; j += GraphicalParameters.SCALE_MEDIUM) {
				yCoordinate++;
				Stone stone = new Stone(game, xCoordinate, yCoordinate);
				stone.setBounds(i - GraphicalParameters.MEDIUM_STONE_SIZE / 2,
						j - GraphicalParameters.MEDIUM_STONE_SIZE / 2,
						GraphicalParameters.MEDIUM_STONE_SIZE,
						GraphicalParameters.MEDIUM_STONE_SIZE);
				stone.addActionListener(stone);
				add(stone);
				stones.add(stone);
			}
		}

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void drawGrid(Graphics g) {

		int width = getWidth() - 15;
		int height = getHeight() - 15;

		int linesStart = 20;

		int ovalStart = 3 * GraphicalParameters.SCALE_MEDIUM + 16;
		int ovalGap = 3 * GraphicalParameters.SCALE_MEDIUM;

		for (int i = linesStart; i < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_MEDIUM; i += GraphicalParameters.SCALE_MEDIUM) {
			g.drawLine(i, linesStart, i, height);
		}

		for (int i = linesStart; i < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_MEDIUM; i += GraphicalParameters.SCALE_MEDIUM) {
			g.drawLine(linesStart, i, width, i);
		}

		for (int i = ovalStart; i < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_MEDIUM - 50; i += ovalGap) {
			for (int j = ovalStart; j < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_MEDIUM - 50; j += ovalGap) {
				g.fillOval(i, j, 7, 7);
			}
		}

		// TODO extract constants

	}

}

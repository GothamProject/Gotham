package run.gui;

import game.Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class LargeGobanInnerPanel extends GobanInnerPanel {

	/**
     * 
     */
	private static final long serialVersionUID = -2474509244446236204L;

	public LargeGobanInnerPanel(Game game, GamePanel gamePanel) {
		super(game);
		setPreferredSize(new Dimension(
				GraphicalParameters.GRAPHICAL_GOBAN_SIZE_LARGE,
				GraphicalParameters.GRAPHICAL_GOBAN_SIZE_LARGE));
		setBackground(new Color(222, 184, 135));

		setLayout(null);

		int xCoordinate = 0;
		int yCoordinate = 0;

		for (int i = 20; i < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_LARGE; i += GraphicalParameters.SCALE_LARGE) {
			xCoordinate++;
			yCoordinate = 0;
			for (int j = 20; j < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_LARGE; j += GraphicalParameters.SCALE_LARGE) {
				yCoordinate++;
				Stone stone = new Stone(game, xCoordinate, yCoordinate);
				stone.setBounds(i - 10, j - 10, 20, 20);
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
		int width = getWidth() - 10;
		int height = getHeight() - 10;

		for (int i = 20; i < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_LARGE; i += GraphicalParameters.SCALE_LARGE) {
			g.drawLine(i, 20, i, height);
		}

		for (int i = 20; i < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_LARGE; i += GraphicalParameters.SCALE_LARGE) {
			g.drawLine(20, i, width, i);
		}

		for (int i = 3 * GraphicalParameters.SCALE_LARGE + 16; i < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_LARGE; i += 6 * GraphicalParameters.SCALE_LARGE) {
			for (int j = 3 * GraphicalParameters.SCALE_LARGE + 16; j < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_LARGE; j += 6 * GraphicalParameters.SCALE_LARGE) {
				g.fillOval(i, j, 7, 7);
			}
		}
		// TODO extract constants

	}

}

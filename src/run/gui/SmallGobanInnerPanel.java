package run.gui;

import game.Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class SmallGobanInnerPanel extends GobanInnerPanel {

	/**
     * 
     */
	private static final long serialVersionUID = -5623963683982559939L;

	public SmallGobanInnerPanel(Game game, GamePanel gamePanel) {
		super(game);
		setPreferredSize(new Dimension(
				GraphicalParameters.GRAPHICAL_GOBAN_SIZE_SMALL - 17,
				GraphicalParameters.GRAPHICAL_GOBAN_SIZE_SMALL - 17));
		setBackground(new Color(222, 184, 135));

		setLayout(null);

		int xCoordinate = 0;
		int yCoordinate = 0;

		for (int i = 20; i < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_SMALL; i += GraphicalParameters.SCALE_SMALL) {
			xCoordinate++;
			yCoordinate = 0;
			for (int j = 20; j < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_SMALL; j += GraphicalParameters.SCALE_SMALL) {
				yCoordinate++;
				Stone stone = new Stone(game, xCoordinate, yCoordinate);
				stone.setBounds(i - GraphicalParameters.SMALL_STONE_SIZE / 2, j
						- GraphicalParameters.SMALL_STONE_SIZE / 2,
						GraphicalParameters.SMALL_STONE_SIZE,
						GraphicalParameters.SMALL_STONE_SIZE);
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

		int width = getWidth() - 20;
		int height = getHeight() - 20;

		for (int i = 23; i < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_SMALL; i += GraphicalParameters.SCALE_SMALL) {
			g.drawLine(i, 23, i, height);
		}

		for (int i = 23; i < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_SMALL; i += GraphicalParameters.SCALE_SMALL) {
			g.drawLine(23, i, width, i);
		}

		for (int i = 2 * GraphicalParameters.SCALE_SMALL + 19; i < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_SMALL; i += 4 * GraphicalParameters.SCALE_SMALL) {
			for (int j = 2 * GraphicalParameters.SCALE_SMALL + 19; j < GraphicalParameters.GRAPHICAL_GOBAN_SIZE_SMALL; j += 4 * GraphicalParameters.SCALE_SMALL) {
				g.fillOval(i, j, 7, 7);
			}
		}
		// TODO extract constants
	}

}

package run.gui;

import game.Game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public abstract class GobanInnerPanel extends JPanel {

	private static final long serialVersionUID = -4955903613950590683L;
	protected static ArrayList<Stone> stones = new ArrayList<Stone>();
	protected static Stone suggestionStone;
	protected static Stone suicideStone;
	private Game game;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);

		drawGrid(g);
	}

	public GobanInnerPanel(Game game) {
		super();
		this.game = game;
	}

	public Game getGame() {
		return game;
	}

	public abstract void drawGrid(Graphics g);

}

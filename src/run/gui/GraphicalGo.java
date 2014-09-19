package run.gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GraphicalGo extends JFrame {

	/**
     * 
     */
	private static final long serialVersionUID = -8276276729177593234L;

	private static MainMenu mainMenu;

	public GraphicalGo(String title) {
		super(title);
		setIconImage(new ImageIcon("./img/logo.png").getImage());

		mainMenu = new MainMenu();

		getContentPane().add(mainMenu);
		setVisible(true);
		setSize(GraphicalParameters.WINDOW_WIDTH,
				GraphicalParameters.WINDOW_HEIGHT);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static MainMenu getMainMenu() {
		return mainMenu;
	}

	public static void main(String[] args) {
		new GraphicalGo("Go");
	}
}

package run.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BackgroundStonePanel extends JPanel {

	private static final long serialVersionUID = 1586631403190531647L;

	private String path;

	public BackgroundStonePanel(String path) {
		this.path = path;

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			BufferedImage image = ImageIO.read(new File(path));
			g.drawImage(image, 0, 0, null);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

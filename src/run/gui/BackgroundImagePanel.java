package run.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BackgroundImagePanel extends JPanel{

  
    private static final long serialVersionUID = -1341334134079292314L;
    
    private String path;

    public BackgroundImagePanel(String path){
        this.path = path;
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        try{
            BufferedImage image = ImageIO.read(new File(path));
            g.drawImage(image, 0, 0, null);
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

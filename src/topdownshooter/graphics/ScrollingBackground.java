
package topdownshooter.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import topdownshooter.GamePanel;

public class ScrollingBackground {
    private double  yOffset, speed;
    private BufferedImage img;
    private int imgHeight = 0;
    
    public ScrollingBackground(String path, double speed){
        this.speed = speed;
        yOffset = 0;
        try{
            img = ImageIO.read(getClass().getResource(path));
            imgHeight = img.getHeight();
        }catch(Exception e){
            System.err.println("Background.java: failed to load img");
        }
    }
    public void update(){
        yOffset+=speed;
        if ((int)Math.round(yOffset) >= GamePanel.HEIGHT )
            yOffset=0;
            
    }
    public void draw(Graphics2D g){
        int roundedOffset = (int)Math.round(yOffset);
        g.drawImage(img, 0, roundedOffset, GamePanel.WIDTH, GamePanel.HEIGHT, null);
        g.drawImage(img, 0,  roundedOffset -GamePanel.HEIGHT, GamePanel.WIDTH, GamePanel.HEIGHT, null);
    }
    

}

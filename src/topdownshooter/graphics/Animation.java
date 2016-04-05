

package topdownshooter.graphics;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class Animation {
    
    
    
    private BufferedImage[] frames;
    public BufferedImage currentImg;
    private int tickNo;
    private int currentFrame = 0;
    private final int updateRate;
    //all spritesheets have to be vertical
    public Animation ( int frameH, String src, int updateRate){
        BufferedImage spriteSheet;
        this.updateRate = updateRate;
        try{
            spriteSheet = ImageIO.read(getClass().getResource(src));
            int sheetH = spriteSheet.getHeight();
            int sheetW = spriteSheet.getWidth();
            frames = new BufferedImage[sheetH/frameH];
            for (int i = 0; i < sheetH/frameH; i++){
                frames[i] = spriteSheet.getSubimage(0, i*frameH, sheetW, frameH);
            }
            currentImg = frames[0];
            System.out.println(currentImg.getHeight());
        }catch (Exception e){
            System.err.println("error loading spritesheet");
        }
                  
    }
    public void update(){
        tickNo++;
        if (currentFrame < 0) currentFrame = 0;
        if (tickNo >= updateRate){
            tickNo = 0;
            currentImg = frames[(++currentFrame)%frames.length];
        }
    }
}

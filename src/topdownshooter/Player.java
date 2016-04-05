

package topdownshooter;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import topdownshooter.graphics.Animation;

public class Player {
    public int x, y, z;
    
    protected int dx, dy, speed, r;
    
    protected int lives, life, maxLife;
    
    protected long lastHit, recoveryTimer = 1000; //1 secs
    protected boolean recovering = false;
    
    public boolean left, right, up, down, firing;
    
    private long firingTimer, firingDelay;
    long timeSincelastHit = 0;
    
    protected Color color1, color2;
    
    protected Animation currentAnim;
    public static Animation shootAnim = new Animation(128, "/shoot.png", 3);
    public static Animation flyAnim = new Animation(128, "/fly.png", 2);
    
    public Player(){
        x = GamePanel.WIDTH/2;
        y = GamePanel.HEIGHT /2;
        r = 64;
        
        dx = 0; dy = 0;
        
        speed = 10; lives = 3; life = 3;
        maxLife = 3;
        
        color1 = Color.WHITE;
        color2 = Color.RED;
        firing= false;
        firingTimer = System.nanoTime();
        firingDelay = 150;
        currentAnim = flyAnim;
        
    }
    
    public void update(){
        timeSincelastHit = (System.nanoTime() - lastHit)/1000000;
        if (recovering && timeSincelastHit > recoveryTimer){
            recovering = false;
        }
        if (left){
            dx = -speed;
        }
        if (right){
            dx = speed;
        }
        if (up){
            dy = -speed;
        }
        if (down){
            dy = speed;
        }
        x += dx;
        y += dy;
//        if (x < r) x = r;
//        if (y < r) y = r;
//        if (x > GamePanel.WIDTH - r) x =GamePanel.WIDTH - r;
//        if (y > GamePanel.HEIGHT - r) y =GamePanel.HEIGHT - r;
        if (x < 0) x = 0;
        if (y < r) y = r;
        if (x > GamePanel.WIDTH ) x = GamePanel.WIDTH ;
        if (y > GamePanel.HEIGHT - r) y = GamePanel.HEIGHT-r;
        dx = 0;
        dy = 0;
        if (firing){
            currentAnim = shootAnim;
            long elapsed = (System.nanoTime()- firingTimer)/1000000;
            if (elapsed > firingDelay){
                GamePanel.bullets.add(new Bullet(270, x, y));
                GamePanel.bullets.add(new Bullet(280, x, y));
                GamePanel.bullets.add(new Bullet(260, x, y));
                firingTimer = System.nanoTime();
            }
        }
        else{
            currentAnim = flyAnim;
        }
        currentAnim.update();
    }
    
    public void draw(Graphics2D g){
        if (currentAnim == null){
            if (!recovering)
                g.setColor(color1);
            else
                g.setColor(color2);
            g.fillOval(x-r, y-r, 2*r, 2*r);
            g.setStroke(new BasicStroke(3));
            if (!recovering)
                g.setColor(color1.darker());
            else
                g.setColor(color2.darker());
        }
        else{
            g.drawImage(currentAnim.currentImg, (int)x-r, (int)y-r, 2*r, 2*r, null);
            if (recovering){
                g.setStroke(new BasicStroke(1));
                g.setColor(Color.RED);
                g.drawOval(x-r, y-r, 2*r, 2*r);
                int alpha = 30+(int)(70*Math.cos(3.14159*timeSincelastHit/recoveryTimer));
                if (alpha >100) alpha = 100;
                if (alpha < 0) alpha = 0;
                g.setColor(new Color(255, 0, 0, alpha));
                g.fillOval(x-r, y-r, 2*r, 2*r);
            }
            else{
                g.setStroke(new BasicStroke(1));
                g.setColor(Color.WHITE);
                g.drawOval(x-r, y-r, 2*r, 2*r);
            }
        }
        //drawing lives
        
        g.drawString("Lives                       HP: "+life, 15, 14);
        for (int i =0; i < lives; i++){
            g.setColor(Color.WHITE);
            g.fillOval(20 + 20*i, 20, 10, 10);
            g.setColor(Color.WHITE.darker());
            g.drawOval(20 + 20*i, 20, 10, 10);
        }
        g.setStroke(new BasicStroke(1));
        
    }
    public void setFiring(boolean b){
        firing = b;
    }
    
    public int getX(){return x;}
    public int getY(){return y;}
    public int getR(){return r;}
    public int getLives(){return lives;}
    public void loseLife(){
        if (!recovering){
            life--;
            recovering = true;
            lastHit = System.nanoTime();
            if (life <= 0){
                lives--;
                life = maxLife;
            }
        }
        
    }
}

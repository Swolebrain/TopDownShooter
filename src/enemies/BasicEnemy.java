

package enemies;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import topdownshooter.GamePanel;


public class BasicEnemy {
    protected double x, y;
    protected int r;
    protected double dx, dy, rad, speed;
    protected int health, type, rank;
    
    protected Color color1;
    
    protected boolean ready, dead;
    
    protected BufferedImage img;
    
    public BasicEnemy(){
        color1 = Color.BLUE;
        speed = 2;
        r = 5;
        health = 1;
        
        x = Math.random()*GamePanel.HEIGHT/2 + GamePanel.HEIGHT/4;
        y = r+1;
        double angle = Math.random()*140+20;
        rad = Math.toRadians(angle);
        dx = Math.cos(rad)*speed;
        dy = Math.sin(rad)*speed;
        ready = false;
        dead = false;
    }
    
    public double getX(){ return x; }
    public double getY(){ return y; }
    public double getR(){ return r; }
    
    public void hit(){
        if (--health <= 0)
            dead = true;
    }
    
    public void update(){
        x += dx;
        y +=dy;
        if (!ready){
            if (x > r && (x < GamePanel.WIDTH - r) &&
                    y > r && (y < GamePanel.HEIGHT - r )){
                ready = true;
            }
        }
        if (x < r && dx < 0) dx = -dx;
        if (y < r && dy < 0) dy = -dy;
        if (x > GamePanel.WIDTH - r && dx > 0) dx = -dx; 
        if (y > GamePanel.HEIGHT - r && dy > 0) dy = -dy; 
    }
    
    public void draw(Graphics2D g){
        if (img == null ){
            g.setColor(color1);
            g.fillOval((int)x-r, (int)y-r, 2*r, 2*r);
            g.setColor(color1.darker());
            g.setStroke(new BasicStroke(3));
            g.drawOval((int)x-r, (int)y-r, 2*r, 2*r);
            g.setStroke(new BasicStroke(1));
        }
        else{
            g.drawImage(img, (int)x-r, (int)y-r, 2*r, 2*r, null);
        }
    }
    
    public boolean isDead(){
        return dead;
    }
    
    
}

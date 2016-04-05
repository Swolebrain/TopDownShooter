

package topdownshooter;

import java.awt.*;

public class Bullet {
    private double x, y;
    int r;
    
    double dx, dy, rad, speed;
    
    private Color color1;
    
    public Bullet(double angle, int x, int y){
        this.x = x;
        this.y = y;
        
        r =16;
        speed = 15;
        rad = Math.toRadians(angle);
        dx = Math.cos(rad)*speed;
        dy = Math.sin(rad)*speed;
        
        color1 = Color.YELLOW;
        
    }
    public boolean update(){
        x+= dx;
        y += dy;
        
        if (x < -r || x > GamePanel.WIDTH ||
                y < -r || y > GamePanel.HEIGHT)
            return true;
        
        return false;
    }
    public void draw(Graphics2D g){
        Color current = color1;
        g.setColor(color1);
        g.fillOval((int)(x-r), (int)(y-r), 2*r, 2*r);
        g.setStroke(new BasicStroke(4));
        for (int i = 4; i >= 0; i--){
            int R = (current.getRed()-25) > 0 ? (current.getRed()-25) : 0;
            int G = (current.getGreen()-25) > 0 ? (current.getGreen()-25) : 0;
            int B = (current.getBlue()-25) > 0 ? (current.getBlue()-25) : 0;
            int A = (current.getAlpha()-60) > 5 ? (current.getAlpha()-60) : 5;
            current = new Color(R, G, B,  A);
            g.setColor(current);
            g.drawOval((int)(x-r+2*i), (int)(y-r+2*i), 2*r-4*i, 2*r-4*i);
        }
        //g.setStroke(new BasicStroke(2));
    }
    
    public double getX(){ return x; }
    public double getY(){ return y; }
    public double getR(){ return r; }
}

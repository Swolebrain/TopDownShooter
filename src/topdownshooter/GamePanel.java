

package topdownshooter;

import enemies.BasicEnemy;
import enemies.Doge1;
import enemies.Doge2;
import enemies.Impossibru;
import enemies.TrollFace;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;
import topdownshooter.graphics.ScrollingBackground;


public class GamePanel extends JPanel implements Runnable, KeyListener{
    
    public static int SCALE = 1;
    public static int WIDTH = 360;
    public static int HEIGHT = 640;
    
    private Thread thread;
    private boolean running;
    
    private BufferedImage image;
    private Graphics2D g;
    
    private final int FPS = 30;
    
    private int fpsCount = 0;
    
    public static Player player;
    
    public static ArrayList<BasicEnemy> enemies;
    public static ArrayList<Bullet> bullets;
    
    private long waveStartTimer, waveStartTimerDiff, waveNumber;
    private final long waveDelay = 2000;
    private boolean waveStart;
    
    private ScrollingBackground bg;
    
    public GamePanel(){
        super();
        setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        setFocusable(true);
        requestFocus();
    }
    public void addNotify(){
        super.addNotify();
        if (thread == null)
            thread = new Thread(this);
        thread.start();
        addKeyListener(this);
    }
    
    public void run(){
        running = true;
        
        player = new Player();
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        bg = new ScrollingBackground("/firebg.jpg", 6);
        
        waveStartTimer = 0;
        waveStartTimerDiff = 0;
        waveStart = true;
        waveNumber = 0;
        
        image = new BufferedImage (WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(
        RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        long start, now, elapsedTime;
        long targetTime = 1000000000/FPS;
        long waitTime = 0;
        long totalTime = 0;
        long sec = 1000000000;
        while (running){
            start = System.nanoTime();
            gameUpdate();
            gameRender();
            gameDraw();
            
            now = System.nanoTime();
            elapsedTime = now - start; //nanoseconds
            waitTime = targetTime - elapsedTime; //nanos - nanos
            try{
                Thread.sleep(waitTime/1000000); 
                //nanos / 10^6 = millis
            }catch (Exception e){}
            fpsCount++;
            totalTime += System.nanoTime() - start; //nanos
            if (totalTime > sec){
                System.out.println("FPS: "+fpsCount);
                fpsCount = 0;
                totalTime = 0;
            }
            
        }
    }
    
    public void gameUpdate(){
        player.update();
        
        if (waveStartTimer ==0 && enemies.size() == 0){
            waveNumber++;
            waveStart = false;
            waveStartTimer = System.nanoTime();
        }
        else{
            waveStartTimerDiff = (System.nanoTime() - waveStartTimer)/1000000;
            if (waveStartTimerDiff > waveDelay){
                waveStart = true;
                waveStartTimer = 0;
                waveStartTimerDiff = 0;
            }
        }
        if (waveStart && enemies.size() == 0){
            createEnemies();
        }
        
        int n = 0;
        while ( n < bullets.size()){
            Bullet x = bullets.get(n);
            boolean remove = x.update();
            if (remove){
                bullets.remove(x);
            }
            else{
                n++;
            }
        }
        
        n = 0;
        while (n < enemies.size()){
            enemies.get(n).update();
            if (enemies.get(n).isDead())
                enemies.remove(n);
            else
                n++;
        }
        checkCollisions();
        bg.update();
    }
    public void gameRender(){
//        g.setColor(Color.BLACK);
//        g.fillRect(0, 0, WIDTH, HEIGHT);
        bg.draw(g);
        g.setColor(Color.WHITE);
        
        player.draw(g);
        for (Bullet x : bullets){
            x.draw(g);
        }
        for (BasicEnemy x: enemies)
            x.draw(g);
        
        if (waveStartTimer != 0 ){
            g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
            String s = " - W A V E  "+waveNumber + " -";
            int length = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
            int alpha = (int)(255*Math.sin(3.14 * waveStartTimerDiff/waveDelay));
            if (alpha > 255) alpha = 255;
            g.setColor(new Color(255, 255, 255, alpha));
            g.drawString(s, WIDTH/2 -length/2, HEIGHT/2);
        }
        
    }
    public void gameDraw(){
        Graphics2D g2 = (Graphics2D)this.getGraphics();
        g2.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
        g2.dispose();
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int key = ke.getKeyCode();
        if (key == KeyEvent.VK_LEFT ||
                key == KeyEvent.VK_A){
            player.left = true;
        }
        if (key == KeyEvent.VK_RIGHT||
                key == KeyEvent.VK_D){
            player.right = true;
        }
        if (key == KeyEvent.VK_UP||
                key == KeyEvent.VK_W){
            player.up = true;
        }
        if (key == KeyEvent.VK_DOWN||
                key == KeyEvent.VK_S){
            player.down = true;
        }
        if (key == KeyEvent.VK_SPACE){
            player.setFiring(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int key = ke.getKeyCode();
        if (key == KeyEvent.VK_LEFT ||
                key == KeyEvent.VK_A){
            player.left = false;
        }
        if (key == KeyEvent.VK_RIGHT||
                key == KeyEvent.VK_D){
            player.right = false;
        }
        if (key == KeyEvent.VK_UP||
                key == KeyEvent.VK_W){
            player.up = false;
        }
        if (key == KeyEvent.VK_DOWN||
                key == KeyEvent.VK_S){
            player.down = false;
        }
        if (key == KeyEvent.VK_SPACE){
            player.setFiring(false);
        }
    }
    
    private void createEnemies(){
        enemies.clear();
        if (waveNumber == 1){
            for (int i = 0; i < 16; i++)
                enemies.add(new Doge2(6));
        }
        if (waveNumber == 2){
            for (int i = 0; i < 16; i++)
                enemies.add(new Doge1(6));
                enemies.add(new TrollFace(6));
        }
        if (waveNumber == 3){
            for (int i = 0; i < 32; i++){
                enemies.add(new TrollFace(6));
                enemies.add(new Impossibru());
            }
        }
        if (waveNumber == 4){
            for (int i = 0; i < 128; i++)
                enemies.add(new BasicEnemy());
        }
    }
    
    public void checkCollisions(){
        for (int i = 0; i < enemies.size(); i++){
            BasicEnemy e = enemies.get(i);
            double xDistP = player.getX() - e.getX();
            double yDistP = player.getY() - e.getY();
            double distP = Math.sqrt(xDistP*xDistP + yDistP*yDistP);
            if (distP < player.getR() + e.getR()){
                player.loseLife();
            }
            int j = 0;
            while (j < bullets.size()){
                Bullet b = bullets.get(j);
                double xDist = b.getX() - e.getX();
                double yDist = b.getY() - e.getY();
                double dist = Math.sqrt(xDist*xDist + yDist*yDist);
                if (dist < b.getR() + e.getR()){
                    bullets.remove(b);
                    e.hit();
                    i--;
                    break;
                }
                else{
                    j++;
                }
            }
            
        }
    }
}

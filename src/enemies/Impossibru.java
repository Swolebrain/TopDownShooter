
package enemies;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Impossibru extends BasicEnemy{

    public Impossibru() {
        super();
        try {
            img = ImageIO.read(getClass().getResource("/impossibru.png"));
        } catch (IOException ex) {
            System.err.println("messed up loading impossibru.png");
        }
        r = 35;
        speed = 6;
        dx = Math.cos(rad)*speed;
        dy = Math.sin(rad)*speed;
    }

}

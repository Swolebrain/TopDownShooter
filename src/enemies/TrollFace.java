

package enemies;

import java.io.IOException;
import javax.imageio.ImageIO;
import topdownshooter.graphics.Animation;


public class TrollFace extends AnimatedEnemy{

    public TrollFace(int s) {
        super(s);
        anim = new Animation(88, "/trollface/anim.png", 4);
        img = anim.currentImg;
        r=38;
    }

}

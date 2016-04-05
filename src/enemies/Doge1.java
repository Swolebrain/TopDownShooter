

package enemies;

import topdownshooter.graphics.Animation;


public class Doge1 extends AnimatedEnemy{

    public Doge1(int s) {
        super(s);
        anim = new Animation(68, "/doge/anim1.png", 8);
        img = anim.currentImg;
        r= 44;
    }

}

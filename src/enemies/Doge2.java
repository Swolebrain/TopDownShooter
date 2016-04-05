
package enemies;

import topdownshooter.graphics.Animation;

public class Doge2 extends AnimatedEnemy{

    public Doge2(int s) {
        super(s);
        anim = new Animation(68, "/doge/anim2.png", 5);
        img = anim.currentImg;
        r= 44;
    }

}

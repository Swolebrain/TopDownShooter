

package enemies;

import topdownshooter.graphics.Animation;


public abstract class AnimatedEnemy extends BasicEnemy {

    protected Animation anim;
    
    public AnimatedEnemy(int s) {
        super();
        r = 35;
        speed = s;
        dx = Math.cos(rad)*speed;
        dy = Math.sin(rad)*speed;
    }
    public void update(){
        if (anim != null && img != null) {
            anim.update();
            img = anim.currentImg;
        }
        super.update();
    }
}

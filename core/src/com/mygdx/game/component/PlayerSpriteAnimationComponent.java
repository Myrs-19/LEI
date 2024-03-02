package com.mygdx.game.component;

import com.badlogic.gdx.graphics.g2d.Animation;
import games.rednblack.editor.renderer.components.sprite.SpriteAnimationComponent;

public class PlayerSpriteAnimationComponent extends SpriteAnimationComponent {

    @Override
    public void reset() {
        super.reset();
        currentAnimation = "idle";
        playMode = Animation.PlayMode.LOOP;
    }
}

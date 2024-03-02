package com.mygdx.game.system;

import com.artemis.ComponentMapper;

import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.Body;

import com.mygdx.game.component.PlayerComponent;
import games.rednblack.editor.renderer.components.ParentNodeComponent;

import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;

import games.rednblack.editor.renderer.components.sprite.SpriteAnimationComponent;
import games.rednblack.editor.renderer.components.sprite.SpriteAnimationStateComponent;

import com.mygdx.game.Constants;

//аннотация - обработка всех компонентов, чей класс является PlayerComponent
@All(PlayerComponent.class)
public class PlayerAnimationSystem extends IteratingSystem {

    //маппер для получения родителя компонента
    protected ComponentMapper<ParentNodeComponent> parentMapper;

    //маппер для получения физики компонента
    protected ComponentMapper<PhysicsBodyComponent> physicsMapper;

    //маппер для получения спрайтов анимаций
    protected ComponentMapper<SpriteAnimationComponent> spriteMapper;

    //маппер для получения состояния спрайтов анимаций
    protected ComponentMapper<SpriteAnimationStateComponent> spriteStateMapper;

    @Override
    protected void process(int entityId) {
        //получаем родителя компонента - главного героя
        ParentNodeComponent nodeComponent = parentMapper.get(entityId);
        //получаем тело главного героя
        Body body = physicsMapper.get(nodeComponent.parentEntity).body;

        //если тело еще null, выходим
        if (body == null)
            return;

        //получаем мапперы для анимаций
        SpriteAnimationComponent spriteAnimationComponent = spriteMapper.get(entityId); //кастомный класс компонента анимации главного персонажа
        SpriteAnimationStateComponent spriteAnimationStateComponent = spriteStateMapper.get(entityId);

        //если текущая анимация cut и она завершилась, то переключаемся на дефолтную анимацию
        if(spriteAnimationComponent.currentAnimation == Constants.PLAYER_ANIMATION_NAME_CUT &&
                spriteAnimationStateComponent.currentAnimation.isAnimationFinished(
                        spriteAnimationStateComponent.time
                )
        ){
            switchToDefault(spriteAnimationStateComponent, spriteAnimationComponent);
        }

        //если нажата клавиша E
        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            //устанавливаем текущую анимацию cut
            spriteAnimationComponent.currentAnimation = Constants.PLAYER_ANIMATION_NAME_CUT;
            //устанавливаем MODE анимации Normal - единождое выполнение
            spriteAnimationComponent.playMode = Animation.PlayMode.NORMAL;
        }

        //устанавливаем собранную анимацию в состояние спрайтов анимаций
        spriteAnimationStateComponent.set(spriteAnimationComponent);
    }

    /**
     * Switch current animation to default
     * @param spriteAnimationStateComponent - state of animation component
     * @param spriteAnimationComponent - component of animation
     */
    private void switchToDefault(
            SpriteAnimationStateComponent spriteAnimationStateComponent,
            SpriteAnimationComponent spriteAnimationComponent
            ){

        spriteAnimationComponent.currentAnimation = Constants.PLAYER_ANIMATION_NAME_IDLE;
        spriteAnimationComponent.playMode = Animation.PlayMode.LOOP;
    }
}

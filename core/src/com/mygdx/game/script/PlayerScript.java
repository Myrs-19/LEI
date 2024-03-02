package com.mygdx.game.script;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.scripts.BasicScript;
import games.rednblack.editor.renderer.utils.ComponentRetriever;
import games.rednblack.editor.renderer.utils.ItemWrapper;

import games.rednblack.editor.renderer.utils.ComponentRetriever;

import com.artemis.ComponentMapper;

public class PlayerScript extends BasicScript {

    protected ComponentMapper<PhysicsBodyComponent> physicsMapper;

    private final Vector2 impulse = new Vector2(0, 0);
    private final Vector2 speed = new Vector2(0, 0);

    protected World mEngine;
    protected int animEntity;

    private PhysicsBodyComponent mPhysicsBodyComponent;


    @Override
    public void init(int item) {
        super.init(item);

        System.out.println("init");

        ItemWrapper itemWrapper = new ItemWrapper(item, mEngine);
        animEntity = itemWrapper.getChild("anim-player").getEntity();

//        mPhysicsBodyComponent = ComponentRetriever.get(item, PhysicsBodyComponent.class, mEngine);
        mPhysicsBodyComponent = physicsMapper.get(item);
    }

    @Override
    public void act(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            move(Direction.LEFT);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            move(Direction.RIGHT);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            move(Direction.UP);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            move(Direction.DOWN);
        }
    }

    public void move(Direction direction){
        Body body = mPhysicsBodyComponent.body;

        Vector2 move = body.getPosition();

        switch (direction){
            case LEFT:
//                impulse.set(-10000, 0);
                move.x -= 2;
                break;
            case RIGHT:
//                impulse.set(10000, 0);
                move.x += 2;
                break;
            case UP:
//                impulse.set(0, 10000);
                move.y += 2;
                break;
            case DOWN:
//                impulse.set(0, -10000);
                move.y -= 2;
                break;
        }

//        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
        body.setTransform(move, 0);
    }

    @Override
    public void dispose() {

    }
}

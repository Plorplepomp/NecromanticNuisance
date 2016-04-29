/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scottdennis.necromanticnuisance;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import screens.PlayScreen;

/**
 *
 * @author Scott
 */
public class PlayerCharacter extends Sprite
{
    public Body b2body;
    Texture texture;
    public World world;
    CircleShape shape;
    
    public PlayerCharacter(World world)
    {   
        //super(new Texture("footman3.png"));
        texture = new Texture("goodWiz.png");
//        setTexture(texture);
        this.world = world;
        
        
        BodyDef bdef = new BodyDef();
        bdef.position.set(100, 400);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.linearDamping = 2;
        b2body = world.createBody(bdef);
         
        /*
        FixtureDef fdef = new FixtureDef();
        shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-25, 50);
        vertice[1] = new Vector2(25, 50);
        vertice[2] = new Vector2(-25, -65);
        vertice[3] = new Vector2(25, -65);
        shape.set(vertice);
        fdef.friction = 25;
        fdef.shape = shape;
        */
        FixtureDef fdef = new FixtureDef();
        shape = new CircleShape();
        shape.setRadius(15f);
        fdef.shape = shape;
        b2body.createFixture(fdef);
        
        setBounds(0, 0, 64, 64);
        setRegion(texture);
    }
    
    public void update(float dt)
    {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }
    
    
}

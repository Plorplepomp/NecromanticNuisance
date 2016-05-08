/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scottdennis.necromanticnuisance;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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
    public float health;
    public boolean dead;
    Sprite emptyHealthBar, fullHealthBar;
    
    public PlayerCharacter(World world, float x, float y)
    {   
        //super(new Texture("footman3.png"));
        texture = new Texture("goodWiz.png");
//        setTexture(texture);
        this.world = world;
        
        health = 1000;
        dead = false;
        
        
        
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
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
    
        emptyHealthBar = new Sprite(new Texture("emptyBar.png"));
        fullHealthBar = new Sprite(new Texture("fullBar.png"));
        
    }
    
    public void update(float dt)
    {
        emptyHealthBar.setPosition(this.getX()+20, this.getY()+54);
        emptyHealthBar.setScale(1f, 0.65f);
        fullHealthBar.setPosition(this.getX()+20, this.getY()+55);
        fullHealthBar.setOrigin(0f,0f);
        fullHealthBar.setScale(health/1000, 0.7f);
        if(!dead)
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if(dead)
            setPosition(-100, -100);
    }
    

    @Override
    public void draw(Batch batch) {
        emptyHealthBar.draw(batch);
        if(health<99999)
            fullHealthBar.draw(batch);
        super.draw(batch); //To change body of generated methods, choose Tools | Templates.
    }
    
}

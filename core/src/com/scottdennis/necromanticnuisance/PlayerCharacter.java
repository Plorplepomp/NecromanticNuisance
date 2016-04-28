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
    private World world;
    PolygonShape shape;
    
    public void PlayerCharacter()
    {   
        texture = new Texture("footman3.png");
        this.setTexture(texture);
        this.world = PlayScreen.world;
        
        BodyDef bdef = new BodyDef();
        bdef.position.set(300, 300);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
                
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
        b2body.createFixture(fdef);
        this.setBounds(b2body.getPosition().x, b2body.getPosition().y, this.getHeight(), this.getWidth());
    }
    
    public void update(float dt)
    {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }
    
    public void draw(SpriteBatch batch)
    {
        super.draw(batch);
    }
    
}

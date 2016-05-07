/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actors;

/**
 *
 * @author Scott
 */

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.scottdennis.necromanticnuisance.NecromanticNuisance;

public class Castle extends Actor
{
    Texture texture;
    MoveToAction ms, ma1, ma2, ma3, ma4, ma5, ma6, ma7, ma8, ma9, ma10;
    public Sprite sprite, emptyHealthBar, fullHealthBar;
    public float health, arrowTimer;
    float velocity;
    
    public Castle(float hlth, float damage,  float x, float y, Stage stage)
    {
        this.setName("castle");
        texture = new Texture("castle.png");
        health = hlth;
        arrowTimer = 3;
        
        sprite = new Sprite(texture);
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        
        emptyHealthBar = new Sprite(new Texture("emptyBar.png"));
        fullHealthBar = new Sprite(new Texture("fullBar.png"));
        
        velocity = 70;

        ms = new MoveToAction();
        ms.setPosition(x, y);
//        ms.setPosition(35f,370f);
        
        Castle.this.addAction(ms);

    }


    


    @Override
    public void act(float delta)
    {
        emptyHealthBar.setPosition(this.getX()-20, this.getY()+130);
        emptyHealthBar.setScale(5f, 0.65f);
        fullHealthBar.setPosition(this.getX()-20, this.getY()+130);
        fullHealthBar.setOrigin(0f,0f);
        emptyHealthBar.setOrigin(0f,0f);
        fullHealthBar.setScale(5*health/20000, 0.7f);
        arrowTimer -= delta;
        super.act(delta);
    }
 
    
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        sprite.setColor(this.getColor());
        batch.setColor(this.getColor());
        sprite.draw(batch);
        emptyHealthBar.draw(batch);
        if(health<99999)
            fullHealthBar.draw(batch);
    
    }
    
    @Override
    protected void positionChanged()
    {
      sprite.setPosition(getX(), getY());
    }
    
}
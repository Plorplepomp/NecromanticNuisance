/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

/**
 *
 * @author Scott
 */
public class Footman extends Actor
{
    Texture texture;
    MoveToAction ms, ma1, ma2, ma3, ma4, ma5, ma6, ma7, ma8, ma9, ma10;
    MoveToAction ma11, ma12, ma13, ma14, ma15, ma16, ma17;
    Sprite sprite;
    public boolean inCombat;
    public float health;
    public float damage;
    float velocity;
    
    public Footman(float health, float dmg,  float x, float y)
    {
        this.setName("footman");
        damage = dmg;
        if(damage < 400)
            texture = new Texture("footman0.png");
        if(damage < 450 && damage >= 400)
            texture = new Texture("footman1.png");
        if(damage < 600 && damage >= 450)
            texture = new Texture("footman2.png");
        if(damage < 1000 && damage >= 600)
            texture = new Texture("champion.png");
        if(damage >= 1000)
            texture = new Texture("footman3.png");
        sprite = new Sprite(texture);
        sprite.setScale(0.65f);
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        this.setZIndex((int) health);
        
       velocity = 80;
       inCombat = false;
        
        ms = new MoveToAction();
        ms.setPosition(100f,375f);
        
        ma1 = new MoveToAction();
        ma1.setPosition(315f,375f);
        ma1.setDuration(((315-100)/velocity));
        
        ma2 = new MoveToAction();
        ma2.setPosition(315f, 180f);
        ma2.setDuration((375-180)/velocity);
        
        ma3 = new MoveToAction();
        ma3.setPosition(510f, 180f);
        ma3.setDuration((510-315)/velocity);
        
        ma4 = new MoveToAction();
        ma4.setPosition(510f, 570f);
        ma4.setDuration((570-180)/velocity);
        
        ma5 = new MoveToAction();
        ma5.setPosition(680f, 570f);
        ma5.setDuration((680-510)/velocity);
        
        ma6 = new MoveToAction();
        ma6.setPosition(680f, 375f);
        ma6.setDuration((570-375)/velocity);
        
        ma7 = new MoveToAction();
        ma7.setPosition(1000f, 375f);
        ma7.setDuration((1000-680)/velocity);
       
           
        
        
        SequenceAction sa1 = new SequenceAction(ms, ma1, ma2, ma3, ma4);
        SequenceAction sa2 = new SequenceAction(ma5, ma6, ma7);
        SequenceAction csa1 = new SequenceAction(sa1, sa2);
        
        if((x==0)&&(y==0))
            this.addAction(csa1);
        
        
            
        
        
    }
    
    @Override
    public void act(float delta)
    {
        if(!inCombat)
            super.act(delta);
//        inCombat = false;
    }
   
    public void draw(Batch batch, float parentAlpha)
    {
        sprite.setColor(this.getColor());
        batch.setColor(this.getColor());
        sprite.draw(batch);
    }
    
    
    @Override
    protected void positionChanged()
    {
      sprite.setPosition(getX(), getY());
      super.positionChanged();
    }
    
}


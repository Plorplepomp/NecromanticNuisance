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
    float health;
    float velocity;
    
    public Footman(float health, float damage)
    {
        this.setName("footman");
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
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        this.setZIndex((int) health);
        
       velocity = 80;
        
        //ScaleByAction sba = new ScaleByAction();
        //sba.setAmount(0.25f);
        
        ms = new MoveToAction();
        ms.setPosition(50f,425f);
        
        ma1 = new MoveToAction();
        ma1.setPosition(195f,425f);
        ma1.setDuration(((195-50)/velocity));
        
        ma2 = new MoveToAction();
        ma2.setPosition(195f, 50f);
        ma2.setDuration((425-50)/velocity);
        
        ma3 = new MoveToAction();
        ma3.setPosition(825f, 50f);
        ma3.setDuration((825-195)/velocity);
        
        ma4 = new MoveToAction();
        ma4.setPosition(825f, 200f);
        ma4.setDuration((200-50)/velocity);
        
        ma5 = new MoveToAction();
        ma5.setPosition(360f, 200f);
        ma5.setDuration((825-360)/velocity);
        
        ma6 = new MoveToAction();
        ma6.setPosition(360f, 580f);
        ma6.setDuration((580-200)/velocity);
        
        ma7 = new MoveToAction();
        ma7.setPosition(550f, 580f);
        ma7.setDuration((550-360)/velocity);
        
        ma8 = new MoveToAction();
        ma8.setPosition(550f, 390f);
        ma8.setDuration((580-390)/velocity);
        
        ma9 = new MoveToAction();
        ma9.setPosition(800f, 390f);
        ma9.setDuration((790-550)/velocity);
        
        ma10 = new MoveToAction();
        ma10.setPosition(800f, 610f);
        ma10.setDuration((610-390)/velocity);
        
        ma11 = new MoveToAction();
        ma11.setPosition(950f, 610f);
        ma11.setDuration((950-800)/velocity);
        
        ma12 = new MoveToAction();
        ma12.setPosition(360f, 580f);
        ma12.setDuration((550-360)/velocity);
        
        ma13 = new MoveToAction();
        ma13.setPosition(360f, 200f);
        ma13.setDuration((580-200)/velocity);
        
        ma14 = new MoveToAction();
        ma14.setPosition(825f, 200f);
        ma14.setDuration((825-360)/velocity);
        
        ma15 = new MoveToAction();
        ma15.setPosition(825f, 50f);
        ma15.setDuration((200-50)/velocity);
        
        ma16 = new MoveToAction();
        ma16.setPosition(195f, 50f);
        ma16.setDuration((825-195)/velocity);
        
        ma17 = new MoveToAction();
        ma17.setPosition(195f, 425f);
        ma17.setDuration((425-50/velocity));
        
        
        
        SequenceAction sa0 = new SequenceAction(ms, ma1); 
        SequenceAction sa1 = new SequenceAction(ma2, ma3, ma4);
        SequenceAction sa2 = new SequenceAction(ma5, ma6, ma7, ma8, ma9);
        SequenceAction sa3 = new SequenceAction(ma10, ma11);
        SequenceAction csa1 = new SequenceAction(sa0, sa1, sa2, sa3);
        /*
        SequenceAction sa3 = new SequenceAction(ma10, ma11, ma12, ma13, ma14);
        SequenceAction sa4 = new SequenceAction(ma15, ma16, ma17);
        SequenceAction csa2 = new SequenceAction(sa3, sa4);
        
        SequenceAction csa3 = new SequenceAction(csa1, csa2);
        SequenceAction csa4 = new SequenceAction(csa1, csa2);
        SequenceAction movement = new SequenceAction(sa0, csa3, csa4);
        */
        Footman.this.addAction(csa1);
        
        
        
    }
    
        @Override
    public void setZIndex(int health)
    {
        this.health = (float)health;
    }
    
    @Override
    public int getZIndex()
    {
        return (int)this.health;
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


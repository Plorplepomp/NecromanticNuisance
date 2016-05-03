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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

/**
 *
 * @author Scott
 */
public class Necromancer extends Actor
{
    Texture texture;
    MoveToAction ms, ma1, ma2, ma3, ma4, ma5, ma6, ma7, ma8, ma9, ma10;
    public Sprite sprite, emptyHealthBar, fullHealthBar;
    public float health, damage, attackTimer;
    float velocity;
    
    public Necromancer(float hlth, float damage, Stage stage)
    {
        this.setName("necromancer");
        texture = new Texture("necromancer.png");
        sprite = new Sprite(texture);
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        health = hlth;
        attackTimer = 2;
        
        emptyHealthBar = new Sprite(new Texture("emptyBar.png"));
        fullHealthBar = new Sprite(new Texture("fullBar.png"));
        velocity = 70;

        
        
        ms = new MoveToAction();
        ms.setPosition(940f,375f);
        
        ma1 = new MoveToAction();
        ma1.setPosition(800f,625f);
        ma1.setDuration((1000-800)/velocity);
        
        ma2 = new MoveToAction();
        ma2.setPosition(800f, 400f);
        ma2.setDuration((625-400)/velocity);
        
        ma3 = new MoveToAction();
        ma3.setPosition(550f, 400f);
        ma3.setDuration((800-550)/velocity);
        
        ma4 = new MoveToAction();
        ma4.setPosition(550f, 580f);
        ma4.setDuration((580-400)/velocity);
        
        ma5 = new MoveToAction();
        ma5.setPosition(360f, 580f);
        ma5.setDuration(5f);
        
        ma6 = new MoveToAction();
        ma6.setPosition(360f, 200f);
        ma6.setDuration((580-200)/velocity);
        
        ma7 = new MoveToAction();
        ma7.setPosition(825f, 200f);
        ma7.setDuration((825-360)/velocity);
        
        ma8 = new MoveToAction();
        ma8.setPosition(825f, 50f);
        ma8.setDuration((200-50)/velocity);
        
        ma9 = new MoveToAction();
        ma9.setPosition(195f, 50f);
        ma9.setDuration((825-195)/velocity);
        
        ma10 = new MoveToAction();
        ma10.setPosition(195f, 800f);
        ma10.setDuration((800-50)/velocity);
        
        SequenceAction sa1 = new SequenceAction(ms, ma1, ma2, ma3, ma4);
        SequenceAction sa2 = new SequenceAction(ma5, ma6, ma7, ma8, ma9);
        SequenceAction csa = new SequenceAction(sa1, sa2, ma10);
        Necromancer.this.addAction(ms);
  /*      
        int len = stage.size;
        for(i=0; i<len; i++){
        Actor a = stageActors.get(i);
        if(a.getName().equals("myactor")){
        //a is your Actor!
        break;
    */
    }


   @Override
    public void act(float delta)
    {
        emptyHealthBar.setPosition(this.getX()-20, this.getY()+100);
        emptyHealthBar.setScale(3f, 0.65f);
        fullHealthBar.setPosition(this.getX()-20, this.getY()+100);
        fullHealthBar.setOrigin(0f,0f);
        emptyHealthBar.setOrigin(0f,0f);
        fullHealthBar.setScale(3*health/20000, 0.7f);
        attackTimer -= delta;
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
      
      /*
      body.x = getX();
      body.y = getY();
      body.height = getHeight();
      body.width = getWidth();
      super.positionChanged();
      
    //  Array<Actor> stageActors = stage.getActors();
      */
    }
    
}

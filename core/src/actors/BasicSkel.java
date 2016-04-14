/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 *
 * @author Scott
 */
public class BasicSkel extends Actor
{
    Texture texture;
    MoveToAction ms, ma1, ma2, ma3, ma4, ma5, ma6, ma7, ma8, ma9, ma10;
    public Sprite sprite;
    public float health;
    public float damage;
    float velocity;
    //public Rectangle body;
    
    public BasicSkel(float health, float damage, float x, float y, Stage stage)
    {
        this.setName("skeleton");
        if(damage < 220)
            texture = new Texture("skelsword1.png");
        if(damage < 240 && damage >= 220)
            texture = new Texture("skelsword2.png");
        if(damage >= 240)
            texture = new Texture("skelsword3.png");
        sprite = new Sprite(texture);
        sprite.setScale(0.65f);
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        
        this.setZIndex((int)health);
        velocity = 70;
        //Rectangle body = new Rectangle(getX(), getY(), getWidth(), getHeight());
        //ScaleByAction sba = new ScaleByAction();
        //sba.setAmount(0.25f);
        
        ms = new MoveToAction();
        ms.setPosition(900f,375f);
        
        ma1 = new MoveToAction();
        ma1.setPosition(680f,375f);
        ma1.setDuration((900-680)/velocity);
        
        ma2 = new MoveToAction();
        ma2.setPosition(680f, 575f);
        ma2.setDuration((575-375)/velocity);
        
        ma3 = new MoveToAction();
        ma3.setPosition(505f, 575f);
        ma3.setDuration((680-505)/velocity);
        
        ma4 = new MoveToAction();
        ma4.setPosition(505f, 185f);
        ma4.setDuration((575-185)/velocity);
        
        ma5 = new MoveToAction();
        ma5.setPosition(310f, 185f);
        ma5.setDuration((505-310)/velocity);
        
        ma6 = new MoveToAction();
        ma6.setPosition(310f, 375f);
        ma6.setDuration((375-185)/velocity);
        
        ma7 = new MoveToAction();
        ma7.setPosition(100f, 375f);
        ma7.setDuration((310-100)/velocity);
        

        SequenceAction sa1 = new SequenceAction(ms, ma1, ma2, ma3, ma4);
        SequenceAction sa2 = new SequenceAction(ma5, ma6, ma7);
        SequenceAction csa = new SequenceAction(sa1, sa2);
        BasicSkel.this.addAction(csa);
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
    public void setZIndex(int health)
    {
        this.health = (float)health;
    }
    
    @Override
    public int getZIndex()
    {
        return (int)this.health;
    }

 /*   @Override
    public void act(float delta)
    {
        Array<Actor> stageActors = stage.getActors();
    }
 */
    
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